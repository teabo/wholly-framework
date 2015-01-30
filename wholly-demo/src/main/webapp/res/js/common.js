var formid = "formList";
var newAction;
var editAction;
var lsAction;
var manageForm;
function dialogBinds(title, w, h) {
	var showTitle = "管理";
	if (title) {
		showTitle = title;
	}
	var dwidth = w && w > 0 ? w : 360;
	var dheight = h && h > 0 ? h : 200;
	$("#manage-dialog").dialog({
		title : showTitle,
		width : dwidth,
		height : dheight,
		closed : true,
		modal : true,
		buttons : "#manage-dialog-btns"
	});
	$("#manage-dialog-btns a").linkbutton();
	$('#manage-dialog').dialog({
		onClose : function() {
			$.messager.progress("close");
		}
	});
}

function saveButtonBinds() {
	if (window.setValidateFields) {
		setValidateFields();
	}
	
	$("#btns-save").bind("click", function() {
		if (window.save) {
			save();
		} else {
			$.messager.progress();
			if (manageForm.form("validate")) {
				manageForm.submit();
			} else {
				$.messager.progress("close");
			}
		}
	});
}

function addButtonBinds(url) {
	$("#btns-add").click(function() {
		if (window.add) {
			add(url);
		} else {
			$.messager.progress();
			doNew(url);
			$("#manage-dialog").dialog("close");
		}
	});
}

function editButtonBinds(url) {
	$("#btns-update").click(function() {
		if (window.edit) {
			edit(url);
		} else {
			$.messager.progress();
			var rows = $('#datas-grid').datagrid("getSelections");	//获取你选择的所有行
			if (rows === null || rows.length<=0) {
				$.messager.progress("close");
				$.messager.alert("提示","请选择记录",'warning');
			} else if (rows.length>1){
				$.messager.progress("close");
				$.messager.alert("提示","查看或编辑操作时，只允许选择一行记录",'warning');
			} else {
				var isCheckPass = true;
				if (window.checkEdit){
					isCheckPass=checkEdit();
				}
				if (isCheckPass==true){
					doEdit(rows[0].id, url);
					$("#manage-dialog").dialog("close");
				} else {
					$.messager.progress("close");
				}
			}
		}
	});
}

function delButtonBinds(url) {

	$("#btns-del").click(function() {
		var rows = $('#datas-grid').datagrid("getSelections");	//获取你选择的所有行
		if (rows === null || rows.length<=0) {
			$.messager.alert("提示","至少选中一行记录",'warning');
			$.messager.progress("close");
		} else {
			$.messager.confirm('提示', '你确定删除这些记录吗', function(r) {
				$.messager.progress("close");
				if (r) {
					 //循环所选的行
					var QString = "";
				    for(var i =0;i<rows.length;i++){    
				    	if (i>0){
				    		QString += "&";
				    	}
				    	QString += "_selects="+rows[i].id;
				    }
					$.post(url, QString, function(data) {
						if (data.code > 0) {
							message('删除成功!','ok');
						} else {
							message('删除失败!','no');
						}
						fnc_search(lsAction);
					}, 'json');

				}
			});

		}
	});
}

function doSearch(isGotoPage){
	fnc_search(lsAction, isGotoPage);
}

function fnc_search(url, isGotoPage) {
	if (window.fn_search) {
		fn_search();
	} else {
		var Pager = $('#datas-grid').datagrid('getPager');
		Pager.pagination('loading');
		//获取每一页的数量
		var options = Pager.data("pagination").options;
		var maxPage = options.total%options.pageSize==0?
				(options.total/options.pageSize):(Math.floor(options.total/options.pageSize)+1);
		if(!maxPage){
			maxPage=1;
		}
		var pageNumber = 1;
		var pageSize = options.pageSize;//每一页的数量
		var QString = "_pagelines="+ pageSize;
		if (isGotoPage){
			pageNumber = Math.floor($('.pagination-num').val());
			if(pageNumber>maxPage || pageNumber<1 || isNaN(pageNumber)){
				$.messager.alert("错误","页码输入有误，请输入“1 ~ "+maxPage+"”范围的整数。",'error');
				return;
			}
			$('.pagination-num').val(pageNumber);
		}
		QString = QString + "&_currpage=" + pageNumber;
		var params = $("#tools-bar :input").serialize();
		if (params){
			QString = QString + "&" + params;
		}
		Pager.pagination({ 
			pageNumber: pageNumber,
			pageSize:pageSize
			}); 
		$.post(url, QString, function(data) {
			if (data.code && data.code==0){
				$.messager.alert("错误",data.error,'error');
			} else {
				$("#datas-grid").datagrid('loadData', data);
			}
		}, "json");
		Pager.pagination('loaded');
	}

};

/** 检查元素是否包含特殊字符 ; 包含特殊字符,返回true;*/
function checkStr(str){
	var pat=new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、？]");
    if(pat.test(str))   
    {   
        return true;   
    } 
    return false;
}

function searchButtonBinds(url) {
	lsAction = url;
	$("#search").click(function(){
		fnc_search(lsAction);
	});
	$($("#datas-grid").datagrid('getPager')).pagination(
			{
				buttons:[{  
					text:'确定',  
			        handler:function(){  
			        	doSearch(true);
			        }  
			    }],
				onSelectPage : function(pageNumber, pageSize) {
					var QString = "_currpage=" + pageNumber + "&_pagelines=" + pageSize;
					QString += "&" + $("#tools-bar :input").serialize();
					$(this).pagination('loading');
					$.post(url, QString, function(data) {
						if (data.code && data.code==0){
							$.messager.alert("错误",data.error,'error');
						} else {
							$("#datas-grid").datagrid('loadData', data);
						}
					}, "json");
					$(this).pagination('loaded');
				},
				onBeforeRefresh : function(pageNumber, pageSize) {
					$(this).pagination('loading');
				},
				onRefresh : function() {
					$(this).pagination('loaded');
				}
			});
}

function doEdit(id, url){
	if (url){
		document.forms[0].action=url+"?id="+id;
	} else {
		document.forms[0].action= editAction+"?id="+id;
	}
	document.forms[0].submit();
}

function doNew(url){
	if (url){
		document.forms[0].action=url;
	} else {
		document.forms[0].action= newAction;
	}
	document.forms[0].submit();
}
function doDetail(id, url){
	if (url){
		document.forms[0].action=url+"?id="+id;
	} else {
		document.forms[0].action= detailAction+"?id="+id;
	}
	document.forms[0].submit();
}
function detailButtonBinds(url) {
	$("#btns-detail").click(function() {
		if (window.edit) {
			edit(url);
		} else {
			$.messager.progress();
			var rows = $('#datas-grid').datagrid("getSelections");	//获取你选择的所有行
			if (rows === null || rows.length<=0) {
				$.messager.progress("close");
				$.messager.alert("提示","请选择记录",'warning');
			} else if (rows.length>1){
				$.messager.progress("close");
				$.messager.alert("提示","查看或编辑操作时，只允许选择一行记录",'warning');
			} else {
				doDetail(rows[0].id, url);
				$("#manage-dialog").dialog("close");
			}
		}
	});
}

function Save_Excel(isFormat) {//导出Excel文件
    //获取Datagride的列
	//debugger;
	
	var columns = $('#datas-grid').datagrid("options").columns;
	removeCheckBox(columns[0]);//去掉复选框
	
    var rows = $('#datas-grid').datagrid('getRows');
    var dataGrid_columns = JSON.stringify(columns);
    if(isFormat==false){
	} else {
    	for(var i=0;i<columns[0].length;i++){
        	if(columns[0][i].formatter){
        		for(var j=0;j<rows.length;j++){
        			rows[j][columns[0][i].field]=format(rows[j],columns[0][i],j);
            	}
        	}
        }
    }
    var dataGrid_datas = JSON.stringify(rows);
    var exportform = document.getElementById("exportForm");
    if (!exportform){
    	var html = '<form id="exportForm" action="'+contextPath+'/export/excel.action" method="post"> ';
        html += '<input type="hidden" name="columns" id="columns" value=""/>';
        html += '<input type="hidden" name="datas" id="datas" value=""/></form>'; 
        $('body').append(html);
    }
    document.getElementById("columns").value=dataGrid_columns;
	document.getElementById("datas").value=dataGrid_datas;
	document.getElementById("exportForm").submit();
	
	return false;
}

function format(row,column,index){
	return column.formatter(row[column.field],row,index);
}

//去掉复选框
function removeCheckBox( array){
	//去掉复选框
	for ( var i = 0; i < array.length; i++) {
		if(array[i].field.toString()=="check" ){
			array.remove(i);
		}
	}
	
	//去掉菜单图标
	for ( var i = 0; i < array.length; i++) {
		if( array[i].field.toString()=="iconUrl"){
			array.remove(i);
		}
	}
}

Array.prototype.remove=function(dx)
{
	if(isNaN(dx)||dx>this.length){return false;}
	this.splice(dx,1);
};

var modalDialog = function(options) {
	var opts = $.extend({
		title : '&nbsp;',
		width : 640,
		height : 480,
		modal : true,
		onClose : function() {
			$(this).dialog('destroy');
		}
	}, options);
	opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
	if (options.url) {
		opts.content = '<iframe id="" src="' + options.url + '" allowTransparency="true" scrolling="auto" width="100%" height="98%" frameBorder="0" name=""></iframe>';
	}
	return $('<div/>').dialog(opts);
};

function message(msg, icon){
	$.msgbox.show({
	    message: msg,
	    icon: icon,
	    timeOut: 2000,
	    modal: false
	});
}

//调整窗口
function adjustDataIteratorSize() {
	
	var container = document.getElementById("container");
	var searchForm = document.getElementById("searchFormTable");
	var pageTable = document.getElementById("pageTable");
	var activityTable = document.getElementById("activityTable");
	var dataTable = document.getElementById("dataTable");
	var containerHeight = $(window).height() - 2;
	container.style.height = containerHeight + 'px';
	var dataTableHeight = containerHeight;
	if (activityTable) {
		dataTableHeight = dataTableHeight - activityTable.offsetHeight;
	}
	if (searchForm) {
		dataTableHeight = dataTableHeight - searchForm.offsetHeight;
	}
	if (pageTable) {
		dataTableHeight = dataTableHeight - pageTable.offsetHeight;
	}
	if($("#ptitle").attr("id")=="ptitle"){
		dataTableHeight = dataTableHeight - $("#ptitle").height();
	}
	dataTable.style.width = "100%";
	dataTable.style.height = dataTableHeight + 'px';
	container.style.visibility = "visible";
}

function currForm(formid){
	if (!formid) formid = "formList";
	var f = document.getElementById(formid);
	if (f)
		return f;
	else{
		var forms = document.getElementsByName(formid);
		if (forms)
			return forms[0];
	}
	return document.forms[0];
}

function resetAll2() {
	resetElements(document.forms[0].elements);
}

function resetAll() {
	var toolbar = document.getElementById("tools-bar");
	resetElements(toolbar.getElementsByTagName("INPUT"));
	resetElements(toolbar.getElementsByTagName("SELECT"));
}

function resetElements(elements){
	for (var i = 0; i < elements.length; i++) {
		var reset = elements[i].getAttribute("reset");
		if (reset && reset=='false') continue;
		var elementName = elements[i].name;
		if ((elementName.indexOf("sm_") > -1)
				|| (elementName.indexOf("d_") > -1)
				|| (elementName.indexOf("n_") > -1) 
				|| (elementName.indexOf("s_") > -1)
				|| (elementName.indexOf("sbe_") > -1)
				|| (elementName.indexOf("sse_") > -1)
		) {
			elements[i].value = "";
		} else if ( (elements[i].tagName.toUpperCase() == 'INPUT'
					&& elements[i].type.toUpperCase()!='HIDDEN'
					&& elements[i].type.toUpperCase()!='BUTTON' 
					&& elements[i].type.toUpperCase()!='SUBMIT')
				|| elements[i].tagName.toUpperCase() == 'SELECT'
		){
			elements[i].value = "";
		}
	}
}