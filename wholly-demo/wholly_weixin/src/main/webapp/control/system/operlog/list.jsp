<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp" %>
<% 
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>管理员列表</title>
<%@include file="/res/common/index.jsp"%>
<script src="<%=request.getContextPath()%>/res/js/common.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/res/js/dg_js.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/component/dialog/jquery.teabo.dialog.js"></script>
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
     			field : "xuhao",
     			title : "序号",
     			width : 60,
     			formatter: function(value,row,index){
     				return index+1;
     			},
     			align:"center"
     		},{
				field : "userId",
				title : "用户标识",
				width : 80,
				align:"center"
			},{
				field : "userName",
				title : "用户名",
				width : 90,
				align:"center"
			},{
				field : "operateTime",
				title : "操作时间",
				width : 120,
				align:"center"
			},{
				field : "operateType",
				title : "操作类型",
				width : 60,
				formatter: function(value,row,index){
					if (value=="0"){
						return "登录";
					} else if(value=="1") {
						return "查询";
					} else if(value=="2"){
						return "新增";
					} else if(value=="3"){
						return "修改";
					}else if(value=="4"){
						return "删除";
					}
				},
				align:"center"
			},{
				field : "operateResult",
				title : "操作结果",
				width : 70,
				formatter: function(value,row,index){
					if (value=="0"){
						return "失败";
					} else if(value=="1") {
						return "成功";
					}
				},
				align:"center"
			},{
				field : "terminalId",
				title : "终端标识",
				width : 90,
				align:"center"
			},{
				field : "operateName",
				title : "功能模块名称",
				width : 230
			},{
				field : "operateCondition",
				title : "操作条件",
				width : 350
			}] ],
			striped : true,
			rownumbers : false,// 序号, 有问题
			singleSelect : true,
			toolbar : "#tools-bar",
			pagination : true
		});
		$("#container-wrapper").resize(function() {
			$("#datas-grid").datagrid("resize");
		});
		
		searchButtonBinds('<%=contextPath%>/control/system/operlog/jsonList.action'); //绑定查询按钮
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
    	<table style="width:90%;margin:0 auto;">
    		<tr>
    			<td style="text-align:right;"><b class="tool_box_b">用户名：</b></td>
    			<td><input type="text" class="tab_form_txt" name="sm_user_name" id="sm_user_name"/></td>
    			<td style="text-align:right;"><b class="tool_box_b">终端标识：</b></td>
    			<td><input type="text" class="tab_form_txt" name="sm_terminal_id" id="sm_terminal_id"/></td>
    		</tr>
    		<tr>
    			<td style="text-align:right;"><b class="tool_box_b">操作类型：</b></td>
    			<td>
			    	<select id="n_operate_type" name="n_operate_type" class="tab_form_select">
					    <option selected value="">--请选择--</option>
					    <option value="0">登录</option>
					    <option value="1">查询</option>
					    <option value="2">新增</option>
						<option value="3">修改</option>
						<option value="4">删除</option>
					</select>
    			</td>
    			<td></td>
    			<td>
			    	<input type="button" class="btn" value="查&nbsp;&nbsp;询" id="search" style="margin-left:0px;"/>
					<input type="button" class="btn" value="重&nbsp;&nbsp;置" id="resetAll" onclick="resetAll();"/>
    			</td>
    		</tr>
    	</table>
    	</div>
		
		<div id="operate" class="tool_operate">
	    	<input type="button" id="btns-update" class="quit" value="导出" onclick="Save_Excel()" />
	    </div>
    </div>
</form>
<table id="datas-grid"></table>

</div>

</body>
</html>
