<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>系统配置信息</title>
<%@include file="/res/common/index.jsp"%>
<%
	String contextPath = request.getContextPath();

%>
<script src="<%=contextPath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=contextPath %>/res/js/dg_js.js"></script>
<script type="text/javascript" src="<%=contextPath %>/res/component/dialog/jquery.teabo.dialog.js"></script>
<style>
#container-wrapper {
	width: 100%;
	height: 100%;
	padding: 0;
	margin: 0;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
	$('#datas-grid').datagrid({
		fit:true,
		columns:[[
		    {field:'paramKey',title:'属性key', width:'250',align:'center',},
		    {field:'paramDesc',title:'属性名',width:'250',editor:'text',align:'center',},
		    {field:'paramValue',title:'属性值',width:'250',editor:'text',align:'center',},
		    {field:'check',title:'操作',width:'100',align:'center',
		    	formatter:function(value,row,index){
		    		if(row.editing){
		    			var s = '<a href="#" onclick="saverow('+index+')">保存</a>';
		    			var c = '<a href="#" onclick="cancelrow('+index+')">取消</a>';
		    			return s+" "+c;
		    		}else{
		    			var e = '<a href="#" onclick="editrow('+index+')">修改</a>';
		    			return e;
		    		}
		    	}
		    }
		]],
		striped : true,
		rownumbers : true,
		singleSelect : false,
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
	$("#container-wrapper").resize(function() {
		$("#datas-grid").datagrid("resize");
	});
	
	searchButtonBinds('${pageContext.request.contextPath}/control/system/context/jsonList.action'); //绑定查询按钮
	
	
	$('#superuser_list').css({height:$(window).height()});
	
	/* $('#datas-grid').pagination({ 
		total:2000, 
		pageSize:10 
		}); */ 

	doSearch();
});

function updateActions(index){
	$('#datas-grid').datagrid('updateRow',{
		index: index,
		row:{}
	});
}

function getRowIndex(target){
	var tr = $(target).closest('tr.datagrid-row');
	return parseInt(tr.attr('datagrid-row-index'));
}

function editrow(index){
	$('#datas-grid').datagrid('beginEdit', index);
}

function saverow(index){
	//$('#datas-grid').datagrid('endEdit', getRowIndex(target));
	$('#datas-grid').datagrid('endEdit', index);
	//如果调用acceptChanges(),使用getChanges()则获取不到编辑和新增的数据。
	 
    //使用JSON序列化datarow对象，发送到后台。
    //var rows = $("#datas-grid").datagrid('getChanges');
	//var rowstr = JSON.stringify(rows);
	
	var row = $('#datas-grid').datagrid('getData').rows[index];
	var rows = new Array();
	rows.push(row);
	
	var str = JSON.stringify(rows);
	//var queryString = "rows=" + rowstr;
	var queryString = "rows=" + str;
	$.post("<%=contextPath%>/control/system/context/saveRow.action",queryString,
         function(data){
        	
       	},"json");
}

function cancelrow(index){
	$('#datas-grid').datagrid('cancelEdit', index);
}

/* function insert(){
	$('#datas-grid').datagrid('appendRow',{});
	var row = $('#datas-grid').datagrid('getSelected');
	alert(row);
	if (row){
	var index = $('#datas-grid').datagrid('getRowIndex', row);
	} else {
	index = 0;
	}
	$('#datas-grid').datagrid('insertRow', {
		index: index,
		row:{
			--status:'P'
		}
	});
	$('#datas-grid').datagrid('selectRow',index);
	$('#datas-grid').datagrid('beginEdit',index);
} */

</script>
</head>
<body id="superuser_list">
<div id="container-wrapper">
<form id="formList" name="formList" method="post">

	<div id="tools-bar">
	    <div class="tab_form">
	    <table style="width:90%;margin:0 auto;">
			    <tr>
			    	<td style="text-align:right;"><b class="tool_box_b">属性key：</b></td>
			    	<td><input type="text" class="tab_form_txt" name="sm_param_key" id="sm_param_key"/></td>
			    	<td style="text-align:right;"><b class="tool_box_b">属性名：</b></td>
			    	<td><input id="sm_param_desc"  name="sm_param_desc" class="tab_form_txt"></select></td>
			    </tr>
			    <tr>
					<td colspan="4" style="text-align:center;">
						<input type="button" class="btn" value="查&nbsp;&nbsp;询" id="search"/>
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
