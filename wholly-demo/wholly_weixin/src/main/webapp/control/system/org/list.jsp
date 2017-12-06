<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp" %>
<% 
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>机构列表</title>
<%@include file="/res/common/index.jsp"%>
<script src="${pageContext.request.contextPath}/res/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/js/dg_js.js"></script>
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
				title : "机构名称",align:'center',
				width : 130
			}, {
				field : "orgCode",
				title : "机构代码",align:'center',
				width : 130
			},{
				field : "orgType",
				title : "机构类型",align:'center',
				width : 130,
				formatter: function(value, row, index){
					if (value==0){
						return "单位";
					} else if (value==1){
						return "部门";
					}
				}
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
		
		searchButtonBinds('${pageContext.request.contextPath}/control/system/org/jsonList.action'); //绑定查询按钮
		addButtonBinds('${pageContext.request.contextPath}/control/system/org/create.action'); //绑定新增按钮
		editButtonBinds('${pageContext.request.contextPath}/control/system/org/edit.action'); //绑定编辑按钮
		delButtonBinds('${pageContext.request.contextPath}/control/system/org/deleteAjax.action');  //绑定删除按钮
		doSearch();
		$('#superuser_list').css({height:$(window).height()});
	});
	

</script>
</head>
<body id="superuser_list">
<div id="container-wrapper">
<form id="formList" name="formList" method="post">
    <div id="tools-bar">
	    <div class="tab_form">
		    <b class="tool_box_b">机构名称：</b>
		    <input type="text" class="tab_form_txt" name="sm_name" id="sm_name"/>
		    <%-- <input type="hidden" name="s_unit_id" reset="false" value="${param.s_unit_id}"/>ssssssssss --%>
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
