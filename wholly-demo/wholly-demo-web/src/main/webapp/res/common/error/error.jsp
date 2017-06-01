<%@page import="com.whollyframework.utils.Dispatcher"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ include file="/res/common/tags.jsp"%>
<%
String contextPath = request.getContextPath();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Error</title>
<%@include file="/res/common/index.jsp"%>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0"
	cellspacing="0">
	<tr>
		<td align="center">
		<table width="419" height="226" border="0" cellpadding="0"
			cellspacing="0" style="margin-top: 0px;">
			<tr>
				<td align="center"
					background="<o:url skin="false" value='/res/images/main/error.jpg'/>">
				<table width="388" height="194" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<%
            String notice = (String)request.getAttribute("notice");
            if (notice !=null){
            	%>
						<td align="center" style="color: red; font-size: 14;"><img
							src="<o:url skin="false" value='/res/images/main/error_b.gif'/>" width="31"
							height="31">&nbsp;&nbsp;警告:<%=notice %> <br />
						<a href="javascript:doRefresh()">刷新</a></td>

						<%
            }else if (exception != null) {
            	exception.printStackTrace();
            %>
						<td align="center" style="color: red; font-size: 14;"><img
							src="<o:url skin="false" value='/res/images/main/error_b.gif'/>" width="31"
							height="31">&nbsp;&nbsp;错误:<%=exception.getMessage() %><br />
						<br />
						<a href="#" onClick="history.back(-1)">返回</a></td>
						<%
            } else {
              %>
						<td align="center" style="color: red; font-size: 14;"><img
							src="<o:url skin="false" value='/res/images/main/error_b.gif'/>" width="31"
							height="31">&nbsp;&nbsp;错误: 
						<c:if test="${hasFieldErrors }">
							<c:forEach var="item" items="${fieldErrors}" varStatus="index">
								*${item.value }&nbsp;&nbsp;
							</c:forEach>
						</c:if>
						<br />
						<a href="javascript:doRefresh()">刷新</a></td>
						<%
            }
              %>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<center></center>
<%
 String url = new Dispatcher().getDispatchURL(contextPath+"/control/index.jsp",request,response);
%>
</body>
<script type="text/javascript">
top.window.location.replace("<%=url%>");
</script>
</html>