package com.whollyframework.base.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.whollyframework.base.model.PersistenceUtils;

public class PersistenceFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {

		HttpServletRequest hreq = (HttpServletRequest) request;

		String uri = hreq.getRequestURI();

		if (uri.indexOf(".action") < 0 && uri.indexOf(".jsp") < 0 && uri.indexOf("/dwr/") < 0) {
			chain.doFilter(request, response);
			return;
		}

		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceUtils.closeConnection();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}
}
