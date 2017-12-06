package com.whollyframework.web.init;

public class InitSystem {

	public static void init(){
		// 初始化数据库
		InitDateBase initDateBase = new InitDateBase();
		initDateBase.run();
		
		// 初始化系统配置信息
//		InitSysConfig initSystemContext = new InitSysConfig();
//		initSystemContext.run();
		
		// 初始化用户信息
		InitUserInfo initUserInfo = new InitUserInfo();
		initUserInfo.run();
	}
}
