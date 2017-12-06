<%@ page contentType="text/html; charset=UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@include file="/res/common/tags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>菜单信息</title>
<%@include file="/res/common/index.jsp"%>
<%@include file="/res/js/document/common.jsp"%> 
<script src="<%=contextPath%>/res/js/common.js"></script>
<script type="">
		var contextPath='<%=contextPath%>';
</script>
<style type="">
.form_txt{
/* 	border: medium none;
    display: block;
    line-height: 20px;
    margin: 0 auto;
    margin-right:2px;
    padding: 6px 0;
    width: 100%; */
}
.td_txt_field{
	/* border: 1px solid #89a1c5; 
	margin-right:2px;
    letter-spacing: 2px; */
    text-align: center;
}
</style>
<script>
	//检查用户中名称中是否包含()字符
	function checkStringAsDefault() {
		var rtn = true;
		var msg = "";
		if (!document.getElementById("content_name").value){
			msg = "菜单名称不能为空！";
			rtn = false;
		}
		if (!validateInteger(document.getElementsByName("sortId")[0].value) || document.getElementsByName("sortId")[0].value>9999999999){
			msg += "   序号值只能为整数(最大长度为10位)！";
			rtn = false;	
		}
		if (msg.length>0){
			showMessage('error',msg);
		}
		return rtn;
	}
 	function save(str){
		if(str == "saveAndNew"){
			jQuery("#formItem").attr("action",contextPath+"/control/system/menu/saveAndNew.action");
		}else{
			jQuery("#formItem").attr("action",contextPath+"/control/system/menu/save.action");
		}
		//alert(jQuery("#formItem").attr("action"));
		if(checkStringAsDefault())
		 {
			jQuery("#formItem").submit();
		 }
		
	}
	
 	function exit(){
		window.location.href=contextPath+"/control/system/menu/list.action";
	} 
	var parameter_row_index = 0;
	
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
	}
	
	 function deleteRow(index){
		  var tabEle = document.getElementById("parameter_rows");
		  var currentRow = document.getElementById("parameter_tr_"+index);
		  tabEle.deleteRow(currentRow.rowIndex);
		}
	
	 function initParams(){
		if('${content.parameter}'!=""){
			var jsons = ${content.parameter}
			jQuery.each(jsons,function(key,val){
				addRow(key,val);
			});
		}
	 }
	 
	 function menuTypeChange(obj){
		 //alert(obj.value);
		 if(obj.value=="@@"){
			 jQuery("#content_url").attr("disabled","disabled");
		 }else{
			 jQuery("#content_url").removeAttr("disabled");
		 }
	 }
	 
	jQuery(function(){
		obj = document.getElementById("menu_type");
		menuTypeChange(obj);
		jQuery.ajax({
             url: contextPath+"/control/system/menu/getMenuListAjax.action",
             type: "post",
             async: "true",  // true 为异步，false 为同步
             dataType: "json",
             success: function(data){
            	var sel = jQuery("#content_parentid");
            	var ops = "";
              	jQuery.each(data,function(key,val){
              		//alert("${content.parentId}"+"---"+key);
              		if("${content.parentId}"==key ){
              			ops = ops + "<option value='"+key+"' selected = 'selected'>"+val+"</option>";
              		}else{
              			ops = ops + "<option value='"+key+"'>"+val+"</option>";              			
              		}
              	});
              	sel.html(ops);
            }
   		 });
		if('${content.parameter}'!=""&&'${content.parameter}'!=null){
			initParams();
		};
		
	});
</script>
</head>
<body id="menu_info" class="body-front" style="padding: 0;margin: 0;border:1px solid #DFE3E6;border-top:0px;">
<div id="container" class="front-align-top">
<form id="formItem" name="formItem" action="save" method="post" >
    <%@include file="/res/common/msg.jsp"%>
	<div id="contentTable" class="front-scroll-auto" style="border:0px;">
		<h1 class="title" style="font-size:15px;border-bottom:1px solid #DFE3E6;">菜单管理</h1>
		<table width="95%" class="mauto tab_form">
			<input type="hidden"  name="id" value="${content.id}"/>
		  	<input type="hidden"  name="istemp" value="${content.istemp}"/> 
			<tr height="35">
				<td class="field" align="right">
					<font color="red">*</font>菜单名：
				</td>
				<td title="菜单名">
					<input type="text" class="tab_form_txt"  maxlength="20" size="20" value="${content.name}" name="name" id="content_name" />
				</td>
				
				<td class="field" align="right">菜单类型：</td>
				<td title="菜单类型" >
					<select id="menu_type" onchange="menuTypeChange(this);" class="tab_form_select" name="type">
						<option value="@@" <c:if test="${content.type eq '@@'}">selected="selected"</c:if>>主菜单</option>
						<option value="05" <c:if test="${content.type eq '05'}">selected="selected"</c:if>>内部链接</option>
						<option value="06" <c:if test="${content.type eq '06'}">selected="selected"</c:if>>外部链接</option>
					</select>
				</td>
			</tr>
			<tr height="35">
				<td class="field" align="right">
					<font color="red"></font>上级菜单：
				</td>
				<td title="上级菜单">
					<select id="content_parentid" class="tab_form_select" name="parentId">
						
					</select>
				</td>
				
				<td class="field" align="right">
					<font color="red">*</font>排序：
				</td>
				<td title="排序">
					<input type="text" class="tab_form_txt" name="sortId" value="${content.sortId }" id="content_sortId" />
				</td>
			</tr>
			<tr height="35">
				<td class="field" align="right">
					<font color="red"></font>菜单链接：
				</td>
				<td title="菜单链接">
					<input type="text" class="tab_form_txt" <c:if test="${content.type eq '@@'}">disabled="disabled"</c:if> name="url" value="${content.url }" id="content_url" />
				</td>
				<td class="field" align="right">
					<font color="red"></font>菜单图标：
				</td>
				<td title="菜单链接">
					<input type="text" class="tab_form_txt"  name="icon" value="${content.icon }" id="content_icon" />
				</td>
			</tr>
		</table>
		<table style="margin-top:5px;border-top:1px solid #89a1c5;" width="95%" class="mauto tab_form" id="parameter_rows">
			<tr height="35"><th width="33%">参数名</th><th width="33%">参数值</th><th><input type="button" id="add" value="添加参数" onclick="addRow();" class="btn" ></th></tr>
		</table>
		<div id="activityTable" class="activity_box">	
			     <input type="button"  id="btn_save_new" onclick="save('saveAndNew')" class="save_new" value="保存/新建"/>
				 <input type="button"  id="btn_save" onclick="save()" class="save" value="保&nbsp;&nbsp;存"/>
				 <input type="button"  id="btn_return" onclick="exit()" class="return" value="返&nbsp;&nbsp;回"/>
		</div>
	</div>
</form>
</div>

</body>
<script>
	jQuery(document).ready(function() {
		adjustDocumentLayout();
		toggleButton("button_act");
	});

	jQuery(window).resize(function(){
		adjustDocumentLayout();
	});
</script>
</html>