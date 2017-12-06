<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>菜单管理</title>
<%@include file="/res/common/index.jsp"%>
<%@include file="/res/js/document/common.jsp"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/res/js/ocupload/jquery.ocupload-1.1.2.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/res/component/dialog/jquery.teabo.dialog.js"></script>
<script>
			var parameter_row_index = 0;
			var arrayObj = new Array();
			
			//添加参数的输入框
			function addRow(key,val){
				if(key==null||key==""){
					key="";
				}
				if(val==null||val==""){
					val="";
				}
				var tabEle = document.getElementById("parameter_rows");
				var addTr = tabEle.insertRow();
				addTr.setAttribute("id", "parameter_tr_"+parameter_row_index);
				addTr.setAttribute("index", parameter_row_index);
				var td_1 = addTr.insertCell();
				td_1.setAttribute("class","td_txt_field");
				td_1.setAttribute("id", "td_"+parameter_row_index+"_1");
				td_1.innerHTML="<input type='text' class='form_txt' name='params' value='"+key+"' id='txt_"+parameter_row_index+"_1'>&nbsp;&nbsp;";
				var td_2 = addTr.insertCell();
				td_2.setAttribute("class","td_txt_field");
				td_2.setAttribute("id", "td_"+parameter_row_index+"_2");
				td_2.innerHTML="<input type='text' class='form_txt' name='params' value='"+val+"' id='txt_"+parameter_row_index+"_2'>&nbsp;&nbsp;";
				
				var td_3 = addTr.insertCell();
				td_3.setAttribute("class","td_txt_field");
				td_3.setAttribute("align","center");
				td_3.setAttribute("id", "td_"+parameter_row_index+"_3");
				td_3.innerHTML="<button  onclick='deleteRow("+parameter_row_index+");' class='btn'>删除参数</button>";
				parameter_row_index ++;
				arrayObj.push(parameter_row_index);
			}
			
			//删除参数输入框
			 function deleteRow(index){
				  var tabEle = document.getElementById("parameter_rows");
				  var currentRow = document.getElementById("parameter_tr_"+index);
				  tabEle.deleteRow(currentRow.rowIndex);
				}
			 
			 //清空参数列表
			 function initRow(arrayObj) {
				$.each($("tr[id^='parameter_tr_']"),function(key,val){
					deleteRow($(val).attr('index'));
				});
			 }
			
			 //遍历初始化参数列表
			 function initParams(param){
				if(param != "") {
					obj= $.parseJSON(param); //Takes a well-formed JSON string and returns the resulting JavaScript object.
					$.each(obj, function(key,val){
						addRow(key,val);
					});
				}
			 }
			 
			 //设置菜单链接的可编辑性
			 function menuTypeChange(obj){
				 if(obj.value=="@@"){
					 $("#content_url").attr("disabled","disabled");
					 $("#parameter_rows").hide();
				 }else{
					 $("#content_url").removeAttr("disabled");
					 $("#parameter_rows").show();
				 }
			 }
			 
			 //判断某节点是否有子节点
			 function isHasChildren(mTree, node) {
				 if(node.state == "open") {
					 var childNodes={};
					try{
						 childNodes = mTree.tree("getChildren", node.target);
					}catch(e){
					}
					if (childNodes.length > 0) {
						return "closed";
					}else {
						return "open";
					}
				 } else {
					 return node.state;
				 }
			 }
			 
			 //判断节点是否是顶级节点
			 function isRootNode(node) {
				 var rootNode = false;
				 var roots = $("#mTree").tree('getRoots');
				 for(var i=0; i<roots.length; i++) {
					 if(node == roots[i]){
						 rootNode = true;
						 break;
					 }
				 }
				 return rootNode;
			 }
			 
			$(function() {
				var obj = $("#menu_type");
				menuTypeChange(obj);
				var flag = true;//true为第一次加载
				//初始化菜单树
				$("#mTree").tree({
					url : "${pageContext.request.contextPath}/control/system/menu/showMenuTree.action",
					onLoadSuccess : function(node, data) {
						 if(flag) {
							var roots = $("#mTree").tree('getRoots');
							//$("#mTree").tree('expand', roots[0].target);
							$("#mTree").tree('select', roots[0].target);
							flag = false;
						} 
						$("#detail").css({visibility:"visible"});
					 },
					onSelect : function(node) {
						//回显菜单节点信息
						$.post("${pageContext.request.contextPath}/control/system/menu/getMenuById.action", {id : node.id}, function(data) {
							$("#mid").val(data.id);
							$("#content_name").val(data.name);
							$("#content_icon").val(data.icon);
							$("#content_sortId").val(data.sortId);
							var parent = $("#mTree").tree('getParent', node.target);
							if(parent != null) {
								$('#content_parentid').combotree('setValue', parent.id);    
							} else {
								$('#content_parentid').combotree('setValue', '');
							}
							
							var type = data.type;
							if(type == "@@") {
								$("#main").attr("selected", true);
								$("#content_url").attr("disabled", true);
								$("#outer").attr("selected", false);
								$("#inner").attr("selected", false);
								$("#parameter_rows").hide();
							} else if(type == "05"){
								$("#inner").attr("selected", true);
								$("#content_url").attr("disabled", false);
								$("#outer").attr("selected", false);
								$("#main").attr("selected", false);
								$("#parameter_rows").show();
							} else {
								$("#outer").attr("selected", true);
								$("#content_url").attr("disabled", false);
								$("#main").attr("selected", false);
								$("#inner").attr("selected", false);
								$("#parameter_rows").show();
							}
							$("#content_url").val(data.url);
							if(data.iconUrl != null) {
								$("#iconCls").show();
								$("#iconCls").attr("src", data.iconUrl);
							} else {
								$("#iconCls").hide();
							}
							
							//初始化参数列表
							initRow(arrayObj);
							
							var param = data.parameter;
							if(param != null && param.length > 0) { //遍历参数列表
								initParams(param);
							}
							
							$("#btn_save").show();
							$("#detail").css({visibility:"visible"});
						});
						
						$("#btn_cancel").hide();
					 }
				});
				
				//打开添加菜单节点框
				$("#btns-add").click(function() {
					var node = $("#mTree").tree('getSelected');
					if(node == null) {
						$.messager.alert("提示信息", "请选择节点！", "info");
						return;
					}
					
					$("#formItem").get(0).reset();
					$("#content_icon").val("");
					$("#mid").val("");
					
					$("#content_parentid").combotree({
						url : "${pageContext.request.contextPath}/control/system/menu/showMenuTreeOnce.action"
					});
					
					$("#main").attr("selected", "selected");
					$("#content_url").attr("disabled", true);
					$("#parameter_rows").hide();
					
					$("#iconCls").attr("src", "");
					$("#iconCls").hide();
					//设置上级菜单的值
					if(node != null) {
						$('#content_parentid').combotree('setValue', node.id);    
					}
					initRow(arrayObj); //初始化参数列表
					$("#btn_cancel").show();
					$("#detail").css({visibility:"visible"});
				});
				
				// 一键上传图标
				$("#upload").upload({
			        name: 'file',  // <input name="file" />
			        action: '${pageContext.request.contextPath}/control/system/menu/uploadImg.action',  // 提交请求路径
			        enctype: 'multipart/form-data', // 编码格式
			        autoSubmit: true, // 选中文件提交表单
			        onComplete: function(data) { // 请求完成时 调用函数
			        	eval("var data2 = " + data);
			        	if(data2.msg == "success"){
			        		$("#content_icon").val(data2.fileName);
							$("#iconCls").attr("src", data2.path + data2.fileName);
							$("#iconCls").show();
			        		$.messager.alert("提示信息","上传图标成功！","info");
			        	} else {
			        		$.messager.alert("提示信息","上传图标失败！","info");
			        	}
			        }
				});
				
				$("#associateRole").click(function() {
					var node = $("#mTree").tree('getSelected');
					if(node == null) {
						$.messager.alert("提示信息", "请选择节点！", "info");
						return;
					}
					
					var mid = node.id;
					$.Dialog.show({
						width : 900,
						height : 500,
						title : "详细信息",
						url : "${pageContext.request.contextPath}/control/system/menu/toBindRoles.action?mid=" + mid,
						showButton : false
				  });
				});
				
				//删除菜单节点
				$("#btns-del").click(function() {
					var node = $("#mTree").tree('getSelected');
					if(node == null) {
						$.messager.alert("提示信息", "请选择节点！", "info");
						return;
					} else {
					 $.messager.confirm('提示', '你确定删除选择的记录吗？', function(r) {
							$.messager.progress("close");
							if (r) {
							    var childNodes={};
								try{
									 childNodes = $("#mTree").tree('getChildren', node.target);
								}catch(e){
									
								}
								if (childNodes.length > 0||"closed"==node.state) {
									$.messager.alert("提示信息", "该节点还有子节点，不能直接删除！", "info");
									return;
								}
								$.post("${pageContext.request.contextPath}/control/system/menu/deleteMenuById.action", {id : node.id}, function(msg) {
									$.messager.alert("提示信息", msg, "info");
									if(msg == "删除成功！") {
										$('#mTree').tree({    
										    url:'${pageContext.request.contextPath}/control/system/menu/showMenuTree.action'  //重新加载树 
										}); 
										$("#formItem").get(0).reset();
									}
								});
							}
						});
					}
				});
				
				//取消添加菜单节点
				$("#btn_cancel").click(function() {
					$("#detail").css({visibility:"hidden"});
				});
				
				//保存菜单节点
				$("#btn_save").click(function() {
					var msg = "";
					
					if($("#content_name").val() == '') {
						msg += "菜单名称不能为空！";
					}
					
					var parentTree = $("#content_parentid").combotree('tree');//上级菜单树
					var selectNode = parentTree.tree('getSelected');// 获取上级菜单树选择的节点
					if($("#mid").val() != null) {
						var node = parentTree.tree('find', $("#mid").val());
						if(selectNode == node) {
							msg += "不能选择本身作为自己的父节点！";
						}
						
						var childNodes={};
					    try{	
						   childNodes = parentTree.tree('getChildren', node.target);
					    }catch(e){
					    }
					    for(var i = 0; i< childNodes.length; i++) {
							if(selectNode == childNodes[i]) {
								msg += "不能选择该节点的子节点作为作为新的父节点！";
								break;
						    }
						}
					}
					
					if (!validateInteger(document.getElementsByName("sortId")[0].value) || document.getElementsByName("sortId")[0].value>9999999999){
						msg += "   序号值只能为整数(最大长度为10位)！";
						rtn = false;	
					}
					
					var params = $("[name='params']");
					if(params != null) {
						for(var i = 0; i < params.length; i++) {
							if($(params[i]).val() == "") {
								msg += "参数或参数值不能为空！";
								break;
							}
						}
					}
					
					if (msg.length > 0) {
						showMessage('error',msg);
						return; 
					} else {
						if($("#mid").val() == "") {//为添加菜单节点
							if(selectNode.id == null) {//选择的上级菜单项为顶级菜单
								var roots = $("#mTree").tree('getRoots');
								if(roots.length > 0) {
									for (var i=0; i<roots.length; i++) {
										if($("#content_name").val() == roots[i].text) {
											$.messager.alert("提示信息", "已存在名称为" + $("#content_name").val() + "的节点！", "info");
											return;
										}
									}
								}
							} else {
								var parentNode = $("#mTree").tree('find', selectNode.id);
								var childNodes = parentNode.children;
								
								if (childNodes.length > 0) {
									for (var i=0; i<childNodes.length; i++) {
										if($("#content_name").val() == childNodes[i].text) {
											$.messager.alert("提示信息", "已存在名称为" + $("#content_name").val() + "的节点！", "info");
											return;
										}
									}
								}
							}
						} else { //为修改菜单节点
							if(selectNode.id == null) {//选择的上级菜单项为顶级菜单
								var roots = $("#mTree").tree('getRoots');
								if(roots.length > 0) {
									for (var i=0; i<roots.length; i++) {
										if($("#content_name").val() == roots[i].text) {
											$.messager.alert("提示信息", "已存在名称为" + $("#content_name").val() + "的节点！", "info");
											return;
										}
									}
								}
							} else {
								var selected = $("#mTree").tree('getSelected'); //菜单树选择的节点
								var parentNode = $("#mTree").tree('find', selectNode.id);
								var childNodes = parentNode.children;
								
								if (childNodes.length > 0) {
									for (var i=0; i<childNodes.length; i++) {
										if($("#content_name").val() == childNodes[i].text && childNodes[i]!=selected) {
											$.messager.alert("提示信息", "已存在名称为" + $("#content_name").val() + "的菜单！", "info");
											return;
										}
									}
								}
							}
						}
						
						$.post("${pageContext.request.contextPath}/control/system/menu/saveOrUpdateMenu.action", $("#formItem").serialize(), function(datas) {
							var isLoadComplete = true;//菜单树需加载的节点是否加载完成
							if(datas.msg == "保存成功！") {
								$("#content_parentid").combotree({
									url : "${pageContext.request.contextPath}/control/system/menu/showMenuTreeOnce.action"
								});
								
								var roots = $("#mTree").tree('getRoots');
								if($("#mid").val() == "") { //为追加节点
									if(selectNode.id == null) {//选择的上级菜单项为顶级菜单
										$("#mTree").tree('insert', {
											after: roots[roots.length-1].target,
											data: {
												id: datas.mid,
												text: $("#content_name").val()
											}
										});
									} else {//选择的上级菜单项非顶级菜单
										var pNode = $("#mTree").tree('find', selectNode.id);
										if(isRootNode(pNode)) {
											$("#mTree").tree('expand', pNode.target);
										} else {
											$("#mTree").tree('expandTo', pNode.target);
										}
										
										$("#mTree").tree('append', {
											parent: pNode.target,
											data: [{
												id: datas.mid,
												text: $("#content_name").val()
											}]
										});
									}
								} else {//为修改节点
									var selected = $("#mTree").tree('getSelected'); //菜单树选择的节点
									var preParentNode = $("#mTree").tree('getParent', selected.target);//菜单树选择的节点的原先的父节点

									if(preParentNode != null) {//原先父节点不是顶级菜单
										if(selectNode.id == null) {//选择的上级菜单项为顶级菜单
											var stat = isHasChildren($("#mTree"), selected);
											$("#mTree").tree('remove', selected.target);
											
											$("#mTree").tree('insert', {
												after: roots[roots.length-1].target,
												data: {
													id: datas.mid,
													text: $("#content_name").val(),
													state: stat
												}
											});
										} else if(selectNode.id == preParentNode.id) {//未改变父节点
											$("#mTree").tree('update', {
												target: selected.target,
												text: $("#content_name").val()
											});
										} else if(selectNode.id != preParentNode.id) {//改变了父节点
											var stat = isHasChildren($("#mTree"), selected);
											$("#mTree").tree('remove', selected.target);
											var pNode = $("#mTree").tree('find', selectNode.id);
											if(isRootNode(pNode)) {
												$("#mTree").tree('expand', pNode.target);
											} else {
												$("#mTree").tree('expandTo', pNode.target);
											}
											
											$("#mTree").tree('append', {
												parent: pNode.target,
												data: [{
													id: datas.mid,
													text: $("#content_name").val(),
													state: stat
												}]
											});
										}
									} else {//原先父节点为顶级菜单
										if(selectNode.id == null) {//选择的上级菜单项为顶级菜单
											$("#mTree").tree('update', {
												target: selected.target,
												text: $("#content_name").val()
											});
										} else {
											var stat = isHasChildren($("#mTree"), selected);
											$("#mTree").tree('remove', selected.target);
											var pNode = $("#mTree").tree('find', selectNode.id);
											if(isRootNode(pNode)) {
												$("#mTree").tree('expand', pNode.target);
											} else {
												$("#mTree").tree('expandTo', pNode.target);
											}
											
											$("#mTree").tree('append', {
												parent: pNode.target,
												data: [{
													id: datas.mid,
													text: $("#content_name").val(),
													state: stat
												}]
											});
										}
									}
								}
								
								$.messager.alert("提示信息", datas.msg, "info");
								
								var getSelectedNode = $("#mTree").tree('find', datas.mid);
								if(getSelectedNode != null) {
									$("#mTree").tree('select', getSelectedNode.target);
									var parent = $("#mTree").tree('getParent', getSelectedNode.target);
									if(parent != null) {
										$('#content_parentid').combotree('setValue', parent.id);    
									} else {
										$('#content_parentid').combotree('setValue', null);
									}
								}
								
	                            $("#btn_cancel").hide();
								$("#detail").css({visibility:"visible"});
								$("#mid").val(datas.mid);
							}
						});
					}
				});
			});
		</script>
</head>
<body style="border: 1px solid #dfe3e6;">
	<%@ include file="/res/common/msg.jsp"%>
	<div style="width: 100%; height: 100%; overflow: auto;">
		<table style="width: 100%; height: 100%" cellspacing='0'
			cellpadding='0'>
			<tr style="width: 100%; height: 30px;">
				<td valign="top"
					style="width: 100%; border-bottom: 1px solid #dfe3e6;" colspan="4">
					<div id="tools-bar">
						<div id="operate" class="tool_operate" style="border: 0px;">
							<input type="button" id="btns-add" class="add" value="新增菜单" /> <input
								type="button" id="btns-del" class="del" value="删除" /> <input
								type="button" id="btns-permission" class="send_power"
								onclick="permissionBind();" value="授权" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td width="150px" valign="top"
					style="border-right: 1px solid #dfe3e6;"><div id="mTree"></div></td>
				<td width="8px">&nbsp;</td>
				<td valign="top">
					<div id="detail" style="visibility: hidden;">
						<form id="formItem" name="formItem">
							<div id="contentTable" class="front-scroll-auto"
								style="border: 0px;">
								<input type="hidden" id="mid" name="id" />
								<table width="95%" class="mauto tab_form">
									<tr height="35">
										<td class="field" align="right"><font color="red">*</font>菜单名：
										</td>
										<td title="菜单名"><input type="text" class="tab_form_txt"
											maxlength="20" size="20" name="name" id="content_name" /></td>

										<td class="field" align="right">菜单类型：</td>
										<td title="菜单类型"><select id="menu_type"
											onchange="menuTypeChange(this);" class="tab_form_select"
											style="width: 285px;" name="type">
												<option id="main" value="@@">主菜单</option>
												<option id="inner" value="05">内部链接</option>
												<option id="outer" value="06">外部链接</option>
										</select></td>
									</tr>
									<tr height="35">
										<td class="field" align="right">上级菜单：</td>
										<td><input id="content_parentid" name="parentId"
											class="easyui-combotree"
											data-options="url:'${pageContext.request.contextPath}/control/system/menu/showMenuTreeOnce.action',method:'post'"
											style="width: 282px;" /></td>

										<td class="field" align="right"><font color="red">*</font>排序：
										</td>
										<td title="排序"><input type="text" class="tab_form_txt"
											name="sortId" id="content_sortId" /></td>
									</tr>
									<tr height="35">
										<td class="field" align="right">菜单链接：</td>
										<td title="菜单链接"><input type="text" class="tab_form_txt"
											name="url" id="content_url" /></td>
										<td class="field" align="right">菜单图标：</td>
										<td><img id="iconCls"
											style="display: inline; float: left; margin-right: 5px; width: 20px; height: 20px" />
											<input id="upload" type="button" value="上传图标..." /> <input
											type="hidden" name="icon" id="content_icon" /></td>
									</tr>

								</table>
								<table
									style="margin-top: 5px; border-top: 1px solid #89a1c5; font-size: 14px;"
									width="95%" class="mauto tab_form" id="parameter_rows">
									<tr height="35">
										<th width="33%">参数名</th>
										<th width="33%">参数值</th>
										<th><input type="button" id="add" value="添加参数"
											onclick="addRow();" class="btn" /></th>
									</tr>
								</table>
								<div id="activityTable" class="activity_box">
									<input type="button" id="btn_save" class="save"
										value="保&nbsp;&nbsp;存" /> <input type="button"
										id="btn_cancel" class="cancel" value="取&nbsp;&nbsp;消" />
								</div>
							</div>
						</form>
					</div>
				</td>
				<td width="8">&nbsp;</td>
			</tr>
		</table>
	</div>
</body>
</html>
<script>
  $(function() {
		$("#error").hide();
  });
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
</script>