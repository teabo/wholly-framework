package com.whollyframework.web.wechat.chatapi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.weixin.sdk.api.ApiConfig;
import com.weixin.sdk.api.ApiConfigKit;
import com.weixin.sdk.api.ApiResult;
import com.weixin.sdk.api.MenuApi;
import com.weixin.sdk.api.UserApi;
import com.weixin.sdk.kit.PropKit;
import com.whollyframework.base.action.BaseController;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.utils.http.ResponseUtil;

@Controller
@Scope("prototype")
@RequestMapping(value = "/wechat/api")
public class WeixinApiController extends BaseController<ApiResult, String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6719086487108814821L;

	public WeixinApiController() {
		super(new ApiResult());
	}

	/**
	 * 如果要支持多公众账号，只需要在此返回各个公众号对应的  ApiConfig 对象即可
	 * 可以通过在请求 url 中挂参数来动态从数据库中获取 ApiConfig 属性值
	 */
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
		
		// 配置微信 API 相关常量
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("appSecret"));
		
		/**
		 *  是否对消息进行加密，对应于微信平台的消息加解密方式：
		 *  1：true进行加密且必须配置 encodingAesKey
		 *  2：false采用明文模式，同时也支持混合模式
		 */
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		return ac;
	}
	
	/**
	 * 获取公众号菜单
	 */
	@RequestMapping(value = "/menus")
	public void getMenu(HttpServletRequest request, HttpServletResponse response) {
		this.setRequest(request);
		this.setResponse(response);
		ApiConfigKit.setThreadLocalApiConfig(getApiConfig());
		ApiResult apiResult = MenuApi.getMenu();
		if (apiResult.isSucceed())
			ResponseUtil.setContentToResponse(getResponse(), apiResult.getJson(), "text/plain; charset=UTF-8");
		else
			ResponseUtil.setContentToResponse(getResponse(), apiResult.getErrorMsg(), "text/plain; charset=UTF-8");
	}
	
	/**
	 * 获取公众号关注用户
	 */
	@RequestMapping(value = "/followers")
	public void getFollowers(HttpServletRequest request, HttpServletResponse response) {
		this.setRequest(request);
		this.setResponse(response);
		ApiConfigKit.setThreadLocalApiConfig(getApiConfig());
		ApiResult apiResult = UserApi.getFollows();
		// TODO 用 jackson 解析结果出来
		ResponseUtil.setContentToResponse(getResponse(), apiResult.getJson(), "text/plain; charset=UTF-8");
	}

	public IDesignService<ApiResult, String> getService() {
		// TODO Auto-generated method stub
		return null;
	}
}

