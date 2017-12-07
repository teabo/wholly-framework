<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.reflect.Method"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>聊天记录信息</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/res/component/dtree/dtree.css" />
<%@include file="/res/common/index.jsp"%>
<script type="">
	var contextPath='<%=contextPath%>';
</script>

<%@include file="/res/js/document/common.jsp"%>
<script src="${pageContext.request.contextPath}/res/js/util.js"></script>
<script>

function doexit() {
	var backurl='${pageContext.request.contextPath}/control/wechat/chatHistory/index.action';
	window.location = backurl;
}
</script>

</head>
<body id="org_info" class="body-front" style="padding: 0;margin: 0;border:1px solid #DFE3E6;border-top:0px;">
<div id="container" class="front-align-top">
<form id="formItem" name="formItem" action="save" method="post"  validate="true" 
	theme="simple">
	<%@include file="/res/common/msg.jsp"%>
	<div id="contentTable" class="front-scroll-auto" style="border:0px;">
    <h1 class="title" style="font-size:15px;border-bottom:1px solid #DFE3E6;">聊天记录</h1>
	<table class="mauto tab_form">
		<tr>
			<td class="field" align="right">
				<font color="red">*</font>发送用户：
			</td>
			<td id="oname_h" class="justForHelp">${content.fromUserName }</td>
			<td class="field" align="right">
				<font color="red">*</font>接收用户：
			</td>
			<td id="orgCode_h" class="justForHelp">${content.toUserName }</td>
		</tr>
		<tr >
			<td class="field" align="right"><font color="red">*</font>信息类型：</td>
			<td id="fax_h" title="上级机构" class="justForHelp">
			<!-- (1：text 文本消息、2：image 图片消息、3：voice 语音消息、4：video 视频消息、5：music 音乐消息、6：news 图文消息) -->
				${content.msgType eq "text"?"文本消息":"" }
				${content.msgType eq "image"?"图片消息":"" }
				${content.msgType eq "voice"?"语音消息":"" }
				${content.msgType eq "video"?"视频消息":"" }
				${content.msgType eq "music"?"音乐消息":"" }
				${content.msgType eq "news"?"图文消息":"" }
			</td>
			<td class="field" align="right"><font color="red">*</font>创建时间：</td>
			<td id="cert_id_h" colspan="1" >
				${content.createTime }
			</td>
		</tr>
		<tr >
			<td class="field" align="right">信息内容XML：</td>
			<td id="tel_h" colspan="3" style="height:50px;">
				<xmp>${content.msgXml }</xmp>
			</td>
		</tr>
	</table>
	<div id="activityTable" class="activity_box">	
		<input type="button"  id="btn_return" onclick="doexit()" class="return" value="返&nbsp;&nbsp;回" />
	</div>
	</div>
</form>
</div>
</body>
<script>

	jQuery(document).ready(function() {
		adjustDocumentLayout();
		toggleButton("button_act");
	});
	
	window.onresize=function(){
		adjustDocumentLayout();
	};
</script>
</html>