package com.whollyframework.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weixin.sdk.kit.HttpKit;
import com.weixin.sdk.kit.PropKit;

public class GenSingDesginUtil {
	private static final Logger log =  LoggerFactory.getLogger(GenSingDesginUtil.class);
	public static final String CHARSET = "UTF-8";
	private static final String ORIGIN = "http://www.yishuzi.com";
	private static final String POST_URL = "http://www.yishuzi.com/a/re.php";

	public static String encode(String qm) {
		try {
			return URLEncoder.encode(qm, CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String genImage(String qm, String genType) {
		return genImage(qm, genType, PropKit.get("imagePath"));
	}
	
	public static String genImage(String qm, String genType, String destPath) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("qm", qm);
		params.put("genType", genType);
		return genImage(params, destPath);
	}

	public static String genImage(Map<String, String> params) {
		return genImage(params, PropKit.get("imagePath"));
	}
	
	public static String genImage(Map<String, String> params, String destPath) {
		log.debug("生成签名设计……");
		
		Map<String, String> headers = new HashMap<String, String>();

		headers.put("Accept", "*/*");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		headers.put("Connection", "keep-alive");
		headers.put("Host", "www.yishuzi.com");
		headers.put("Origin", ORIGIN);
		headers.put("Referer", ORIGIN+"/");
		
		if (params.get("qm")==null){
			throw new RuntimeException("姓名不能为空！");
		}
		StringBuilder query = new StringBuilder();
		
		//&id1609=jiqie&id1608=jiqie_com&idi=jiqie
		query.append("id=").append(params.get("qm")).append("&id1609=jiqie&id1608=jiqie_com&idi=jiqie&id1=").append(params.get("genType"));
		
		//bgcolor id2
		if (params.get("bgcolor")==null){
			params.put("bgcolor", "%23FFFFFF");
		}
		//signColorB id4
		if (params.get("signColorB")==null){
			params.put("signColorB", "%234E2880");
		}
		//signColor id6
		if (params.get("signColor")==null){
			params.put("signColor", "%23540000");
		}
		query.append("&id2=").append(params.get("bgcolor"));
		query.append("&id3=&id4=").append(params.get("signColorB"));
		query.append("&id5=&id6=").append(params.get("signColor"));

		String s = HttpKit.post(POST_URL, query.toString(), headers);
		Pattern p = Pattern.compile("(/.*(?=\"))");
		Matcher m = p.matcher(s);
		String result = "";
		while (m.find()) {
			result = m.group(0);
		}

		String imgName = ORIGIN + result;
		try {
			URL url = new URL(imgName);
			BufferedImage buffImg = ImageIO.read(url);

			Graphics2D g = buffImg.createGraphics();

			// 将图像填充为白色
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 182, 40);

			String fileName = System.currentTimeMillis() + ".png";
			String destFile = destPath + "/" + fileName;
			File file = new File(destFile);
			file.createNewFile();
			ImageIO.write(buffImg, "png", file);
			log.debug("生成签名设计成功!");
			return fileName;
		} catch (Exception e) {
			log.error("生成签名设计失败：", e);
			throw new RuntimeException("生成签名设计失败：", e);
		}
	}
	
	public static void main(String[] args) {
		try {
			GenSingDesginUtil.genImage(URLEncoder.encode("刘德华",CHARSET), "901", "C:\\Users\\Chris Hsu\\Desktop\\");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
