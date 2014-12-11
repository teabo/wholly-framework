package com.whollyframework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The html encode utility.
 */
public class HtmlEncoder {

	private static final String[] htmlCode = new String[256];
	public final static String REGEX_SCRIPT = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>
	public final static String REGEX_STYLE = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>
	public final static String REGEX_HTML = "<[^>]+>"; // 定义HTML标签的正则表达式
	public final static String REGEX_HTML1 = "<[^>]+";

	static {
		for (int i = 0; i < 10; i++) {
			htmlCode[i] = "&#00" + i + ";";
		}

		for (int i = 10; i < 32; i++) {
			htmlCode[i] = "&#0" + i + ";";
		}

		for (int i = 32; i < 128; i++) {
			htmlCode[i] = String.valueOf((char) i);
		}

		htmlCode['\"'] = "&quot;";
		htmlCode['&'] = "&amp;";
		htmlCode['<'] = "&lt;";
		htmlCode['>'] = "&gt;";
		// htmlCode[' '] = "&nbsp;";

		for (int i = 128; i < 256; i++) {
			htmlCode[i] = "&#" + i + ";";
		}
	}

	/**
	 * Encode the given text into html.
	 * 
	 * @param s
	 *            The text to encode
	 * @return The encoded string
	 */
	public static String encode(String s) {
		if (!StringUtil.isBlank(s)) {
			int n = s.length();
			char character;
			StringBuffer buffer = new StringBuffer();

			for (int i = 0; i < n; i++) {
				character = s.charAt(i);
				try {
					buffer.append(htmlCode[character]);
				} catch (ArrayIndexOutOfBoundsException aioobe) {
					buffer.append(character);
				}
			}
			return buffer.toString();
		} else {
			return s;
		}
	}

	public static String Html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		Pattern p_script;
		Matcher m_script;
		Pattern p_style;
		Matcher m_style;
		Pattern p_html;
		Matcher m_html;

		Pattern p_html1;
		Matcher m_html1;

		try {
			p_script = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_html1 = Pattern.compile(REGEX_HTML1, Pattern.CASE_INSENSITIVE);
			m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return textStr;// 返回文本字符串
	}

	public static boolean hasHtmlTagName(String target) {
		if (find(target, REGEX_SCRIPT) || find(target, REGEX_STYLE)
				|| find(target, REGEX_HTML) || find(target, REGEX_HTML1)) {
			return true;
		}
		return false;
	}

	private static boolean find(String htmlStr, String regex) {
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(htmlStr);
		return m.find();
	}

	/**
	 * 把文本编码为Html代码
	 * 
	 * @param target
	 * @return 编码后的字符串
	 */
	public static String htmEncode(String target) {
		StringBuffer stringbuffer = new StringBuffer();
		int j = target.length();
		for (int i = 0; i < j; i++) {
			char c = target.charAt(i);
			switch (c) {
			case 60:
				stringbuffer.append("&lt;");
				break;
			case 62:
				stringbuffer.append("&gt;");
				break;
			case 38:
				stringbuffer.append("&amp;");
				break;
			case 34:
				stringbuffer.append("&quot;");
				break;
			case 169:
				stringbuffer.append("&copy;");
				break;
			case 174:
				stringbuffer.append("&reg;");
				break;
			case 165:
				stringbuffer.append("&yen;");
				break;
			case 8364:
				stringbuffer.append("&euro;");
				break;
			case 8482:
				stringbuffer.append("&#153;");
				break;
			case 13:
				if (i < j - 1 && target.charAt(i + 1) == 10) {
					stringbuffer.append("<br>");
					i++;
				}
				break;
			case 32:
				if (i < j - 1 && target.charAt(i + 1) == ' ') {
					stringbuffer.append(" &nbsp;");
					i++;
					break;
				}
			default:
				stringbuffer.append(c);
				break;
			}
		}
		return new String(stringbuffer.toString());
	}
}
