package com.whollyframework.web.wechat.chatmsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.weixin.sdk.api.ApiConfig;
import com.weixin.sdk.kit.PropKit;
import com.weixin.sdk.msg.in.InImageMsg;
import com.weixin.sdk.msg.in.InLinkMsg;
import com.weixin.sdk.msg.in.InLocationMsg;
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
import com.weixin.sdk.msg.out.OutMusicMsg;
import com.weixin.sdk.msg.out.OutNewsMsg;
import com.weixin.sdk.msg.out.OutTextMsg;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.wechat.chathistory.model.ChatHistoryVO;
import com.whollyframework.dbservice.wechat.chathistory.service.ChatHistoryService;
import com.whollyframework.web.wechat.MsgController;

/**
 * WeixinController 的 index方法即可直接运行看效果，在此基础之上修改相关的方法即可进行实际项目开发
 */
@Controller
@RequestMapping(value = "/wechat/msg")
public class WeixinMsgController extends MsgController {
	static Logger logger = LoggerFactory.getLogger(WeixinMsgController.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 6593275967346985187L;
	private static final String helpStr = "TEABO-茶博网。分享技术、分享方法、分享感悟、分享思想、完美人生。提示：\n1.发送 help 获取帮助。\n2.发送 news 查看TEABO最新动态。\n\t公众号持续更新中，想要更多惊喜欢迎每天关注 ^_^";
	
	protected void processInTextMsg(InTextMsg inTextMsg) {
		String msgContent = inTextMsg.getContent().trim();
		Process:try {
			if ("help".equalsIgnoreCase(msgContent)) {
				OutTextMsg outMsg = new OutTextMsg(inTextMsg);
				outMsg.setContent(helpStr);
				render(outMsg);
			} else if ("news".equalsIgnoreCase(msgContent)) {
				OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
				outMsg.addNews(
						"茶博开讲啦",
						"《茶博开讲啦》是由Teabo团队共同打造的一个IT技术博客论坛。Teabo团队成员会定期贡献出自己的IT技术心得体会、分享编程思想。^_^",
						"http://blog.teabo.cn/zb_users/upload/2015/02/201502261424936528113651.png",
						"http://blog.teabo.cn/?id=1");
				outMsg.addNews(
						"茶博开讲啦",
						"TEABO-茶博网。分享技术、分享方法、分享感悟、分享思想、完美人生。^_^",
						"http://blog.teabo.cn/zb_users/upload/2015/03/201503121426145585724945.png",
						"http://blog.teabo.cn/?id=4");
				render(outMsg);
			} else if ("music".equalsIgnoreCase(msgContent)) {
				OutMusicMsg outMsg = new OutMusicMsg(inTextMsg);
				outMsg.setTitle("Listen To Your Heart");
				outMsg.setDescription("建议在 WIFI 环境下流畅欣赏此音乐");
				outMsg.setMusicUrl("http://www.jfinal.com/Listen_To_Your_Heart.mp3");
				outMsg.setHqMusicUrl("http://www.jfinal.com/Listen_To_Your_Heart.mp3");
				outMsg.setFuncFlag(true);
				render(outMsg);
			} else if ("美女".equalsIgnoreCase(msgContent)) {
				OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
				outMsg.addNews(
						"我们只看美女",
						"又一大波美女来袭，我们只看美女 ^_^",
						"https://mmbiz.qlogo.cn/mmbiz/zz3Q6WSrzq3DmIGiadDEicRIp69r1iccicwKEUOKuLhYgjibyU96ia581gCf5o3kicqz6ZLdsDyUtLib0q0hdgHtZOf4Wg/0",
						"http://mp.weixin.qq.com/s?__biz=MjM5ODAwOTU3Mg==&mid=202080887&idx=1&sn=0649c67de565e2d863bf3b8feee24da0#rd");
				render(outMsg);
			} else {
				break Process;
			}
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		OutTextMsg outMsg = new OutTextMsg(inTextMsg);
		outMsg.setContent(inTextMsg.getContent() + "\t文本消息输入异常请重新输入！ " + "\n\n" + helpStr);
		render(outMsg);
		// //转发给多客服PC客户端
		// OutCustomMsg outCustomMsg = new OutCustomMsg(inTextMsg);
		// render(outCustomMsg);
	}
	
	
	
	@Override
	protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
		// 转发给多客服PC客户端
//		OutCustomMsg outCustomMsg = new OutCustomMsg(inVoiceMsg);
//		render(outCustomMsg);
		OutTextMsg outMsg = new OutTextMsg(inVoiceMsg);
		outMsg.setContent("\t文本消息输入异常请重新输入！ " + "\n\n" + helpStr);
		render(outMsg);
	}

	@Override
	protected void processInVideoMsg(InVideoMsg inVideoMsg) {
		// 转发给多客服PC客户端
//		OutCustomMsg outCustomMsg = new OutCustomMsg(inVideoMsg);
//		render(outCustomMsg);
		OutTextMsg outMsg = new OutTextMsg(inVideoMsg);
		outMsg.setContent("\t文本消息输入异常请重新输入！ " + "\n\n" + helpStr);
		render(outMsg);
	}

	@Override
	protected void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg) {
		// 转发给多客服PC客户端
//		OutCustomMsg outCustomMsg = new OutCustomMsg(inShortVideoMsg);
//		render(outCustomMsg);
		OutTextMsg outMsg = new OutTextMsg(inShortVideoMsg);
		outMsg.setContent("\t文本消息输入异常请重新输入！ " + "\n\n" + helpStr);
		render(outMsg);
	}

	@Override
	protected void processInLocationMsg(InLocationMsg inLocationMsg) {
		// 转发给多客服PC客户端
//		OutCustomMsg outCustomMsg = new OutCustomMsg(inLocationMsg);
//		render(outCustomMsg);
		OutTextMsg outMsg = new OutTextMsg(inLocationMsg);
		outMsg.setContent("\t文本消息输入异常请重新输入！ " + "\n\n" + helpStr);
		render(outMsg);
	}

	@Override
	protected void processInLinkMsg(InLinkMsg inLinkMsg) {
		// 转发给多客服PC客户端
//		OutCustomMsg outCustomMsg = new OutCustomMsg(inLinkMsg);
//		render(outCustomMsg);
		OutTextMsg outMsg = new OutTextMsg(inLinkMsg);
		outMsg.setContent("\t文本消息输入异常请重新输入！ " + "\n\n" + helpStr);
		render(outMsg);
	}

	@Override
	protected void processInCustomEvent(InCustomEvent inCustomEvent) {
		logger.debug("测试方法：processInCustomEvent()");
		renderNull();
	}

	protected void processInImageMsg(InImageMsg inImageMsg) {
		// 转发给多客服PC客户端
//		OutCustomMsg outCustomMsg = new OutCustomMsg(inImageMsg);
//		render(outCustomMsg);
		OutTextMsg outMsg = new OutTextMsg(inImageMsg);
		outMsg.setContent("\t文本消息输入异常请重新输入！ " + "\n\n" + helpStr);
		render(outMsg);
	}

	/**
	 * 实现父类抽方法，处理关注/取消关注消息
	 */
	protected void processInFollowEvent(InFollowEvent inFollowEvent) {
		if (InFollowEvent.EVENT_INFOLLOW_SUBSCRIBE.equals(inFollowEvent
				.getEvent())) {
			logger.debug("关注：" + inFollowEvent.getFromUserName());
			OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
			outMsg.setContent("感谢您的关注!\n" + helpStr);
			render(outMsg);
		}
		// 如果为取消关注事件，将无法接收到传回的信息
		if (InFollowEvent.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEvent
				.getEvent())) {
			logger.debug("取消关注：" + inFollowEvent.getFromUserName());
		}
	}

	@Override
	protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
		if (InQrCodeEvent.EVENT_INQRCODE_SUBSCRIBE.equals(inQrCodeEvent
				.getEvent())) {
			logger.debug("扫码未关注：" + inQrCodeEvent.getFromUserName());
			OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
			outMsg.setContent("感谢您的关注，二维码内容：" + inQrCodeEvent.getEventKey());
			render(outMsg);
		}
		if (InQrCodeEvent.EVENT_INQRCODE_SCAN.equals(inQrCodeEvent.getEvent())) {
			logger.debug("扫码已关注：" + inQrCodeEvent.getFromUserName());
		}
	}

	@Override
	protected void processInLocationEvent(InLocationEvent inLocationEvent) {
		logger.debug("发送地理位置事件：" + inLocationEvent.getFromUserName());
		OutTextMsg outMsg = new OutTextMsg(inLocationEvent);
		outMsg.setContent("地理位置是：" + inLocationEvent.getLatitude());
		render(outMsg);
	}

	@Override
	protected void processInMassEvent(InMassEvent inMassEvent) {
		logger.debug("测试方法：processInMassEvent()");
		renderNull();
	}

	/**
	 * 实现父类抽方法，处理自定义菜单事件
	 */
	protected void processInMenuEvent(InMenuEvent inMenuEvent) {
		logger.debug("菜单事件：" + inMenuEvent.getFromUserName());
		OutTextMsg outMsg = new OutTextMsg(inMenuEvent);
		outMsg.setContent("菜单事件内容是：" + inMenuEvent.getEventKey());
		render(outMsg);
	}

	@Override
	protected void processInSpeechRecognitionResults(
			InSpeechRecognitionResults inSpeechRecognitionResults) {
		logger.debug("语音识别事件：" + inSpeechRecognitionResults.getFromUserName());
		OutTextMsg outMsg = new OutTextMsg(inSpeechRecognitionResults);
		outMsg.setContent("语音识别内容是："
				+ inSpeechRecognitionResults.getRecognition());
		render(outMsg);
	}

	@Override
	protected void processInTemplateMsgEvent(
			InTemplateMsgEvent inTemplateMsgEvent) {
		logger.debug("测试方法：processInTemplateMsgEvent()");
		renderNull();
	}

	

	@Override
	protected void processInShakearoundUserShakeEvent(
			InShakearoundUserShakeEvent inShakearoundUserShakeEvent) {
		logger.debug("摇一摇周边设备信息通知事件："
				+ inShakearoundUserShakeEvent.getFromUserName());
		OutTextMsg outMsg = new OutTextMsg(inShakearoundUserShakeEvent);
		outMsg.setContent("摇一摇周边设备信息通知事件UUID："
				+ inShakearoundUserShakeEvent.getUuid());
		render(outMsg);
	}

	@Override
	protected void processInVerifySuccessEvent(
			InVerifySuccessEvent inVerifySuccessEvent) {
		logger.debug("资质认证成功通知事件：" + inVerifySuccessEvent.getFromUserName());
		OutTextMsg outMsg = new OutTextMsg(inVerifySuccessEvent);
		outMsg.setContent("资质认证成功通知事件：" + inVerifySuccessEvent.getExpiredTime());
		render(outMsg);
	}

	@Override
	protected void processInVerifyFailEvent(InVerifyFailEvent inVerifyFailEvent) {
		logger.debug("资质认证失败通知事件：" + inVerifyFailEvent.getFromUserName());
		OutTextMsg outMsg = new OutTextMsg(inVerifyFailEvent);
		outMsg.setContent("资质认证失败通知事件：" + inVerifyFailEvent.getFailReason());
		render(outMsg);
	}

}
