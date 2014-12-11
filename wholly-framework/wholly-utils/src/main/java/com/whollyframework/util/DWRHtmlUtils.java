package com.whollyframework.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DWRHtmlUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	public static String createOptions(Map<String, String> map, String selectFieldName, String def) {
		StringBuffer fun = new StringBuffer();
		fun.append("new Function(\"");
		fun.append(createOptionScript(map, selectFieldName, def));
		fun.append("\")");

		return fun.toString();
	}

	public static String createOptionScript(Map<String, String> map, String selectFieldName, String def) {
		StringBuffer fun = new StringBuffer();
		fun.append("var menuTemp=document.getElementsByName('" + selectFieldName + "')[0];");
		fun.append("for (var m = menuTemp.options.length - 1; m >= 0; m--) {menuTemp.options[m] = null;}");

		int i = 0;

		for (Iterator<Entry<String, String>> iter = map.entrySet().iterator(); iter.hasNext(); i++) {
			//Object key = iter.next();
			Map.Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String value = entry.getValue();

			fun.append("menuTemp.options[" + i + "] = new Option('" + value + "', '" + key + "');");

			if (key.equals(def)) {
				fun.append("menuTemp.options[" + i + "].selected = true;");
			}
		}

		return fun.toString();
	}

	public static String createHtmlStr(Map<String, String> map, String divid, String[] def) {
		StringBuffer fun = new StringBuffer();
//		int imgid = 0;
		fun.append("{");
		fun.append("var div = document.getElementById('" + divid + "');");

		fun.append("var htmtext = \"");
		fun.append("<table cellpadding='0' cellspacing='0' class='checkbox-text'>");

		for (Iterator<Entry<String, String>> iter = map.entrySet().iterator(); iter.hasNext();) {
			fun.append("<tr>");
			//String key = iter.next();
			Map.Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String value = entry.getValue();
			String checked = "";
			if (def != null) {
				for (int k = 0; k < def.length; k++) {
					if (def[k] != null && def[k].equals(key)) {
						checked = " checked ";
						break;
					}
				}
			}
			
			fun.append("<td><input name='_nextids' type='checkbox' value='");
			fun.append(key).append("'").append(checked).append(" />");
			fun.append(value).append("</td>");
			/*
			fun.append("<td>");
			fun.append("{*[Auditor]*}：<input onclick='showSelectUserDiv(this);' id='" + imgid + "' name='isToPerson' type='checkbox' />");
			fun.append("</td>");
			fun.append("<td>");
			fun.append("<div id='opra_" + imgid + "' style='display:none;'>");
			fun.append("<input id='input_" + imgid + "' name='input_" + imgid + "' readonly='true' type='text' />");
			fun.append("<img id='selectUserImg_" + imgid + "' style='cursor:pointer;' onclick='selectUser({nextNodeId:" + key
					+ ",readonly: false})' src='/obpm/script/dialog/images/userselect.gif' /> ");
			fun.append("</div>");

			fun.append("</td>");
			*/
//			imgid++;

			fun.append("</tr>");
		}

		fun.append("</table>");
		fun.append("\";");
		fun.append("div.innerHTML = htmtext;");
		fun.append("}");
		return fun.toString();
	}

	public static String createCheckbox(Map<String, String> map, String divid, String[] def) {
		StringBuffer fun = new StringBuffer();
		fun.append("{");
		fun.append("var div = document.all('" + divid + "');");

		fun.append("var htmtext = \"");
		fun.append("<table cellpadding='0' cellspacing='0' class='checkbox-text'>");
		for (Iterator<Entry<String, String>> iter = map.entrySet().iterator(); iter.hasNext();) {
			fun.append("<tr>");
			for (int j = 0; j < 3 && iter.hasNext(); j++) {
				//String key = iter.next();
				Map.Entry<String, String> entry = iter.next();
				String key = entry.getKey();
				String value = entry.getValue();
				String checked = "";
				if (def != null) {
					for (int k = 0; k < def.length; k++) {
						if (def[k] != null && def[k].equals(key)) {
							checked = " checked ";
							break;
						}
					}
				}
				fun.append("<td><input name='roleids' type='checkbox' value='").append(key).append("'").append(checked).append(" /></td>");
				fun.append("<td>").append(value).append("</td>");
			}
			fun.append("</tr>");
		}
		fun.append("</table>");
		fun.append("\";");
		fun.append("div.innerHTML = htmtext;");
		fun.append("}");
		return fun.toString();
	}

	/**
	 * 根据form filed生成checkbox
	 * 
	 * @param map
	 * @param divid
	 * @param def
	 * @return
	 */
	public static String createFiledCheckbox(Map<String, String> map, String divid, String[] def) {
		StringBuffer fun = new StringBuffer();
		fun.append("{");
		fun.append("var div = document.all('" + divid + "');");

		fun.append("var htmtext = \"");
		fun.append("<table>");
		for (Iterator<Entry<String, String>> iter = map.entrySet().iterator(); iter.hasNext();) {
			fun.append("<tr >");
			for (int j = 0; j < 3 && iter.hasNext(); j++) {
				//String key = iter.next();
				Map.Entry<String, String> entry = iter.next();
				String key = entry.getKey();
				String value = entry.getValue();
				String checked = "";
				if (def != null) {
					for (int k = 0; k < def.length; k++) {
						if (def[k] != null && def[k].equals(key)) {
							checked = " checked ";
							break;
						}
					}
				}
				fun.append("<td><input name='colids' type='checkbox' value='").append(key).append("'").append(checked).append(" /></td>");
				fun.append("<td class='commFont'>").append(value).append("</td>");
			}
			fun.append("</tr>");
		}
		fun.append("</table>");
		fun.append("\";");
		fun.append("div.innerHTML = htmtext;");
		fun.append("}");
		return fun.toString();
	}

}
