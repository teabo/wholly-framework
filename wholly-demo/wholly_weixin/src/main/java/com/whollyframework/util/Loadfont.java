package com.whollyframework.util;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Loadfont {

	public static Font loadFont(String fontFileName, float fontSize) // 第一个参数是外部字体名，第二个是字体大小
	{
		InputStream in = null;
		try {
			URL propsUrl = Thread.currentThread().getContextClassLoader()
					.getResource(fontFileName);
			in = propsUrl.openStream();
			Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, in);
			in.close();
			return dynamicFont.deriveFont(fontSize);
		} catch (Exception e) {// 异常处理
			e.printStackTrace();
			return new java.awt.Font("宋体", Font.PLAIN, 14);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static java.awt.Font getFont(String name) {
		Font font = Loadfont.loadFont("fonts/" + name + ".ttf", 24f);// 调用
		return font;// 返回字体
	}

	
	public static void main(String[] args) {
		
		
		

	}
}
