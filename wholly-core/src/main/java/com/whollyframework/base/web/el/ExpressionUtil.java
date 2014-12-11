package com.whollyframework.base.web.el;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.whollyframework.base.model.FormPurview;
import com.whollyframework.util.HtmlEncoder;

/**
 * EL表达式方法定义类
 * 
 * @author Chris Xu
 * @since 2012-07-16
 */
public class ExpressionUtil {
	
	private static final SimpleDateFormat YMDHMS_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();

	/**
	 * 获取自定义按钮是否显示
	 * 
	 * @param formPurview
	 *            权限控制类
	 * @param key
	 *            自定义按钮标识名
	 * @return
	 */
	public static boolean getUserButton(FormPurview formPurview, String key) {
		return formPurview.getUserButton(key);
	}

	/**
	 * 获取字段是否只读
	 * 
	 * @param formPurview
	 *            权限控制类
	 * @param key
	 *            字段标识名
	 * @return
	 */
	public static boolean getFieldReadOnly(FormPurview formPurview, String key) {
		return formPurview.getFieldReadOnly(key);
	}
	
	@SuppressWarnings("rawtypes")
	public static String getListValue(List list, int index) {
		if (list != null && list.size() > 0) {
			Object o = list.get(index);
			return o != null ? o.toString() : null;
		} else {
			return null;
		}
	}
	
	public static String str(Object value){
		if (value==null)
			return "";
		if (value instanceof Date){
			return YMDHMS_FORMAT.format((Date)value);
		}
		return HtmlEncoder.encode(String.valueOf(value));
	}
	
	public static String fmtValue(Object value, String pattern){
		if (value==null)
			return "";
		if (value instanceof Date){
			if ("yyyy-MM-dd HH:mm:ss".equalsIgnoreCase(pattern)){
				return YMDHMS_FORMAT.format((Date)value);
			} else {
				DATE_FORMAT.applyPattern(pattern);
				return DATE_FORMAT.format((Date)value);
			}
		} else if (value instanceof Number){
			DecimalFormat f = new DecimalFormat();
			f.applyPattern(pattern);
			return f.format(value);
		}
		return HtmlEncoder.encode(String.valueOf(value));
	}
	
	public static boolean equalsIgnoreCase(String srcValue, String destValue){
		return srcValue.equalsIgnoreCase(destValue);
	}
}
