<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>重置菜单</title>
<%@include file="/res/common/index.jsp"%>
<%
	String contextPath = request.getContextPath();
%>
<script src="<%=contextPath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=contextPath%>/res/js/dg_js.js"></script>
<style>
<!--
#container-wrapper {
	width: 100%;
	height: 100%;
	padding: 0;
	margin: 0;
}
-->
</style>
<script type="text/javascript">
window.onload=function(){
	var result=<%=request.getAttribute("result")%>;
	if(result!=null)
	{
		if(result==true){
			alert("重置菜单成功");
		}else{
			alert("重置菜单失败，请检查SQL脚本或者SQL脚本的文件名。");
		}
	}
};
</script>
</head>
<body id="superuser_list">

</body>
</html>
