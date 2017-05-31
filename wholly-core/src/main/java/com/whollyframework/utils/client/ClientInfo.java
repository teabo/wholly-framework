package com.whollyframework.utils.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.whollyframework.util.client.useragent.UserAgent;

/**
 * 返回客户端信息工具类 by chris
 */
public class ClientInfo {

	public static final String H_USER_AGENT = "User-Agent";
	public static final String C_TYPE_UNKNOW = "未知";

	private String info = "";
	private String explorerVer = "未知";
	private UserAgent userAgent;
	private HttpServletRequest request = null;

	/**
	 * 构造函数 参数：String request.getHeader("user-agent")
	 * 
	 * 操作系统： Win7 : Windows NT 6.1 WinXP : Windows NT 5.1
	 */
	public ClientInfo(HttpServletRequest request) {
		this(request, request.getHeader(H_USER_AGENT));
	}

	public ClientInfo(HttpServletRequest request, String info) {
		this(info);
		this.request = request;
	}

	public ClientInfo(String info) {
		this.info = info;
		userAgent = new UserAgent(info);
	}

	/**
	 * 获取核心浏览器名称
	 */
	public String getExplorerName() {
		StringBuffer str = new StringBuffer(20);
		try{
			Pattern pattern = Pattern.compile("([\\d]+)");
			str.append(userAgent.getBrowser().getName());
			Matcher matcher = pattern.matcher(str);
			String ver = getExplorerVer();
			if (matcher.find()) {
				int index = explorerVer.indexOf(".");
				if (index!=-1){
					str.append(ver.substring(index));
				}
			} else {
				str.append(" ").append(ver);
			}
			return str.toString();
		}catch(Exception e){
			return C_TYPE_UNKNOW;
		}
		
	}

	/**
	 * 获取核心浏览器版本
	 */
	public String getExplorerVer() {
		try{
			explorerVer = userAgent.getBrowser().getVersion(this.info).getVersion();
		}catch(Exception e){
		}
		return explorerVer;
	}

	/**
	 * 获取浏览器插件名称（例如：遨游、世界之窗等）
	 */
	public String getExplorerPlug() {
		String str = "无";
		if (info.indexOf("Maxthon") != -1) {
			str = "Maxthon浏览器"; // 遨游
		} else if (info.indexOf("360EE") != -1) {
			str = "360浏览器"; // 360
		} else if (info.indexOf("360SE") != -1) {
			str = "360浏览器"; // 360
		} else if (info.indexOf("TheWorld") != -1) {
			str = "TheWorld浏览器";
		}
		return str;
	}

	public String getAgentExplorer() {
		String ExplorerPlug = getExplorerPlug();
		StringBuffer str = new StringBuffer(20);
		if (!"无".equals(ExplorerPlug)) {
			str.append(ExplorerPlug).append("(").append(getExplorerName()).append("内核)");
		} else {
			str.append(getExplorerName()).append("浏览器");
		}
		return str.toString();
	}

	/**
	 * 获取操作系统名称
	 */
	public String getOSName() {
		String str = C_TYPE_UNKNOW;
		try{
			str = userAgent.getOperatingSystem().getName();
		}catch(Exception e){
		}
		return str;
	}

	public String getAgentOSystem() {
		return getOSName();
	}

	public String getRemoteIP() {
		if (request != null) {
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			if ("0:0:0:0:0:0:0:1".equals(ip)) {
				ip = "127.0.0.1";
			}
			if (request.getSession() != null)
				request.getSession().setAttribute("__client-ip", ip);
			return ip;
		}
		return "";
	}

	public String getRemoteHost() {
		return request.getRemoteHost();
	}

	public int getRemotePort() {
		return request.getRemotePort();
	}

	public String getRemoteUser() {
		return request.getRemoteUser();
	}

	public static void main(String[] args) {
//		 // IE
//		 String Agent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET4.0C; .NET4.0E)";
//		 //360EE
//		 String Agent = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.202 Safari/535.1 360EE";
//		 //360SE
//		 String Agent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET4.0C; .NET4.0E; 360SE)";
//		 //Maxthon
//		 String Agent = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.12 (KHTML, like Gecko) Maxthon/3.3.6.2000 Chrome/18.0.966.0 Safari/535.12";
//		 //Firefox
//		 String Agent = "Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko/20100101 Firefox/11.0";
//		 //Opera
//		 String Agent =  "Opera/9.80 (Windows NT 5.1; U; Edition IBIS; zh-cn) Presto/2.10.229 Version/11.62";
//		//Opera
//		 String Agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36 OPR/22.0.1471.70";
		 //Safari
		 String Agent = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/534.52.7 (KHTML, like Gecko) Version/5.1.2 Safari/534.52.7";
//      // TheWorld
//		String Agent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET4.0C; .NET4.0E; TheWorld)";
		ClientInfo clientInfo = new ClientInfo(Agent);
		System.out.println("ExplorerName -> " + clientInfo.getExplorerName());
		System.out.println("ExplorerVar -> " + clientInfo.getExplorerVer());
		System.out.println("ExplorerPlug -> " + clientInfo.getExplorerPlug());
		System.out.println("OSName -> " + clientInfo.getOSName());
		System.out.println("Agentexplorer -> " + clientInfo.getAgentExplorer());
		System.out.println("Agentsystem -> " + clientInfo.getAgentOSystem());

	}
}
