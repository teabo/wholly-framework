<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp" %>
<% 
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户列表</title>
<%@include file="/res/common/index.jsp"%>
<script src="${pageContext.request.contextPath}/res/js/common.js"></script>
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
			fitColumns:  true,
			rownumbers:true ,
			columns : [ [ {
				field : "check",
				title : "check",
				width : 200,
				checkbox:true
			},{
				field : "loginName",
				title : "登录名",
				width : 150
			},{
				field : "name",
				title : "姓名",
				width : 100
			},{
				field : "aliasName",
				title : "登录别名",
				width : 150
			},{
				field : "certType",
				title : "证件类型",
				width : 150,
				formatter: function(value, row, index){
					if (value=="00"){
						return "身份证";
					} else if (value=="01"){
						return "警官证";
					} else if (value=="02"){
						return "军官证";
					}
				}
			},{
				field : "certId",
				title : "证件号码",
				width : 100
			},{
				field : "orgId",
				title : "所属部门",
				width : 150
			},{
				field : "status",
				title : "状态",
				width : 50,
				hidden:true,
				formatter: function(value,row,index){
					if (value==1){
						return "启用";
					} 
					if(value==2) {
						return "离职";
					}
					if(value==0){
						return "停用";
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
		
		searchButtonBinds('jsonList.action'); //绑定查询按钮
		addButtonBinds('create.action?app_id=${param.app_id }'); //绑定新增按钮
		editButtonBinds('edit.action'); //绑定编辑按钮
		delButtonBinds('deleteAjax.action');  //绑定删除按钮
		
		doSearch();
		$('#superuser_list').css({height:$(window).height()});
	});
	function resetPassword() {
		var rows = $('#datas-grid').datagrid("getSelections");	//获取你选择的所有行
		if (rows === null || rows.length<=0) {
			jQuery.messager.alert("提示","至少选中一行记录",'warning');
			jQuery.messager.progress("close");
		} else {
			jQuery.messager.confirm('提示', "确定重置用户密码？", function(r) {
				//jQuery.messager.progress("close");
				if (r) {
					 //循环所选的行
					var QString = "";
				    for(var i =0;i<rows.length;i++){    
				    	if (i>0){
				    		QString += "&";
				    	}
				    	QString += "_selects="+rows[i].id;
				    }
				    jQuery.ajax({
				    	type: "POST",
				        url: "resetPassword.action",
				        data: QString,
				        dataType:"json",
				        async:false,
				    	success: function(data){
				    		if (data.code > 0) {
								message(data.msg,'ok');
							} else {
								message(data.msg,'no');
							}
				    	},
				    	error:function(textStatus){
				    	}
				        }
				    );
				    
					//window.location=window.location;
				}	
			});

		}
    }
	function lockOrUnlock(state) {
			var rows = $('#datas-grid').datagrid("getSelections");	//获取你选择的所有行
			if (rows === null || rows.length<=0) {
				jQuery.messager.alert("提示","至少选中一行记录",'warning');
				jQuery.messager.progress("close");
			} else {
				
				var msg;
				var url="";
				if(state==0){
			    	msg="确定停用选择的记录吗?";
			    	url="dolock.action";
			    }
				if(state==1){
					msg="确定启用选择的记录吗?";
					url="unlock.action";
				}
				jQuery.messager.confirm('提示', msg, function(r) {
					//jQuery.messager.progress("close");
					if (r) {
						 //循环所选的行
						var QString = "";
					    for(var i =0;i<rows.length;i++){    
					    	if (i>0){
					    		QString += "&";
					    	}
					    	QString += "_selects="+rows[i].id;
					    }
					    jQuery.ajax({
					    	type: "POST",
					        url: url,
					        data: QString,
					        dataType:"json",
					        async:false,
					    	success: function(data){
					    		if (data.code > 0) {
									message('操作成功!','ok');
								} else {
									message('操作失败!','no');
								}
					    	},
					    	error:function(textStatus){
					    	}
					        }
					    );
					    
						window.location=window.location;
					}	
				});
	
			}
	}
	function resetAll(){
		document.getElementsByName("sm_loginName")[0].value="";
		document.getElementsByName("sm_username")[0].value="";
		document.getElementsByName("sm_cert_Id")[0].value="";
		document.getElementsByName("s_org_id")[0].value="";
		document.getElementsByName("sm_police_no")[0].value="";
	}
			
</script>
</head>
<body id="superuser_list">
<div id="container-wrapper">
<form id="formList" name="formList" method="post">
    <div id="tools-bar">
	    <div class="tab_form">
		    <table style="width:90%;margin:0 auto;">
			    <tr>
			    	<td style="text-align:right;"><b class="tool_box_b">登录名：</b></td>
			    	<td><input type="text" class="tab_form_txt" name="sm_loginName"/></td>
			    	<td style="text-align:right;"><b class="tool_box_b">所属部门：</b></td>
			    	<td><select id="orgId"  name="s_org_id" class="tab_form_select"></select></td>
			    </tr>
			    <tr>
			    	<td style="text-align:right;"><b class="tool_box_b">姓名：</b></td>
			    	<td><input type="text" class="tab_form_txt" name="sm_username" /></td>
			    	<td style="text-align:right;"><b class="tool_box_b">证件号码：</b></td>
			    	<td><input type="text" class="tab_form_txt" name="sm_cert_Id" /></td>
			    </tr>
			    <tr>
					<td colspan="4" style="text-align:center;">
						<input type="button" class="btn" value="查&nbsp;&nbsp;询" id="search"/>
						<input type="button" class="btn" value="重&nbsp;&nbsp;置" id="resetAll" onclick="resetAll();"/>
					</td>
			    </tr>
		    </table>
			<input type="hidden" name="app_id" reset="false" value="${param.app_id}"/>
	    </div>
	    <div id="operate" class="tool_operate">
			<input type="button" id="btns-add" class="add" value="新增" />
			<input type="button" id="btns-update" class="edit" value="编辑"/>
			<input type="button" id="btns-del" class="del" value="删除"/>
			<input type="button" id="bts_xx" class="add" value="用户角色管理" onclick="BindRole()"/>
			<input  id="btnlock" name='button_act'  type="button" value="停用"   onclick="javascript:lockOrUnlock(0);" class="pause"/>
			<input  id="btnUnlock" name='button_act'  type="button" value="启用"   onclick="javascript:lockOrUnlock(1);" class="able"/>
			<input type="button" class="modify_pwd" value="重置密码" onclick="resetPassword();"/>
			<!-- <input type="button" id="usepwd" class="modify_pwd" onclick="javascript:changePwd(); " value="修改密码"/> -->
			<input type="button" id="btns-update" class="quit" value="导出" onclick="Save_Excel()" />
		</div>
    </div>
</form>
<table id="datas-grid"></table>

</div>
<script type="text/javascript">
function mycombobox(params){
	//var select=document.getElementById(params.selectId);
	var select=jQuery("#"+params.selectId);
	jQuery.ajax({
        url: params.url,
        type: "post",
        async: false,  // true 为异步，false 为同步
        dataType: "json",
        success: function(data){
        	jQuery("<option />").attr("value","").html("请选择").appendTo(select);
        	jQuery.each(data,function(i,n){
        		if(n[params.valueField]==params.defaut)
				 {
        			jQuery("<option />").attr("value",n[params.valueField]).html(n[params.textField]).attr("selected","selected").appendTo(select);
				 }
        		else{
        			jQuery("<option />").attr("value",n[params.valueField]).html(n[params.textField]).appendTo(select);
        		}
			});
       }
	});
}
jQuery(document).ready(function(){
	
	mycombobox({ 
		selectId:"orgId",
	    url:'orgSelect.action',    
	    valueField:'id',
	    textField:'name'
	});
 });
 function changePwd(){
	  $.Dialog.show({
			width : 900,
			height : 500,
			title : "详细信息",
			url : "<%=request.getContextPath()%>/control/exchange/user/userPwd.action",
			showButton : false
		});
 }
 function openBindRole(userId){
	 $.Dialog.show({
			width : 900,
			height : 500,
			title : "详细信息",
			url : "<%=request.getContextPath()%>/control/system/user/userRole.action?userId="+userId,
			showButton : false
	  });
 }

 function BindRole(){
		var rows = $('#datas-grid').datagrid("getSelections");	//获取你选择的所有行
		if (rows == null || rows.length<=0) {
			$.messager.alert("提示","请选择记录",'warning');
		}else if(rows.length>1){
			$.messager.alert("提示","请选择一个用户！",'warning');
		}else {
			openBindRole(rows[0].id);
		}
}
</script>
</body>
</html>
