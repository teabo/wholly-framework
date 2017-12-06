/**
 * Copyright (c) 2011-2015, Unas 小强哥 (unas@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.weixin.sdk.api;

import com.weixin.sdk.kit.HttpKit;

/**
 * 生成带参数的二维码 API
 * https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN
 */
public class QrcodeApi
{
	private static String apiUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
	
	public static ApiResult create(String jsonStr) {
		String jsonResult = HttpKit.post(apiUrl + AccessTokenApi.getAccessTokenStr(), jsonStr);
		return new ApiResult(jsonResult);
	}
	
}
