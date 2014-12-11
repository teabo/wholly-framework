package com.whollyframework.base.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.whollyframework.base.model.DataPackage;

/**
 * The page tag for page navigation in list page.
 */
public class PageNavTag extends TagSupport {

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
		int currentPage = 0;
		int pageCount = 0;
		if (bean != null) {
			currentPage = bean.getPageNo();
			pageCount = bean.getPageCount();
		}

		StringBuffer html = new StringBuffer();

		html.append("<span class='span1'>符合条件的记录共<em>");
		html.append(bean.getRowCount()).append("</em>条，每页<em>");
		html.append(bean.linesPerPage).append("</em>条，共<em>");
		html.append(pageCount).append("</em>页，当前第<em>");
		html.append(currentPage).append("</em>页.</span>");
		html.append("<span class='span2'>");
		if (currentPage > 1) {
			html.append("<input type='button' onclick='showFirstPage()' class='butt1'/>");
			html.append("<input type='button' onclick='showPreviousPage()' class='butt2'/>");
		}else{
			html.append("<input type='button' class='butt1'/>");
			html.append("<input type='button' class='butt2'/>");
		}
		if (currentPage < pageCount) {
			html.append("<input type='button' onclick='showNextPage()' class='butt3'/>");
			html.append("<input type='button' onclick='showLastPage()' class='butt4'/>");
		}else{
			html.append("<input type='button' class='butt3'/>");
			html.append("<input type='button' class='butt4'/>");
		}
		
		html.append("<select name='_lines' onchange='changeLines()'>");
		if (bean.linesPerPage==5){
			html.append("<option value='5' selected='selected'>5</option>");
		} else {
			html.append("<option value='5'>5</option>");
		}
		if (bean.linesPerPage==10){
			html.append("<option value='10' selected='selected'>10</option>");
		} else {
			html.append("<option value='10'>10</option>");
		}
		if (bean.linesPerPage==15){
			html.append("<option value='15' selected='selected'>15</option>");
		} else {
			html.append("<option value='15'>15</option>");
		}
		if (bean.linesPerPage==20){
			html.append("<option value='20' selected='selected'>20</option>");
		} else {
			html.append("<option value='20'>20</option>");
		}
		if (bean.linesPerPage==30){
			html.append("<option value='30' selected='selected'>30</option>");
		} else {
			html.append("<option value='30'>30</option>");
		}
		if (bean.linesPerPage==50){
			html.append("<option value='50' selected='selected'>50</option>");
		} else {
			html.append("<option value='50'>50</option>");
		}
		html.append("</select>");
		if (pageCount > 1){
			html.append("&nbsp;<em>跳转到</em>&nbsp;<input type='text' class='numtxt' value='"+currentPage+"' name='_jumppage' size='2' />&nbsp;<em>页</em>&nbsp;");
			html.append("<button type='button' onclick='javascript:jumpPage();' class='enter'>跳转</button>&nbsp;");
		}
		html.append("</span>");

		try {
			pageContext.getOut().print(html.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return super.doEndTag();
	}
}
