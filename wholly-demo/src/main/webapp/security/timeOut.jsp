<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/res/common/tags.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
  String contextPath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录过期</title>
</head>
<script >
//has parent window 
var typeflage = typeof(dialogArguments);
 var url = parent.location;
 var timeout ;
 var flag=10;
 var flag2 =1;
 var type1 =1;
   if(url!=null&&url!="")
{
  if(typeof(dialogArguments)!='undefined'){
   type1 =2;
  }
  else{
   var object1 = window.parent;
   window.top.location='login_error.jsp';
   
 }
 }else{
 window.location='login_error.jsp';
 }
  
//alert(temp);
</script>
<body align="center">
<table valign="center">
<tr><td style="color: red;">
你的登录已经过期，请重新登录！</td></tr>
</table>
</body>
</html>