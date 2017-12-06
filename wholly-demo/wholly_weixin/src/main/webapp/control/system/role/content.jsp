<%@ page contentType="text/html; charset=UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@include file="/res/common/tags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>角色信息</title>
<%@include file="/res/common/index.jsp"%>
<%@include file="/res/js/document/common.jsp"%>
<script src="<%=contextPath%>/res/js/common.js"></script>
<script type="">
		var contextPath='<%=contextPath%>';
</script>
<script>
	function save(str){
		if(!checkStringAsDefault()){
			return ;
		}
		if(str == "saveAndNew"){
			jQuery("#formItem").attr("action",contextPath+"/control/system/role/saveAndNew.action");
		}else{
			jQuery("#formItem").attr("action",contextPath+"/control/system/role/save.action");
		}
		jQuery("#formItem").submit();
		
	}
	
	function exit(){
		window.location.href=contextPath+"/control/system/role/list.action";
	}

	function changeUserList(type){
		//alert("${content.id}");
		var urlStr = contextPath+"/control/system/role/roleUserList.action?type="+type+"&roleid=${content.id}";
		jQuery("#user_iframe").attr("src",urlStr);
	}
	
	function checkStringAsDefault() {
		var rtn = true;
		var msg = "";
		if (document.getElementsByName("name")[0].value==''){
			msg = "角色名称不能为空！";
			rtn = false;
		}
		else if(!(document.getElementsByName("name")[0].value).match( /^[\u4E00-\u9FA5a-zA-Z0-9_]{1,100}$/)){
			msg = "角色名称只能为汉字 英文字母 数字 下划线组成！";
			rtn = false;
		}
		else if (!validateInteger(document.getElementsByName("sortId")[0].value) || document.getElementsByName("sortId")[0].value>9999999999){
			msg += "   序号值只能为整数(最大长度为10位)！";
			rtn = false;	
		}
		if (msg.length>0){
			showMessage('error',msg);
		}
		return rtn;
	}
		
</script>
</head>
<body id="menu_info" class="body-front" style="padding: 0;margin: 0;border:1px solid #DFE3E6;border-top:0px;">
<div id="container" class="front-align-top">
<form id="formItem" name="formItem" action="save" method="post" >
    <%@include file="/res/common/msg.jsp"%>
	<div id="contentTable" class="front-scroll-auto" style="border:0px;width:100%;height:100%;">
		<h1 class="title" style="font-size:15px;border-bottom:1px solid #DFE3E6;">角色管理</h1>
		<table width="95%" class="mauto tab_form">
		  	<input type="hidden"  name="id" value="${content.id}"/>
		  	<input type="hidden"  name="istemp" value="${content.istemp}"/>
			<tr height="35">
				<td  class="field" align="right">
					<font color="red">*</font>角色名：
				</td>
				<td  title="角色名">
					<input type="text" class="tab_form_txt"  maxlength="40" size="20" value="${content.name }" name="name" id="content_name" />
				</td>
				
				<td  class="field" align="right">
					角色说明：
				</td>
				<td  title="角色说明">
					<input type="text" maxlength="40" class="tab_form_txt" name="description" value="${content.description }" id="content_description" />
				</td>
			</tr>
			<tr height="35">
				<td  class="field" align="right">
					排序：
				</td>
				<td  colspan="3" title="排序">
					<input type="text" class="tab_form_txt"  maxlength="6" size="6" value="${content.sortId }" name="sortId" id="content_sortId" />
				</td>
			</tr>
			<c:if test="${not empty content.id}">
				<tr>
					<td colspan="4" style="padding-left: 10px;">
						<label><input type="radio" name="user_list_type" onclick="changeUserList(2);" checked="checked" value="2" style="vertical-align:middle;"/>&nbsp;未加入用户</label>
						<label><input type="radio" name="user_list_type" onclick="changeUserList(1);" value="1" style="vertical-align:middle;"/>&nbsp;已加入用户</label>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<div id="role_user_div">
							<iframe id="user_iframe" src="" style="width:100%;height:300px;border: 0px;"></iframe>
						</div>
					</td>
				</tr>
			</c:if>
		</table>
	</div>
	<div id="activityTable" class="activity_box">	
			     <c:if test="${empty content.id}"><input type="button"  id="btn_save_new" onclick="save('saveAndNew')" class="save_new" value="保存/新建"/></c:if>  
				 <input type="button"  id="btn_save" onclick="save()" class="save" value="保&nbsp;&nbsp;存"/>
				 <input type="button"  id="btn_return" onclick="exit()" class="return" value="返&nbsp;&nbsp;回"/>
	</div>
</form>
</div>

</body>
<script>
	jQuery(document).ready(function() {
		adjustDocumentLayout();
		changeUserList(2);
		toggleButton("button_act");
	});

	jQuery(window).resize(function(){
		adjustDocumentLayout();
	});
</script>
</html>