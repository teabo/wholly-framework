<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp" %>
<% 
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>聊天记录列表</title>
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
				field : "fromUserName",
				title : "发送用户",align:'center',
				width : 130
			}, {
				field : "toUserName",
				title : "接收用户",align:'center',
				width : 130
			},{
				field : "msgType",
				title : "信息类型",align:'center',
				width : 130,
				formatter: function(value, row, index){
					//(1：text 文本消息、2：image 图片消息、3：voice 语音消息、4：video 视频消息、5：music 音乐消息、6：news 图文消息)
					if (value=='text'){
						return "文本消息";
					} else if (value=='image'){
						return "图片消息";
					} else if (value=='voice'){
						return "语音消息";
					} else if (value=='video'){
						return "视频消息";
					} else if (value=='music'){
						return "音乐消息";
					} else if (value=='news'){
						return "图文消息";
					}
				}
			}, {
				field : "createTime",
				title : "创建时间",align:'center',
				width : 130
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
		
		searchButtonBinds('jsonList.action'); //绑定查询按钮
		editButtonBinds('edit.action'); //绑定编辑按钮
		delButtonBinds('deleteAjax.action');  //绑定删除按钮
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
		    <b class="tool_box_b">发送用户：</b>
		    <input type="text" class="tab_form_txt" name="sm_fromUserName" id="sm_fromUserName"/>
		     <b class="tool_box_b">接收用户：</b>
		    <input type="text" class="tab_form_txt" name="sm_toUserName" id="sm_toUserName"/>
		    <input type="button" class="btn" value="查&nbsp;&nbsp;询" id="search"/>
			<input type="button" class="btn" value="重&nbsp;&nbsp;置" id="resetAll" onclick="resetAll();"/>
	    </div>
	    <div id="operate" class="tool_operate">
			<input type="button" id="btns-update" class="edit" value="查看">
			<input type="button" id="btns-del" class="del" value="删除">
			<!-- <input type="button" id="btns-update" class="quit" value="导出" onclick="Save_Excel()" /> -->
		</div>
    </div>
</form>
<table id="datas-grid"></table>
</div>

</body>
</html>
