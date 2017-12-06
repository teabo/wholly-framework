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
<title>机构信息</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/res/component/dtree/dtree.css" />
<%@include file="/res/common/index.jsp"%>
<script type="">
	var contextPath='<%=contextPath%>';
</script>

<%@include file="/res/js/document/common.jsp"%>
<script src="${pageContext.request.contextPath}/res/js/util.js"></script>
<script>

	//检查用户中名称中是否包含()字符
	function checkStringAsDefault() {
		var rtn = true;
		var msg = "";
		if (document.getElementsByName("name")[0].value==''){
			msg = "机构名称不能为空！";
			rtn = false;
		}
		if(checkStr(document.getElementsByName("name")[0].value)){
			msg = "机构名称中含有非法字符！";
			rtn = false;
		} 
		if(isOverLength("name",40)){
			msg = "名称字段不能超过40位！";
			rtn = false;
		}
		if (!validateInteger(document.getElementsByName("sortId")[0].value) || document.getElementsByName("sortId")[0].value>99999){
			msg += "   序号值只能为整数(最大长度为5位)！";
			rtn = false;	
		}
		if (""==document.getElementsByName("orgCode")[0].value){
			msg = "机构代码不能为空！";
			rtn = false;
		}
		
		if(""!=document.getElementsByName("telephone")[0].value){
			mobile= document.getElementsByName("telephone")[0].value;
		    var myreg = /^([1-9]\d{10})$/;
		    var mytel = /^(\(\d{3,4}\)|\d{3,4}-)?\d{7,8}$/;
		    if(!myreg.test(mobile) && !mytel.test(mobile))
		    {
		        msg = "请输入11位手机号码!或固话“XXXX-XXXXXXX，XXXX-XXXXXXXX，XXX-XXXXXXX，XXX-XXXXXXXX，XXXXXXX，XXXXXXXX”";
				rtn = false;
		    }
		}
		if (msg.length>0)
			showMessage('error',msg);
		return rtn;
	}
	function checkStr(str){
		var pat=new RegExp("[^a-zA-Z0-9\_\u4e00-\u9fa5]","i");
	    if(pat.test(str))   
	    {   
	        return true;   
	    } 
	}
</script>
<script>

function getAllORG(unitId){
		var para="";
		if(unitId){
			para=para+"?unitId="+unitId;
		}
		jQuery.ajax({
            url: contextPath+"/control/system/org/getAllORG.action"+para,
            type: "post",
            async: "true",
            dataType: "json",
            success: function(data){
            	var parentId = jQuery("#parentId");
           		var ops = "";
             	jQuery.each(data,function(key,val){
             		if("${content.parentId}"==key ){
             			ops = ops + "<option value='"+key+"' selected = 'selected'>"+val+"</option>";
             		}else{
             			ops = ops + "<option value='"+key+"'>"+val+"</option>";              			
             		} 
             	});
             	parentId.html(ops);
           }
  		 });
	}

function dosubmit(url) {
	var isValid = true;
	if (window.checkStringAsDefault) {
		isValid = checkStringAsDefault();
	}
	if (isValid){
		var formItem = document.getElementById("formItem");
		if (url == "saveAndNew") {
			formItem.action = "${pageContext.request.contextPath}/control/system/org/saveAndNew.action";
		}else{
			formItem.action="${pageContext.request.contextPath}/control/system/org/save.action";
		}
		formItem.submit();
	}
}

function doexit() {
	var backurl='${pageContext.request.contextPath}/control/system/org/list.action';
	window.location = backurl;
}
</script>

</head>
<body id="org_info" class="body-front" style="padding: 0;margin: 0;border:1px solid #DFE3E6;border-top:0px;">
<div id="container" class="front-align-top">
<form id="formItem" name="formItem" action="save" method="post"  validate="true" 
	theme="simple">
	<%-- <%@include file="../../res/common/pagebtns.jsp"%>
	<%@include file="../../res/common/msg.jsp"%>
	<%@include file="../..//res/common/page.jsp"%> --%>
	<%@include file="/res/common/msg.jsp"%>
	<div id="contentTable" class="front-scroll-auto" style="border:0px;">
    <h1 class="title" style="font-size:15px;border-bottom:1px solid #DFE3E6;">机构信息</h1>
	<table class="mauto tab_form">
		<tr>
			<td class="field" align="right">
				<font color="red">*</font>名称：
			</td>
			<td id="oname_h" title="机构名" class="justForHelp"><input
				class="tab_form_txt" theme="simple" name="name"
				id="name" value="${content.name }"/></td>
			<td class="field" align="right">
				<font color="red">*</font>代码：
			</td>
			<td id="orgCode_h" title="机构代码" class="justForHelp"><input
				class="tab_form_txt" theme="simple" name="orgCode"
				id="orgCode" value="${content.orgCode }"/></td>
		</tr>
		<tr >
			<td class="field" align="right"><font color="red">*</font>上级机构：</td>
			<td id="fax_h" title="上级机构" class="justForHelp">
				<select class="tab_form_select" id="parentId"  name="parentCode" >
						<option value="0">顶级机构</option>
				</select>
			</td>
			<td class="field" align="right"><font color="red">*</font>机构类型：</td>
			<td id="cert_id_h" colspan="1" >
			   <input type="radio" name="orgType" value="0" ${content.orgType eq "0"?"checked=\"checked\"":"" } />单位
			   <input type="radio" name="orgType" value="1" ${content.orgType eq "1"?"checked=\"checked\"":"" } />部门
			</td>
		</tr>
		<tr >
			<td class="field" align="right">电话：</td>
			<td id="tel_h" title="电话" class="justForHelp"><input
				maxlength="13" size="13" 
				class="tab_form_txt" theme="simple" name="telephone" value="${content.telephone }"/></td>
			<td class="field" align="right"><font color="red">*</font>排序：</td>
			<td id="order1_h" title="序号" class="justForHelp">
			<input class="tab_form_txt" maxlength="5" name="sortId" value="${content.sortId }"/>
			</td>
			
		</tr>
		<input type="hidden" name="id" value='${content.id }' />
		<input type="hidden"  name="istemp" value="${content.istemp eq null?1:content.istemp}"/>
	</table>
	<div id="activityTable" class="activity_box">	
		<input type="button"  id="btn_save" onclick="dosubmit('saveAndNew')" class="save_new" value="保存/新建" />
		<input type="button"  id="btn_save" onclick="dosubmit()" class="save" value="保&nbsp;&nbsp;存" />
		<input type="button"  id="btn_return" onclick="doexit()" class="return" value="返&nbsp;&nbsp;回" />
	</div>
	</div>
</form>
</div>
</body>
<script>

	jQuery(document).ready(function() {
		adjustDocumentLayout();
		getAllORG();
		toggleButton("button_act");
	});
	
	window.onresize=function(){
		adjustDocumentLayout();
	};
</script>
</html>