/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.whollyframework.web.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.weixin.sdk.api.ApiConfig;
import com.weixin.sdk.api.ApiConfigKit;
import com.weixin.sdk.kit.HttpKit;
import com.weixin.sdk.kit.MsgEncryptKit;
import com.weixin.sdk.kit.PropKit;
import com.weixin.sdk.msg.InMsgParser;
import com.weixin.sdk.msg.OutMsgXmlBuilder;
import com.weixin.sdk.msg.in.InImageMsg;
import com.weixin.sdk.msg.in.InLinkMsg;
import com.weixin.sdk.msg.in.InLocationMsg;
import com.weixin.sdk.msg.in.InMsg;
import com.weixin.sdk.msg.in.InShortVideoMsg;
import com.weixin.sdk.msg.in.InTextMsg;
import com.weixin.sdk.msg.in.InVideoMsg;
import com.weixin.sdk.msg.in.InVoiceMsg;
import com.weixin.sdk.msg.in.event.InCustomEvent;
import com.weixin.sdk.msg.in.event.InFollowEvent;
import com.weixin.sdk.msg.in.event.InLocationEvent;
import com.weixin.sdk.msg.in.event.InMassEvent;
import com.weixin.sdk.msg.in.event.InMenuEvent;
import com.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.weixin.sdk.msg.in.event.InShakearoundUserShakeEvent;
import com.weixin.sdk.msg.in.event.InTemplateMsgEvent;
import com.weixin.sdk.msg.in.event.InVerifyFailEvent;
import com.weixin.sdk.msg.in.event.InVerifySuccessEvent;
import com.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.weixin.sdk.msg.out.OutMsg;
import com.weixin.sdk.msg.out.OutTextMsg;
import com.whollyframework.base.action.BaseController;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.wechat.chathistory.model.ChatHistoryVO;
import com.whollyframework.dbservice.wechat.chathistory.service.ChatHistoryService;
import com.whollyframework.utils.http.ResponseUtil;

/**
 * 接收微信服务器消息，自动解析成 InMsg 并分发到相应的处理方法
 */
public abstract class MsgController extends BaseController<ChatHistoryVO, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1772505600726710673L;
	private static final Logger log = LoggerFactory.getLogger(MsgController.class);
	private String inMsgXml = null; // 本次请求 xml数据
	private InMsg inMsg = null; // 本次请求 xml 解析后的 InMsg 对象象

	@Autowired
	private ChatHistoryService chatHistoryService;
	
	@Override
	public IDesignService<ChatHistoryVO, String> getService() {
		return chatHistoryService;
	}
	
	public MsgController() {
		super(new ChatHistoryVO());
	}

	/**
	 * 如果要支持多公众账号，只需要在此返回各个公众号对应的 ApiConfig 对象即可 可以通过在请求 url 中挂参数来动态从数据库中获取
	 * ApiConfig 属性值
	 */
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();

		// 配置微信 API 相关常量
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("appSecret"));

		/**
		 * 是否对消息进行加密，对应于微信平台的消息加解密方式： 1：true进行加密且必须配置 encodingAesKey
		 * 2：false采用明文模式，同时也支持混合模式
		 */
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey",
				"setting it in config file"));
		return ac;
	}

	/**
	 * weixin 公众号服务器调用唯一入口，即在开发者中心输入的 URL 必须要指向此 action
	 */
	@RequestMapping(value = "/index")
	public void index(HttpServletRequest request, HttpServletResponse response) {
		this.setRequest(request);
		this.setResponse(response);
		ApiConfigKit.setThreadLocalApiConfig(getApiConfig());
		// 开发模式输出微信服务发送过来的 xml 消息
		if (ApiConfigKit.isDevMode()) {
			log.info("接收消息:" + getInMsgXml(true));
		}

		// 解析消息并根据消息类型分发到相应的处理方法
		InMsg msg = getInMsg(true);
		if (msg instanceof InTextMsg)
			processInTextMsg((InTextMsg) msg);
		else if (msg instanceof InImageMsg)
			processInImageMsg((InImageMsg) msg);
		else if (msg instanceof InVoiceMsg)
			processInVoiceMsg((InVoiceMsg) msg);
		else if (msg instanceof InVideoMsg)
			processInVideoMsg((InVideoMsg) msg);
		else if (msg instanceof InShortVideoMsg) // 支持小视频
			processInShortVideoMsg((InShortVideoMsg) msg);
		else if (msg instanceof InLocationMsg)
			processInLocationMsg((InLocationMsg) msg);
		else if (msg instanceof InLinkMsg)
			processInLinkMsg((InLinkMsg) msg);
		else if (msg instanceof InCustomEvent)
			processInCustomEvent((InCustomEvent) msg);
		else if (msg instanceof InFollowEvent)
			processInFollowEvent((InFollowEvent) msg);
		else if (msg instanceof InQrCodeEvent)
			processInQrCodeEvent((InQrCodeEvent) msg);
		else if (msg instanceof InLocationEvent)
			processInLocationEvent((InLocationEvent) msg);
		else if (msg instanceof InMassEvent)
			processInMassEvent((InMassEvent) msg);
		else if (msg instanceof InMenuEvent)
			processInMenuEvent((InMenuEvent) msg);
		else if (msg instanceof InSpeechRecognitionResults)
			processInSpeechRecognitionResults((InSpeechRecognitionResults) msg);
		else if (msg instanceof InTemplateMsgEvent)
			processInTemplateMsgEvent((InTemplateMsgEvent) msg);
		else if (msg instanceof InShakearoundUserShakeEvent)
			processInShakearoundUserShakeEvent((InShakearoundUserShakeEvent) msg);
		else
			log.error("未能识别的消息类型。 消息 xml 内容为：\n" + getInMsgXml());
	}

	public String getPara(String key) {
		return getParams().getParameterAsString(key);
	}

	/**
	 * 在接收到微信服务器的 InMsg 消息后后响应 OutMsg 消息
	 */
	public void render(OutMsg outMsg) {
		String outMsgXml = OutMsgXmlBuilder.build(outMsg);
		// 开发模式向控制台输出即将发送的 OutMsg 消息的 xml 内容
		if (ApiConfigKit.isDevMode()) {
			log.info("发送消息:" + outMsgXml);
			log.info("--------------------------------------------------------------------------------\n");
		}
		outMsg.setMsgXML(outMsgXml);
		ChatHistoryVO vo = new ChatHistoryVO(outMsg);
		try {
			getService().doCreate(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 是否需要加密消息
		if (ApiConfigKit.getApiConfig().isEncryptMessage()) {
			outMsgXml = MsgEncryptKit.encrypt(outMsgXml, getPara("timestamp"), getPara("nonce"));
		}

		ResponseUtil.setContentToResponse(getResponse(), outMsgXml, "text/xml; charset=UTF-8");
	}

	public void renderNull() {
		
	}
	public void renderOutTextMsg(String content) {
		OutTextMsg outMsg = new OutTextMsg(getInMsg());
		outMsg.setContent(content);
		render(outMsg);
	}
	
	public String getInMsgXml() {
		return getInMsgXml(false);
	}

	public String getInMsgXml(boolean submit) {
		if (inMsgXml == null || submit) {
			inMsgXml = HttpKit.readIncommingRequestData(getRequest());
			// 是否需要解密消息
			if (ApiConfigKit.getApiConfig().isEncryptMessage()) {
				inMsgXml = MsgEncryptKit.decrypt(inMsgXml, getPara("timestamp"), getPara("nonce"),
						getPara("msg_signature"));
			}
		}
		return inMsgXml;
	}

	public InMsg getInMsg() {
		return getInMsg(false);
	}
	
	public InMsg getInMsg(boolean submit) {
		if (inMsg == null || submit)
			inMsg = InMsgParser.parse(getInMsgXml(submit));
		ChatHistoryVO vo = new ChatHistoryVO(inMsg);
		try {
			getService().doCreate(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inMsg;
	}

	// 处理接收到的文本消息
	protected abstract void processInTextMsg(InTextMsg inTextMsg);

	// 处理接收到的图片消息
	protected abstract void processInImageMsg(InImageMsg inImageMsg);

	// 处理接收到的语音消息
	protected abstract void processInVoiceMsg(InVoiceMsg inVoiceMsg);

	// 处理接收到的视频消息
	protected abstract void processInVideoMsg(InVideoMsg inVideoMsg);

	// 处理接收到的视频消息
	protected abstract void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg);

	// 处理接收到的地址位置消息
	protected abstract void processInLocationMsg(InLocationMsg inLocationMsg);

	// 处理接收到的链接消息
	protected abstract void processInLinkMsg(InLinkMsg inLinkMsg);

	// 处理接收到的多客服管理事件
	protected abstract void processInCustomEvent(InCustomEvent inCustomEvent);

	// 处理接收到的关注/取消关注事件
	protected abstract void processInFollowEvent(InFollowEvent inFollowEvent);

	// 处理接收到的扫描带参数二维码事件
	protected abstract void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent);

	// 处理接收到的上报地理位置事件
	protected abstract void processInLocationEvent(InLocationEvent inLocationEvent);

	// 处理接收到的群发任务结束时通知事件
	protected abstract void processInMassEvent(InMassEvent inMassEvent);

	// 处理接收到的自定义菜单事件
	protected abstract void processInMenuEvent(InMenuEvent inMenuEvent);

	// 处理接收到的语音识别结果
	protected abstract void processInSpeechRecognitionResults(InSpeechRecognitionResults inSpeechRecognitionResults);

	// 处理接收到的模板消息是否送达成功通知事件
	protected abstract void processInTemplateMsgEvent(InTemplateMsgEvent inTemplateMsgEvent);

	// 处理微信摇一摇事件
	protected abstract void processInShakearoundUserShakeEvent(InShakearoundUserShakeEvent inShakearoundUserShakeEvent);

	// 资质认证成功 || 名称认证成功 || 年审通知 || 认证过期失效通知
	protected abstract void processInVerifySuccessEvent(InVerifySuccessEvent inVerifySuccessEvent);

	// 资质认证失败 || 名称认证失败
	protected abstract void processInVerifyFailEvent(InVerifyFailEvent inVerifyFailEvent);
}
