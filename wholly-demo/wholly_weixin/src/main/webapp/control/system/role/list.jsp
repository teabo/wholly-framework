<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>角色列表</title>
<%@include file="/res/common/index.jsp"%>
<%
	String contextPath = request.getContextPath();
%>
<script src="<%=contextPath %>/res/js/common.js"></script>
<script type="text/javascript" src="<%=contextPath %>/res/js/dg_js.js"></script>
<script type="text/javascript" src="<%=contextPath %>/res/component/dialog/jquery.teabo.dialog.js"></script>

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
	$(document).ready(function() {
		$("#datas-grid").datagrid({
			fit : true,
			fitColumns:  true,
			rownumbers:true ,
			columns : [ [ {
				field : "check",
				title : "check",
				width : 200,
				checkbox:true
			},{
				field : "name",
				title : "角色名称",
				width : 100
			},{
				field : "userCount",
				title : "用户数",
				align : 'center',
				width : 50
			},{
				field : "author",
				title : "创建人",
				width : 100
			},{
				field : "created",
				title : "创建时间",
				width : 250,
				formatter: function(value,row,index){
					return new Date(value).format("Y 年 M 月 dd 日");
				}
			},{
				field : "description",
				title : "角色说明",
				width : 300
			}] ],
			striped : true,
			rownumbers : false,
			singleSelect : false,
			toolbar : "#tools-bar",
			pagination : true
		});
		$("#container-wrapper").resize(function() {
			$("#datas-grid").datagrid("resize");
		});
		
		searchButtonBinds('<%=contextPath%>/control/system/role/jsonList.action'); //绑定查询按钮
		addButtonBinds('<%=contextPath%>/control/system/role/create.action'); //绑定新增按钮
		editButtonBinds('<%=contextPath%>/control/system/role/edit.action'); //绑定编辑按钮
		delButtonBinds('<%=contextPath%>/control/system/role/deleteAjax.action');  //绑定删除按钮
		
		$('#superuser_list').css({height:$(window).height()});
		doSearch();
	});
	
	Date.prototype.format = function(format){
		var o = {
		"Y+" : this.getFullYear(),//year		
		"M+" : this.getMonth()+1, //month
		"d+" : this.getDate(), //day
		"h+" : this.getHours(), //hour
		"m+" : this.getMinutes(), //minute
		"s+" : this.getSeconds(), //second
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter
		"S" : this.getMilliseconds() //millisecond
		};
		if(/(y+)/.test(format)) {
			format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
			}
		for(var k in o) {
			if(new RegExp("("+ k +")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
			}
		}
		return format;
	}; 
	
	function permissionBind(){
		
		jQuery.Dialog.show({
            width : 800,
            height : 500,
            title : "权限分配" ,
            url : "<%=contextPath%>/control/system/role/rolPermissionBindIndex.action",
            showButton : false,
            ok : function(res){
				
            },
            cancel : function(){
            	
            }
 		 });
 		 
  /* 		$('#win').window({    
 		    title: "权限分配" ,
 		    minimizable:false,
 		    maximizable:false,
 		    draggable:false,
 		    modal:true   
 		});  
 		$('#win').window("maximize");  */

	}
	
	 
</script>
</head>
<body id="superuser_list">
<div id="container-wrapper">
<form id="formList" name="formList" method="post">

	<div id="tools-bar">
	    <div class="tab_form">
		    <b class="tool_box_b">角色名称：</b>
		    <input type="text" class="tab_form_txt" name="sm_name" id="sm_name"/>
		    <input type="button" class="btn" value="查&nbsp;&nbsp;询" id="search"/>
			<input type="button" class="btn" value="重&nbsp;&nbsp;置" id="resetAll" onclick="resetAll();"/>
	    </div>
	    <div id="operate" class="tool_operate">
			<input type="button" id="btns-add" class="reg" value="注册" >
			<input type="button" id="btns-update" class="edit" value="编辑">
			<input type="button" id="btns-del" class="del" value="删除">
			<input type="button" id="btns-permission" class="send_power" onclick="permissionBind();" value="授权" >
			<input type="button" id="btns-update" class="quit" value="导出" onclick="Save_Excel()" />
		</div>
		
	
    </div>
</form>
<table id="datas-grid"></table>
<%-- <div width="100%" height="100%" style="overflow: auto;visibility: hidden;" id="win"> 
	<iframe frameborder="0" id="wind_if" width="100%" src="<%=contextPath%>/control/system/role/rolPermissionBindIndex.action"></iframe>
</div> --%>
</div>

</body>
</html>
<script type="text/javascript">

	function fitLayout() {
		var container = document.getElementById("win");
		var content_if = document.getElementById("wind_if");
		var containerHeight = jQuery(window).height() - 50;
		container.style.width = "100%";
		container.style.height = containerHeight + 'px';
		content_if.style.height = (containerHeight-5) + 'px';
		container.style.visibility = "visible";
	}
	
	jQuery(document).ready(function() {
		//fitLayout();
	});
	
	jQuery(window).resize(function() {
		//fitLayout();
	});

</script>