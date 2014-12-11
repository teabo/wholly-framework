package com.whollyframework.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.whollyframework.utils.image.ImageCodeUtil;

public class CheckCodeServlet extends HttpServlet {

	private static final long serialVersionUID = -3765376273799867603L;

	

	/**
	 * HttpServlet方法实现，验证码图片升成逻辑 1.设置浏览器不要缓存此图片 2.创建内存图象并获得其图形上下文 3.产生随机的认证码
	 * 4.产生图像 5.结束图像的绘制过程，完成图像 6.将图像输出到客户端 7.将当前验证码存入到Session
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ImageCodeUtil util = new ImageCodeUtil();
		util.crimg(request, response);
	}

	
}
