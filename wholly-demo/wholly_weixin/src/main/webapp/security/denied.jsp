<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/res/common/tags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
   String ctxpath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript">
function doback() {
	history.back();
}
function docontinue() {
	top.window.location.replace("../");
}
function doclose() {
	parent.close();
}

var timeout =window.setInterval("displayTime()",1000);
var flag=3;
var objectmp;
function displayTime() {
	document.getElementById('disid').innerHTML=flag;
	if(flag<=1){
	  window.clearInterval(timeout);
	  top.window.location.replace("../");
	}
	flag--;
}
</script>
<style>
.frm1 {
	background-image: url('<o:url skin="false" value="/res/images/main/timeout_button.gif" />');
	height:28px;
	line-height: 28px;
	border: 0px;
	width:95px;
	font-size:16px;
	font-family: Arial;
	text-align: center;
	color: #535355;
	cursor:hand;
	font-weight:bold;
}

</style>
<title>拒绝访问</title>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<div align="center">
	<table width="100%" border="0" cellspacing="0"  cellpadding="0"  style="width:100%;height:15px;line-height:15px;background-repeat:repeat-x; background: url('<o:url skin="false" value="/res/images/main/timeout_back.gif" />'); background-position:top;">
		<tr>
			<td></td>
		</tr>
	</table>

</div>
<div align="center" style="align:center; margin-top: 100px;">

<table width="50%" style="margin:0px; padding:0px;"  cellspacing="0" cellpadding="0">
	<tr>
		<td bgcolor="#FFFFFF" valign="middle" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="37%" align="center" style="height:200px" rowspan="4"><img src="<o:url skin="false" value='/res/images/main/timeout.gif'/>" /></td>
				</tr>
				<tr>
					<td style="font-size:20px; font-family: Arial, '黑体'; font-weight: bold;">拒绝访问</td>
				</tr>
				<tr>
					<td width="63%" valign="middle" style="color: #5A5A5A; font-size:14px;font-family: Arial, Vendera, '黑体';"> 
						 <font color="#FF0000">提醒: </font>您没有访问此页面的权限！系统将在数秒内返回到登录页面<p align="center"><span style="color:#FF0000; font-size:26px;" id="disid">3</span>&nbsp;<i>秒</i></p>
					</td>
				</tr>

				<tr>
					<td style="position:relative;right:-60px" height="30">
   						<input type="button" accesskey="c" onClick="docontinue()" value="登录" class="frm1">
   						<input type="button" name="Submit3" accesskey="x" onClick="doclose()" value="关闭" class="frm1">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>
</body>
</html>
