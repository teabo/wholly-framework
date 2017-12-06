package com.whollyframework.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.whollyframework.util.Security;

/**
 * e.g."/s/encryptor?word=sunshine" or "/s/encryptor?word=sunshine&type=base64" ;
 * @author Administrator
 *
 */
public class EncryptorServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9009559826371889390L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String word = request.getParameter("word");
		String type = request.getParameter("type");
		
		String password;
		if ("base64".equalsIgnoreCase(type)){
			password = Security.encodeToBASE64(word);
		} else {
			password = Security.encryptPassword(word);
		}
		
		PrintWriter out=response.getWriter(); 
		out.println("<html><body><h1>"+word+" encryptPassword : </h1>"+ password +"</body></html>"); 
		out.flush(); 
	}

}
