<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>角色列表</title>
<%@include file="/res/common/index.jsp"%>
<script src="${pageContext.request.contextPath}/res/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/js/dg_js.js"></script>
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
		
		searchButtonBinds('userRoleList.action'); //绑定查询按钮
		
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
            url : "${pageContext.request.contextPath}/control/system/role/rolPermissionBindIndex.action",
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
	function doBind(value){
		if(value){
			$("#s_type").val("1");
			jQuery("#btns-add2").show();
			jQuery("#btns-add").hide();
		}else{
			$("#s_type").val("0");
			jQuery("#btns-add2").hide();
			jQuery("#btns-add").show();
		}
		doSearch();
	}
	 
</script>
</head>
<body id="superuser_list">
<div id="container-wrapper">
<form id="formList" name="formList" method="post">

	<div id="tools-bar">
	    <div class="tab_form">
	        <b class="tool_box_b">快捷查询:</b>
	         <input type="radio" name="type" onclick="doBind(false)" value="2" checked="checked"/>
	        <b class="tool_box_b">未加入:</b>
	        <input type="radio" name="type" onclick="doBind(true)" value="1"/>
	        <b class="tool_box_b">已加入:</b>
	        <br/>
		    <b class="tool_box_b">角色名称：</b>
		    <input type="text" class="tab_form_txt" name="sm_name" id="sm_name"/>
		    <input id="s_type"  type="hidden" value="0"/>
		    <input id="userId"  type="hidden" value="${param.userId}" name="userId"/>
		    <input type="button" class="btn" value="查&nbsp;&nbsp;询" id="search"/>
			<input type="button" class="btn" value="重&nbsp;&nbsp;置" id="resetAll" onclick="reset();"/>
	    </div>
	    <div id="operate" class="tool_operate">
			<input type="button" id="btns-add" class="reg" onclick="oneSelect()" value="加入" />
			<input type="button" id="btns-add2" class="reg" onclick="oneSelect()" value="撤销" />
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
	function reset(){
		jQuery("#sm_name").val("");
	}
	jQuery(document).ready(function() {
		jQuery("#btns-add2").hide();
	});
	
	jQuery(window).resize(function() {
	});
	function oneSelect(){
			var rows = $('#datas-grid').datagrid("getSelections");	//获取你选择的所有行
			if (rows === null || rows.length<=0) {
				$.messager.alert("提示","请选择记录",'warning');
			}else {
				var isCheckPass = true;
				if (window.checkEdit){
					
					isCheckPass=checkEdit();
				}
				if (isCheckPass==true){
					var type=$("#s_type").val();
					var url="";
					if(type==1){
						url="unBind.action";
					}
					if(type==0){
						url="doBind.action";
					}
					var QString = "userId="+$("#userId").val();
				    for(var i =0;i<rows.length;i++){    
				    	QString += "&_selects="+rows[i].id;
				    }
					$.post(url, QString, function(data) {
						if (data.code) {
							message(data.msg,'ok');
						} else {
							message(data.msg,'no');
						}
						fnc_search(lsAction);
					}, 'json');
					$("#manage-dialog").dialog("close");
				} else {
				}
			}
	  }
</script>