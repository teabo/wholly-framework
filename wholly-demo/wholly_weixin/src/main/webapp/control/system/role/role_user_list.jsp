<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp" %>
<% 
	String contextPath = request.getContextPath();
%>
<s:bean name="com.sunshine.core.software.datasource.action.DataSourceHelper" id="dh" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>数据源列表</title>
<%@include file="/res/common/index.jsp"%>
<script src="<%=contextPath %>/res/js/common.js"></script>
<script type="text/javascript" src="<%=contextPath %>/res/js/dg_js.js"></script>
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
				field : "userName",
				title : "用户名",
				width : 100
			},{
				field : "loginName",
				title : "账号",
				width : 150
			},{
				field : "aliasName",
				title : "登录别名",
				width : 150
			},{
				field : "telephone",
				title : "手机",
				width : 100
			},{
				field : "station",
				title : "职务",
				width : 150
			},{
				field : "state",
				title : "状态",
				width : 50,
				formatter: function(value,row,index){
					if (value==1){
						return "启用";
					} 
					if(value==2) {
						return "<font color='red'>离职</font>";
					}
					if(value==0){
						return "<font color='red'>停用</font>";
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
		
		searchButtonBinds('jsonUserRoleList.action?type=${param.type}&roleid=${param.roleid}'); //绑定查询按钮
		
		doSearch();
		
		$('#superuser_list').css({height:$(window).height()});
	});
	
	function getSelectData(){
		var rows = $('#datas-grid').datagrid("getSelections");
		var uid = "";
		jQuery.each(rows,function(k,v){
			//alert(k+"--"+v.id);
			uid = uid + v.id+",";
		})
		return uid;
	}
	
	function unBind(){
		//alert("${param.roleid}");
		//alert(getSelectData());
		 var QString = "roleid=${param.roleid}&uids="+getSelectData();
 		jQuery.ajax({
            url: contextPath+"/control/system/role/unBindAjax.action",
            type: "post",
            data:QString,
            async: "true",  // true 为异步，false 为同步
            dataType: "json",
            success: function(data){
            	doSearch();
            	//alert(data.msg);
           }
  		 }); 
	}
	
	function bind(){
		 var QString = "roleid=${param.roleid}&uids="+getSelectData();
		 jQuery.ajax({
            url: contextPath+"/control/system/role/bindAjax.action",
            type: "post",
            data:QString,
            async: "true",  // true 为异步，false 为同步
            dataType: "json",
            success: function(data){
            	doSearch();
           }
  		 });
	}
			
</script>
</head>
<body id="superuser_list">
<div id="container-wrapper">
<form id="formList" name="formList" method="post">
    <div id="tools-bar">
	    <div class="tab_form">
		    <b class="tool_box_b">名称：</b>
		    <input type="text" class="tab_form_txt" name="sm_username" id="sm_username"/>
		    <input type="button" class="btn" value="查&nbsp;&nbsp;询" id="search"/>
			<input type="button" class="btn" value="重&nbsp;&nbsp;置" id="resetAll" onclick="resetAll();"/>
	    </div>
	    <div id="operate" class="tool_operate">
	    	<c:if test="${param.type==1 }">
				<input type="button" id="btns-unbind" onclick="unBind();" class="del" value="解除" />	    	
	    	</c:if>
	    	<c:if test="${param.type==2 }">
				<input type="button" id="btns-bind" onclick="bind();" class="add" value="绑定"/>	    	
	    	</c:if>
		</div>
    </div>
</form>
<table id="datas-grid"></table>

</div>

</body>
</html>
