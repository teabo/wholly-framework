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
<title>用户信息</title>
<%@include file="/res/common/index.jsp"%>
<%@include file="/res/js/document/common.jsp"%> 
<link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/main/main.css" />
<script>

	//检查用户中名称中是否包含()字符
	function checkStringAsDefault() {
		var oldps=document.getElementById('oldpwd').value;
		var newps=document.getElementById('newpwd1').value;
		var newps2=document.getElementById('newpwd2').value;
		if(oldps.trimAll()==""){
			showMessage('error','"旧密码不能为空"');
			return false;
		}
		
		if(newps.trimAll()==""){
			showErrorDiv('error','"输入新密码"');
			return false;
		}
		
		if(newps2.trimAll()==""){
			showErrorDiv('error','再次输入新密码');
			return false;
		}
		if(newps!=newps2){
			showErrorDiv('error','新密码  两次输入的不一致');
			return false;
		}
		
		return true;
	}
	function checkStr(str){
		var pat=new RegExp("[^a-zA-Z0-9\_\u4e00-\u9fa5]","i");
	    if(pat.test(str))   
	    {   
	        return true;   
	    } 
	}
	function dosubmit(){
		if(checkStringAsDefault()){
			jQuery.ajax({
	            cache: true,
	            type: "POST",
	            url:"${pageContext.request.contextPath}/control/system/user/changePassword.action",
	            data:jQuery('#formItem').serialize(),// 你的formid
	            async: false,
	            dataType:"json",
	            error: function(request) {
	                alert("Connection error");
	            },
	            success: function(data) {
	            	if(data.code=='0'){
	            		showMessage("notice",data.msg);
	            	}else{
	            		showMessage('error',data.msg);
	            	}
	            	
	            }
	        });
		}
		
	}
</script>

</head>
<body id="org_info" class="body-front" style="padding: 0;margin: 0;">
<div id="container" class="front-align-top">
<form id="formItem" name="formItem" action="changePassword.action" method="post"  onsubmit="javascript:return checkStringAsDefault(this);" validate="true" theme="simple">
	<%@include file="/res/common/msg.jsp"%>
	<%@include file="/res/common/page.jsp"%>
	<div id="contentTable" class="front-scroll-auto" style="border:0px;">
    <h1 class="title" style="font-size:15px;border-bottom:1px solid #DFE3E6;">用户信息</h1>
	 <input type="hidden"  name="istemp" value="${content.istemp}"></input>
      <input type="hidden"  name="userId" value="${sessionScope.USER.id}"></input>
	<table width="80%" class="mauto tab_form">
		<tr >
			<td class="field" align="right">
				<font color="red">*</font>输入旧密码：
			</td>
			<td title="输入旧密码">
			 <input type="password" class="tab_form_txt"  maxlength="20" size="20" name="oldpwd" id="oldpwd" />
			</td>
		</tr>
		<tr >
			<td  class="field" align="right">
				<font color="red">*</font>输入新密码：
			</td>
			<td  title="输入旧密码">
			 <input type="password" class="tab_form_txt" maxlength="20" size="20" name="newpwd1" id="newpwd1" />
			</td>
		</tr>
		<tr >
			<td width="20%" class="field" align="right">
				<font color="red">*</font>输入新密码：
			</td>
			<td width="30%" id="name_h" title="输入新密码">
			 <input type="password" class="tab_form_txt"  maxlength="20" size="20" name="newpwd2" id="newpwd2" />
			</td>
		</tr>		
	</table>
	<div id="activityTable" class="activity_box">
		<input  id="btnSave" name='button_act' onclick="dosubmit()" disabled="disabled" type="button" class="btn" value="保&nbsp;存"/>
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