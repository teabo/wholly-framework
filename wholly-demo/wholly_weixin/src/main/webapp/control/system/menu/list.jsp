<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>菜单列表</title>
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
	$(document).ready(function() {
		$("#datas-grid").datagrid({
			fit : true,
			columns : [ [ {
				field : "check",
				title : "check",
				width : 200,
				checkbox:true
			},{
				field : "iconUrl",
				title : "图标",
				width : 30,
				align : 'center',
				formatter: function(value,row,index){
						//alert(row.iconUrl);
						if(value != undefined){
							return "<img src='"+value+"'></img>";							
						}else{
							return "";
						}
					}
			},{
				field : "name",
				title : "菜单名称",
				width : 100
			},{
				field : "parentmenu_name",
				title : "上级菜单",
				width : 100
			},{
				field : "type_name",
				title : "菜单类型",
				width : 100
			},{
				field : "menuUrlWithParams",
				title : "菜单完全路径",
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
		
		searchButtonBinds('<%=contextPath%>/control/system/menu/jsonList.action'); //绑定查询按钮
		addButtonBinds('<%=contextPath%>/control/system/menu/create.action'); //绑定新增按钮
		editButtonBinds('<%=contextPath%>/control/system/menu/edit.action'); //绑定编辑按钮
		delButtonBinds('<%=contextPath%>/control/system/menu/deleteAjax.action');  //绑定删除按钮
		
		$('#superuser_list').css({height:$(window).height()});
		doSearch();
	});
	
</script>
</head>
<body id="superuser_list">
<div id="container-wrapper">
<form id="formList" name="formList" method="post">

	<div id="tools-bar">
	    <div class="tab_form">
		    <b class="tool_box_b">菜单名称：</b>
		    <input type="text" class="tab_form_txt" name="sm_name" id="sm_name"/>
		    <input type="button" class="btn" value="查&nbsp;&nbsp;询" id="search"/>
			<input type="button" class="btn" value="重&nbsp;&nbsp;置" id="resetAll" onclick="resetAll();"/>
	    </div>
	    <div id="operate" class="tool_operate">
			<input type="button" id="btns-add" class="add" value="新增" >
			<input type="button" id="btns-update" class="edit" value="编辑">
			<input type="button" id="btns-del" class="del" value="删除">
			<input type="button" id="btns-update" class="quit" value="导出" onclick="Save_Excel()" />
		</div>
    </div>
</form>
<table id="datas-grid"></table>

</div>

</body>
</html>
