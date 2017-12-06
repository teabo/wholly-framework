package com.whollyframework.web.wechat.chatmsg;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.whollyframework.dbservice.wechat.msgcategory.model.MsgCategoryVO;

public class WeChatMsgCategoryUtil {

	private static CopyOnWriteArrayList<MsgCategoryVO> MsgCategorys = new CopyOnWriteArrayList<MsgCategoryVO>();
	
	
	public static void load(List<MsgCategoryVO> categorys){
		MsgCategorys = new CopyOnWriteArrayList<MsgCategoryVO>(categorys);
	}
	
	
	public static String categorys2String(){
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < MsgCategorys.size(); i++) {
			buf.append(MsgCategorys.get(i).toString()).append("\n");
		}
		return buf.toString();
	}
	
	
}
