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
			$(function() {
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
					pagination : true,
					url : "${pageContext.request.contextPath}/control/system/role/showNotAssociatedRoles.action?mid=" + $("#mid").val()
				});
				
				$("#btns-add2").hide();
				
				$("#container-wrapper").resize(function() {
					$("#datas-grid").datagrid("resize");
				});
				
				$('#superuser_list').css({height:$(window).height()});
				
				$("#search").click(function() {
					$("#datas-grid").datagrid({
						url : "${pageContext.request.contextPath}/control/system/role/searchRoles.action",
						method : "post",
						queryParams : {
							mid : $("#mid").val(),
							name : $("#sm_name").val(),
							type : $("input[name='type']:checked").val()
						}
					});
				});
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
			
			function doBind(value){
				if(value){ //已关联
					$("#s_type").val("1");
					$("#btns-add2").show();
					$("#btns-add").hide();
					$("#datas-grid").datagrid({
						url : "${pageContext.request.contextPath}/control/system/role/showAssociatedRoles.action",
						method : "post",
						queryParams : {
							mid : $("#mid").val()
						}
					});
				}else{ //未关联
					$("#s_type").val("0");
					$("#btns-add2").hide();
					$("#btns-add").show();
					$("#datas-grid").datagrid({
						url : "${pageContext.request.contextPath}/control/system/role/showNotAssociatedRoles.action",
						method : "post",
						queryParams : {
							mid : $("#mid").val()
						}
					});
				}
			}
			
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
				$("#sm_name").val("");
			}
			
			function oneSelect(flag){
				var rows = $('#datas-grid').datagrid("getSelections");	//获取你选择的所有行
				if (rows === null || rows.length<=0) {
					$.messager.alert("提示","请选择记录",'warning');
				}else {
					var url="${pageContext.request.contextPath}/control/system/menu/";
					
					if(flag) { //true为关联
						url += "doBind.action";
					} else {
						url += "unBind.action";
					}
				
					var QString = "mid="+$("#mid").val();
				    for(var i =0;i<rows.length;i++){    
				    	QString += "&_selects="+rows[i].id;
				    }
					$.post(url, QString, function(data) {
						if (data.code) {
							//重刷新角色列表显示的数据
							doBind(!flag); //刷新关联的角色列表
							message(data.msg,'ok');
						} else {
							message(data.msg,'no');
						}
					}, 'json');
				}
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
				        <b class="tool_box_b">未关联:</b>
				        <input type="radio" name="type" onclick="doBind(true)" value="1"/>
				        <b class="tool_box_b">已关联:</b>
				        <br/>
					    <b class="tool_box_b">角色名称：</b>
					    <input type="text" class="tab_form_txt" name="sm_name" id="sm_name"/>
					    <input id="s_type"  type="hidden" value="0"/>
					    <input id="mid"  type="hidden" value="${param.mid}" name="mid"/>
					    <input type="button" class="btn" value="查&nbsp;&nbsp;询" id="search"/>
						<input type="button" class="btn" value="重&nbsp;&nbsp;置" id="resetAll" onclick="reset();"/>
				    </div>
				    <div id="operate" class="tool_operate">
						<input type="button" id="btns-add" class="reg" onclick="oneSelect(true)" value="关联" />
						<input type="button" id="btns-add2" class="reg" onclick="oneSelect(false)" value="撤销" />
					</div>
			    </div>
			</form>
			<table id="datas-grid"></table>
		</div>
	</body>
</html>