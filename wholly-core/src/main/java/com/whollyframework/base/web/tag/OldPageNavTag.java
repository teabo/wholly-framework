package com.whollyframework.base.web.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.whollyframework.base.model.DataPackage;
import com.whollyframework.utils.Dispatcher;

/**
 * The page tag for page navigation in list page.
 */
public class OldPageNavTag extends TagSupport {

	private static final long serialVersionUID = 6338111746579488137L;

	/**
	 * @uml.property name="dpName"
	 */
	private String dpName;
	/**
	 * @uml.property name="css"
	 */
	private String css;

	/**
	 * @param datapackage
	 * @uml.property name="dpName"
	 */
	public void setDpName(String dpName) {
		this.dpName = dpName;
	}

	/**
	 * @return the css
	 * @uml.property name="css"
	 */
	public String getCss() {
		return css;
	}

	/**
	 * @param css
	 *            the css to set
	 * @uml.property name="css"
	 */
	public void setCss(String css) {
		this.css = css;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		DataPackage<?> bean = (DataPackage<?>) pageContext.getRequest().getAttribute(
				dpName);
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String contextPath = request.getContextPath();
		String baseUrl = new Dispatcher().getDispatchURL(contextPath + "/portal/dispatch/", request, pageContext
				.getResponse());
		int currentPage = 0;
		int pageCount = 0;
		if (bean != null) {
			currentPage = bean.getPageNo();
			pageCount = bean.getPageCount();
		}

		StringBuffer html = new StringBuffer();

		html.append("&nbsp;&nbsp;共&nbsp;").append(pageCount).append("&nbsp;页/");
		html.append("共&nbsp;").append(bean.getRowCount()).append("&nbsp;记录&nbsp;&nbsp;");
		
		html.append("当前第&nbsp;").append(currentPage).append("&nbsp;页&nbsp;&nbsp;");
		html.append("每页&nbsp;").append(bean.linesPerPage).append("&nbsp;条记录&nbsp;&nbsp;");
		html.append("<img src='"+baseUrl+"resource/imgv2/front/main/act_seperate.gif'/>&nbsp;");
		if (currentPage > 1) {
			html.append("<a class=" + css
					+ " href='javascript:showFirstPage()'><img src='"+baseUrl+"resource/imgv2/front/main/pg_first.gif' alt='首页'></a>&nbsp;");

			html.append("<a class="
					+ css
					+ " href='javascript:showPreviousPage()'><img src='"+baseUrl+"resource/imgv2/front/main/pg_previous.gif' alt='上页'></a>&nbsp;");
		}else{
			html.append("<img src='"+baseUrl+"resource/imgv2/front/main/pg_first_d.gif' alt='首页'>&nbsp;");
			html.append("<img src='"+baseUrl+"resource/imgv2/front/main/pg_previous_d.gif' alt='上页'>&nbsp;");
		}
		html.append("<img src='"+baseUrl+"resource/imgv2/front/main/act_seperate.gif'/>&nbsp;");
		if (currentPage < pageCount) {
			html.append("<a href='javascript:showNextPage()'><img src='"+baseUrl+"resource/imgv2/front/main/pg_next.gif' alt='下页'></a>&nbsp;");
			html.append("<a href='javascript:showLastPage()'><img src='"+baseUrl+"resource/imgv2/front/main/pg_last.gif' alt='尾页'></a>&nbsp;");
		}else{
			html.append("<img src='"+baseUrl+"resource/imgv2/front/main/pg_next_d.gif' alt='下页'>&nbsp;");
			html.append("<img src='"+baseUrl+"resource/imgv2/front/main/pg_last_d.gif' alt='尾页'>&nbsp;");
		}
		html.append("<img src='"+baseUrl+"resource/imgv2/front/main/act_seperate.gif'/>&nbsp;");
		if (pageCount > 1){
			html.append("第&nbsp;<input type='text' style='width:25px;height:18px;' value='"+currentPage+"' name='_jumppage' />&nbsp;页&nbsp;");
			html.append("<button type='button' onclick='javascript:jumpPage();' style='width:60px;height:20px;text-align: center;vertical-align: middle;'>跳转</button>&nbsp;");
		}

		try {
			pageContext.getOut().print(html.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return super.doEndTag();
	}
}
