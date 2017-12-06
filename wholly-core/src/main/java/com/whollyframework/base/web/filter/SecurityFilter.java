package com.whollyframework.base.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.whollyframework.authentications.IWebUser;
import com.whollyframework.base.model.AnonymousUser;
import com.whollyframework.base.model.WebUser;
import com.whollyframework.base.web.security.OnlineUserBindingListener;
import com.whollyframework.constans.Environment;
import com.whollyframework.constans.Web;
import com.whollyframework.util.ObjectUtil;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.property.DefaultProperty;
import com.whollyframework.util.sequence.Sequence;

/**
 * 
 * @author Chris
 * 
 */
public class SecurityFilter extends HttpServlet implements Filter {
	private final static Logger LOG = Logger.getLogger(SecurityFilter.class);

	private static final long serialVersionUID = -853305800678372152L;

	private static boolean ACCESS_ADMIN = false;
	
	private static boolean ANONYMOUS_ACCESS = false;
	
	private ICustomSecurityFilter customfilter = null;
	
	public SecurityFilter(){
		String filter = DefaultProperty.getProperty(Web.FRAMEWORK_SECURITY_FILTER);
		if (!StringUtil.isBlank(filter))
			customfilter = (ICustomSecurityFilter) ObjectUtil.getInstance(filter);
	}

	static {
		try {
			ACCESS_ADMIN = Boolean.parseBoolean(DefaultProperty
					.getProperty(Web.FRAMEWORK_ADMIN_ACCESS));
			ANONYMOUS_ACCESS= Boolean.parseBoolean(DefaultProperty
					.getProperty(Web.FRAMEWORK_ANONYMOUS_ACCESS));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	protected void doFilterInteral(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String uri = hreq.getRequestURI();

		if (ANONYMOUS_ACCESS){
			HttpSession session = hreq.getSession();
			IWebUser user = (IWebUser) session
					.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
			if (user == null) {
				AnonymousUser anonymousUser = new AnonymousUser();
				anonymousUser.setId(Sequence.getSequence());
				anonymousUser.setName("匿名用户");
				anonymousUser.setLoginName("anonymous");
				IWebUser webUser = new WebUser(anonymousUser);
				session.setAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER, webUser);
				session.setAttribute(Web.SESSION_ATTRIBUTE_USER, webUser);
				session.setAttribute(Web.SESSION_ATTRIBUTE_ONLINEUSER, new OnlineUserBindingListener(webUser));
			}
			chain.doFilter(request, response);
			return;
		} else if (isExcludeURI(uri)) {
			chain.doFilter(request, response);
			return;
		} else {
			HttpSession session = hreq.getSession();

			if (isForegroundURI(uri)) {
				IWebUser user = (IWebUser) session
						.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
				// 检查URI权限
				if (user != null) {
					chain.doFilter(request, response);
					return;
				}
				LOG.warn(uri);
				resp.sendRedirect(hreq.getContextPath() + "/security/timeOut.jsp");
			} else {
				if (uri.indexOf("/system/") >= 0 && uri.indexOf("/control/") == -1){
					IWebUser user = (IWebUser) session
							.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
					// 检查URI权限
					if (user != null && user.isAdmin()) {
						chain.doFilter(request, response);
						return;
					}
				}
				if (ACCESS_ADMIN) {
					IWebUser user = (IWebUser) session
							.getAttribute(Web.SESSION_ATTRIBUTE_USER);
					if (user != null) {
						chain.doFilter(request, response);
						return;
					} else {
						LOG.warn(uri);
						resp.sendRedirect(hreq.getContextPath() + "/security/timeOut.jsp");
						return;
					}
				}
				LOG.warn(uri);
				resp.sendRedirect(hreq.getContextPath() + "/security/denied.jsp");
			}
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) request;
		String wwwServer = Environment.getInstance().getServerName();
		int wwwPort = Environment.getInstance().getServerPort();
		if (StringUtil.isBlank(wwwServer)){
			Environment.getInstance().setServerName(hreq.getLocalAddr());
		}
		if (wwwPort==-1){
			Environment.getInstance().setServerPort(hreq.getServerPort());
		}
		// 获取参数前需设置编码
		request.setCharacterEncoding(Environment.getInstance().getEncoding());
		
		doFilterInteral(request, response, chain);
	}

	/**
	 * 是否不作检验的URI
	 * 
	 * @param uri
	 * @return 是返回true,否则返回false
	 */
	private boolean isExcludeURI(String uri) {
		return uri.indexOf("help") >= 0 
				|| uri.indexOf("index.jsp") >= 0 
				|| uri.indexOf("login.jsp") >= 0
				|| uri.indexOf("login.action") >= 0
				|| uri.indexOf("logout.jsp") >= 0
				|| (uri.indexOf("timeOut.jsp") >= 0)
				|| (uri.indexOf("login_error.jsp") >= 0)
				|| (uri.indexOf("denied.jsp") >= 0)				
				|| uri.equals("") || uri.equals(Environment.getInstance().getContextPath()+"/")
				|| (ACCESS_ADMIN && uri.endsWith("/admin/login.jsp"))
				|| uri.endsWith(".ico")
				|| (customfilter!=null?customfilter.isExcludeURI(uri):false);
	}

	/**
	 * 是否前台URI
	 * 
	 * @param uri
	 * @return 是返回true,否则返回false
	 */
	private boolean isForegroundURI(String uri) {
		// 检查以"/portal"或"/mobile"开始的URI(*.action、*.jsp、*.html);
		return uri.indexOf("/portal/") >= 0 || uri.indexOf("/mobile/") >= 0 || (customfilter!=null?customfilter.isForegroundURI(uri):false);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		if (customfilter!=null){
			customfilter.init(filterConfig);
		}
	}

	public void destroy() {
		if (customfilter!=null){
			customfilter.destroy();
		}
	}

}
