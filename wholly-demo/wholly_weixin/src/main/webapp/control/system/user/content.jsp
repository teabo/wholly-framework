<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.reflect.Method"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户信息</title>
<script type="">
	var contextPath='<%=contextPath%>';
</script>

<%@include file="/res/common/index.jsp"%>
<%@include file="/res/js/document/common.jsp"%> 
<script src="${pageContext.request.contextPath}/res/js/common.js"></script>
<script>

	//检查用户中名称中是否包含()字符
	function checkStringAsDefault() {
		var rtn = true;
		var msg = "";
		if (document.getElementsByName("name")[0].value==''){
			msg = "用户名称不能为空！";
			rtn = false;
		}
		if(checkStr(document.getElementsByName("name")[0].value)){
			msg = "用户名称中含有非法字符！";
			rtn = false;
		}
		if (document.getElementsByName("loginName")[0].value==''){
			msg += "用户账号不能为空！";
			rtn = false;
		}
		if(checkStr(document.getElementsByName("loginName")[0].value)){
			msg = "用户账号中含有非法字符！";
			rtn = false;
		}
		if (!document.getElementsByName("certId")[0].value){
			msg += "证件号码不能为空！";
			rtn = false;
		}
		if (document.getElementsByName("newpassword")[0].value==''){
			msg += "用户密码不能为空！";
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
	function checkStr(str){
		var pat=new RegExp("[^a-zA-Z0-9\_\u4e00-\u9fa5]","i");
	    if(pat.test(str))   
	    {   
	        return true;   
	    } 
	}
	function checkIp(ip){
		var re =  /^([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;
		return re.test(ip);
	}
	$.extend($.fn.validatebox.defaults.rules, {    
		checkIp: {    
	        validator: function(value,param){    
	            return checkIp(value);    
	        },    
	        message: 'IP格式不正确！'   
	    }    
	});  
	$(document).ready(function() {
		$("#datas-grid").datagrid({
			fit : true,
			columns:[[
					    {field:'startIp',title:'起始IP',width:'250', editor: { type: 'validatebox', options: { required: true,validType: 'checkIp'}} },
					    {field:'endIp',title:'结束IP',width:'250', editor: { type: 'validatebox', options: { required: true,validType: 'checkIp'} }},
					    {field:'action',title:'操作',width:'150',
					    	formatter:function(value,row,index){
					    		var s = '<a href="#" onclick="saverow(this)">保存</a>';
				    			var c = '<a href="#" onclick="cancelrow(this)">取消</a>';
				    			var e = '<a href="#" onclick="editrow(this)">修改</a>';
				    			var d = '<a href="#" onclick="deleteRow(this)">删除</a>';
					    		if(row.editing){
					    			return e+" "+s+" "+d;
					    		}else{
					    			return e+" "+s+" "+d;
					    		}
					    	}
					    }
					]],
			striped : true,
			rownumbers : true,
			singleSelect : true,
			toolbar : "#tools-bar",
			pagination : true,
			onBeforeEdit:function(index,row){
				row.editing = true;
				updateActions(index);
			},
			onAfterEdit:function(index,row){
				row.editing = false;
				updateActions(index);
			},
			onCancelEdit:function(index,row){
				row.editing = false;
				updateActions(index);
			}
		});
		function updateActions(index){
			$('#datas-grid').datagrid('updateRow',{
				index: index,
				row:{}
			});
		}
		$("#container-wrapper").resize(function() {
			$("#datas-grid").datagrid("resize");
		});
		searchButtonBinds('bindIpList.action?s_user_id=${content.id}'); //绑定查询按钮
		doSearch();
		//$('#superuser_list').css({height:$(window).height()});
	});
	
</script>
</head>
<body id="org_info" class="body-front">
<div id="container" class="front-align-top">
<form id="formItem" name="formItem" action="saveUser.action" method="post"  validate="true" theme="simple" onsubmit="javascript:return checkStringAsDefault(this);">
	<%@include file="/res/common/msg.jsp"%>
	<div id="contentTable" class="front-scroll-auto" style="border:0px;">
    <h1 class="title" style="font-size:15px;border-bottom:1px solid #DFE3E6;">用户信息</h1>
	 	<input type="hidden"  name="istemp" value="${content.istemp eq null?1:content.istemp}"/>
      <input type="hidden"  name="id" value="${content.id}"></input>
	<table class="mauto tab_form">
		<tr >
		   
			<td  class="field" align="right">
				<font color="red">*</font>用户名：
			</td>
			<td   title="用户名">
			<input type="text" class="tab_form_txt" maxlength="20"  name="name" id="content_name" value="${content.name}"  />
			</td>
			<td  class="field" align="right">登录别名：</td>
			<td  title="登录别名" class="justForHelp">
				<input type="text" class="tab_form_txt" maxlength="20"   name="aliasName"  value="${content.aliasName}"  />
			</td>
		</tr>
		<tr >
			<td class="field" align="right">
				<font color="red">*</font>登录名：
			</td>
			<td  id="loginName_h" title="登录名">
				<input type="text" class="tab_form_txt" maxlength="20"  name="loginName" id="content_loginName" value="${content.loginName}"  />
			</td>
			<td class="field" align="right"><font color="red">*</font>证件类型：</td>
			<td id="cert_id_h" colspan="1" >
			   <input type="radio" name="certType" value="00" ${content.certType eq "00"?"checked=\"checked\"":"" } />身份证
			   <input type="radio" name="certType" value="01" ${content.certType eq "01"?"checked=\"checked\"":"" } />警官证
			   <input type="radio" name="certType" value="02" ${content.certType eq "02"?"checked=\"checked\"":"" } />军官证
			</td>
		</tr>
		<tr >
			<td class="field" align="right">
				<font color="red">*</font>密码：
			</td>
			<td id="password_h" title="密码">
				<input type="password" maxlength="15" name="newpassword" value="${content.realPassword}" id="formItem__password" class="tab_form_txt"/>
				<input type="hidden" maxlength="15" name="userPassword" value="${content.userPassword}"  />
			</td>
			<td class="field" align="right"><font color="red">*</font>证件号码：</td>
			<td id="cert_id_h" colspan="1" title="cert_id">
				<input type="text" class="tab_form_txt" maxlength="18"    name="certId"  value="${content.certId}" />
			</td>
		</tr>
		<tr>
			<td class="field" align="right">性别：</td>
			<td id="sex_h" title="性别" >
				<input type="radio" name="sex" value="1" ${content.sex eq "1"?"checked=\"checked\"":"" }/>男
				<input type="radio" name="sex" value="2" ${content.sex eq "2"?"checked=\"checked\"":"" }/>女
			</td>
			
			<td class="field" align="right">用户级别：</td>
			<td title="用户级别">
			    <input type="radio" name="userLevel" value="0" ${content.userLevel eq "0"?"checked=\"checked\"":"" } />普通用户
			    <input type="radio" name="userLevel" value="1" ${content.userLevel eq "1"?"checked=\"checked\"":"" }/>管理员用户
			    <input type="radio" name="userLevel" value="65536" ${content.userLevel eq "65536"?"checked=\"checked\"":"" } />超级管理员
			</td>
		</tr>
		<tr >
		    <td class="field" align="right">用户类型：</td>
			<td>
			   	<input type="radio" name="userType" value="0" ${content.userType eq "0"?"checked=\"checked\"":"" } />本地用户
			    <input type="radio" name="userType" value="1" ${content.userType eq "1"?"checked=\"checked\"":"" } />漫游用户
			</td>
			<td class="field" align="right">状态：</td>
			<td>
			     <input type="radio" name="status" value="1" ${content.status eq "1"?"checked=\"checked\"":"" } />启用
			     <input type="radio" name="status" value="0" ${content.status eq "0"?"checked=\"checked\"":"" } />停用
			     <input type="radio" name="status" value="2" ${content.status eq "2"?"checked=\"checked\"":"" } />离职
			</td>
			
		</tr>
		<tr >
		    <td class="field" align="right">所属机构：</td>
			<td id="org_h">
			    <select id="orgId" class="tab_form_select" name="orgId" >
			    </select>
			</td>
			<td class="field" align="right">序号：</td>
			<td id="sortId_h" title="序号">
				<input type="text"  class="tab_form_txt" maxlength="10" name="sortId" value="${content.sortId eq null?100:content.sortId}" />
			</td>
		</tr>
		<tr>
			<td class="field" align="right">绑定IP</td>
			<td colspan="3" title="绑定IP" >
			    <input name="updateIps" type="hidden" id="updateIps"/>
			    <input name="deleteIps" type="hidden" id="deleteIps"/>
			    <input name="insertIps" type="hidden" id="insertIps"/>
			   <input type="checkbox" onclick="bindIp(this.checked)" value="1"  ${content.hasBindIp eq "1"?"checked=\"checked\"":"" } name="hasBindIp"/>
			</td>
		</tr>
	</table>
	<div id="ip_gride" style="height: 200px;">
		<div id="tools-bar">
				<div id="operate" class="tool_operate">
				  <input type="button" id="btns-add" class="add" onclick="addBindIp();" value="新增" />
			    </div>
		  </div>
		 <table id="datas-grid"></table>
	</div>
	</div>
	<div id="activityTable" class="activity_box">	
		<input type="button"  id="btn_save" onclick="save()" class="save" value="保&nbsp;&nbsp;存" />
		<input type="button"  id="btn_return" onclick="exit()" class="return" value="返&nbsp;&nbsp;回"/>
    </div>
</form>
</div>
</body>
<script>
	function bindIp(val){
		if(val){
			jQuery("#ip_gride").show();
			
			doSearch();
		}else{
			jQuery("#ip_gride").hide();
		}
	}
	
	function getAllORG(unitId){
			var para="";
			if(unitId){
				para=para+"?unitId="+unitId;
			}
			jQuery.ajax({
	            url: contextPath+"/control/system/org/getAllORG.action"+para,
	            type: "post",
	            async: "true",
	            dataType: "json",
	            success: function(data){
	            	var parentId = jQuery("#orgId");
	           		var ops = "";
	             	jQuery.each(data,function(key,val){
	             		if("${content.orgId}"==key ){
	             			ops = ops + "<option value='"+key+"' selected = 'selected'>"+val+"</option>";
	             		}else{
	             			ops = ops + "<option value='"+key+"'>"+val+"</option>";              			
	             		} 
	             	});
	             	parentId.html(ops);
	           }
	  		 });
		}


	function save(str){
		 var inserted = $("#datas-grid").datagrid('getChanges', "inserted");
	    // var rowstr = JSON.stringify(inserted);
	     
	     var inserted = $("#datas-grid").datagrid('getChanges', "inserted");
         var deleted = $("#datas-grid").datagrid('getChanges', "deleted");
         var updated = $("#datas-grid").datagrid('getChanges', "updated");
         var effectRow = new Object();
         if (inserted.length) {
        	 jQuery("#insertIps").val(JSON.stringify(inserted));
         }
         if (deleted.length) {
        	 jQuery("#deleteIps").val(JSON.stringify(deleted));
         }
         if (updated.length) {
        	 jQuery("#updateIps").val(JSON.stringify(updated));
         }

		jQuery("#formItem").submit();
		
	}
	
	function exit(){
		window.location.href="list.action";
	}
    function passwordChange(){
    	jQuery("#passwordChange").val("1");//密码已修改
    }
	jQuery(document).ready(function() {
		getAllORG();
		adjustDocumentLayout();
		toggleButton("button_act");
		bindIp(formItem.hasBindIp.checked);
	});
	function  getSelectRow(){
		return $('#datas-grid').datagrid('getRowIndex',$('#datas-grid').datagrid('getSelected'));
	}
	function getRowIndex(target){
		var tr = $(target).closest('tr.datagrid-row');
		return parseInt(tr.attr('datagrid-row-index'));
	}
	function saverow(target){
		$('#datas-grid').datagrid('endEdit', getRowIndex(target));
	    var rows = $("#datas-grid").datagrid('getChanges');
		//var queryString = "rows=" + rowstr;
	 }

	function cancelrow(target){
		$('#datas-grid').datagrid('cancelEdit', getRowIndex(target));
	}
	function editrow(target){
		$('#datas-grid').datagrid('beginEdit', getRowIndex(target));
	}
	function addBindIp(){
    	$('#datas-grid').datagrid('insertRow',{
    		index: 0,	// 索引从0开始
    		row: {}
    	  });
    	$('#datas-grid').datagrid('beginEdit',0);
   }
   function deleteRow(){
		  $.messager.confirm('确认','确认删除?',function(row){  
            if(row){  
                $('#datas-grid').datagrid('deleteRow',getSelectRow());
                
            }  
        });  
	}
	window.onresize=function(){
		//adjustDocumentLayout();
	};
</script>
</html>