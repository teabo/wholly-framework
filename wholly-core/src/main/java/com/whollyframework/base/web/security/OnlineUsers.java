package com.whollyframework.base.web.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.whollyframework.authentications.IWebUser;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;

public class OnlineUsers {

	static Map<String, IWebUser> publicSpace = new HashMap<String, IWebUser>();

	public static void add(String userid, IWebUser user) {
		if (user == null) {
			return;
		}
		if (publicSpace != null) {
			IWebUser theUser = (IWebUser) publicSpace.get(userid);
			if (theUser != null) {
				publicSpace.remove(userid);
			}
		}
		publicSpace.put(userid, user);
	}

	public static IWebUser get(String key) {
		return publicSpace.get(key);
	}

	public static void remove(String key) {
		if (key == null) {
			return;
		}
		publicSpace.remove(key);
	}

	public static int getUsersCount() {
		return publicSpace.size();
	}

	public static DataPackage<IWebUser> query(ParamsTable params) {
		DataPackage<IWebUser> datas = new DataPackage<IWebUser>();
		ArrayList<IWebUser> result = new ArrayList<IWebUser>(
				publicSpace.values());

		datas.rowCount = result.size();
		int page, lines;
		try {
			page = Integer.parseInt(params.getParameterAsString("_currpage"));
		} catch (Exception ex) {
			page = 1;
		}
		try {
			lines = Integer.parseInt(params.getParameterAsString("_pagelines"));
		} catch (Exception ex) {
			lines = Integer.MAX_VALUE;
		}
		datas.pageNo = page;
		datas.linesPerPage = lines;
		// 分页
		try {
			datas.setDatas(result.subList((page - 1) * lines,
					(datas.rowCount > (page) * lines ? (page) * lines
							: datas.rowCount)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}

}
