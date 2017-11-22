package com.whollyframework.utils.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtil {
	public static void setTextToResponse(HttpServletResponse response, String text) {
		PrintWriter out = null;
		try {
			response.setHeader("Content-Type", "text/html; charset=UTF-8");
			out = response.getWriter();
			out.print(text);
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if(out != null)
				out.close();
		}
	}

	public static void setJsonToResponse(HttpServletResponse response, String JSON) {
		PrintWriter out = null;
		try {
			response.setHeader("Content-Type", "application/x-json; charset=UTF-8");
			out = response.getWriter();
			out.print(JSON);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null)
				out.close();
		}
	}

	public static void setXmlToResponse(HttpServletResponse response, String XML) {
		PrintWriter out = null;
		try {
			response.setHeader("Content-Type", "application/xml; charset=UTF-8");
			out = response.getWriter();
			out.print(XML);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null)
				out.close();
		}
	}

	public static void setContentToResponse(HttpServletResponse response, String outMsgXml, String contentType) {
		PrintWriter out = null;
		try {
			response.setHeader("Content-Type", contentType);
			out = response.getWriter();
			out.print(outMsgXml);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null)
				out.close();
		}
	}
}
