var listAction = null;
function selectAll(b, fieldname) {
	var fname = '_selects';
	if (fieldname){
		fname = fieldname;
	}
	var c = document.getElementsByName(fname);
	if (c == null)
		return;

	if (c.length != null) {
		for (var i = 0; i < c.length; ++i)
			c[i].checked = b && !(c[i].disabled);
	} else {
		c.checked = b;
	}
}

function selectAllByField(b, fieldName) {
	var c = document.getElementsByName(fieldName);

	if (c == null)
		return;

	if (c.length != null) {
		for (var i = 0; i < c.length; ++i)
			c[i].checked = b && !(c[i].disabled);
	} else {
		c.checked = b;
	}
}

function showFirstPage(FO) {
	if (FO == null) {
		FO = document.forms;
	}
	if (isNaN(parseInt(FO[0]._currpage.value))
			|| isNaN(parseInt(FO[0]._pagelines.value))
			|| isNaN(parseInt(FO[0]._rowcount.value))) {
		return;
	}

	var pageCount = Math.ceil(parseInt(FO[0]._rowcount.value)
			/ parseInt(FO[0]._pagelines.value));

	if (pageCount > 1) {
		if (listAction) {
			FO[0].action = listAction;
		}
		FO[0]._currpage.value = 1;
		FO[0].submit();
	}
}

function showPreviousPage(FO) {
	if (FO == null) {
		FO = document.forms;
	}
	if (isNaN(parseInt(FO[0]._currpage.value))
			|| isNaN(parseInt(FO[0]._pagelines.value))
			|| isNaN(parseInt(FO[0]._rowcount.value))) {
		return;
	}

	var pageNo = parseInt(FO[0]._currpage.value);
	// var pageCount = Math.ceil(parseInt(FO._rowcount.value) /
	// parseInt(FO._pagelines.value));

	if (pageNo > 1) {
		if (listAction) {
			FO[0].action = listAction;
		}
		FO[0]._currpage.value = pageNo - 1;
		FO[0].submit();
	}
}

function showNextPage(FO) {
	if (FO == null) {
		FO = document.forms;
	}
	if (isNaN(parseInt(FO[0]._currpage.value))
			|| isNaN(parseInt(FO[0]._pagelines.value))
			|| isNaN(parseInt(FO[0]._rowcount.value))) {
		return;
	}
//	 alert(FO[0]._currpage.value);
	var pageNo = parseInt(FO[0]._currpage.value);
	var pageCount = Math.ceil(parseInt(FO[0]._rowcount.value)
			/ parseInt(FO[0]._pagelines.value));

	if (pageCount > 1 && pageCount > pageNo) {
		if (listAction) {
			FO[0].action = listAction;
		}
		FO[0]._currpage.value = pageNo + 1;
		FO[0].submit();
	}
}

function showLastPage(FO) {
	if (FO == null) {
		FO = document.forms;
	}
	// alert(FO);
	if (isNaN(parseInt(FO[0]._currpage.value))
			|| isNaN(parseInt(FO[0]._pagelines.value))
			|| isNaN(parseInt(FO[0]._rowcount.value))) {
		return;
	}

	// var pageNo = parseInt(FO._currpage.value);
	var pageCount = Math.ceil(parseInt(FO[0]._rowcount.value)
			/ parseInt(FO[0]._pagelines.value));

	if (pageCount > 0) {
		if (listAction) {
			FO[0].action = listAction;
		}
		FO[0]._currpage.value = pageCount;
		FO[0].submit();
	}
}


function changeLines(FO){
	if (FO == null) {
		FO = document.forms;
	}
	if (listAction) {
		FO[0].action = listAction;
	}
	FO[0]._currpage.value = '1';
	FO[0]._pagelines.value = document.getElementsByName("_lines")[0].value;
	FO[0].submit();
}

function jumpPage(FO) {
	if (FO == null) {
		FO = document.forms;
	}

	if (isNaN(parseInt(FO[0]._currpage.value))
			|| isNaN(parseInt(FO[0]._pagelines.value))
			|| isNaN(parseInt(FO[0]._rowcount.value))) {
		return;
	}
	var pageNo = parseInt(FO[0]._jumppage.value);
	var pageCount = Math.ceil(parseInt(FO[0]._rowcount.value)
			/ parseInt(FO[0]._pagelines.value));
	if (pageNo > pageCount) {
		alert(" 输入参数有误!");
		return;
	}
	if (pageCount > 1 && pageCount >= pageNo) {
		if (listAction) {
			FO[0].action = listAction;
		}
		FO[0]._currpage.value = pageNo;
		FO[0].submit();
	}
}

function resetPage(FO) {
	if (FO == null) {
		FO = document.formList;
	}
	FO[0]._currpage.value = '1';
}

function resetForm(FO) {
	if (FO == null) {
		FO = document.formList;
	}
	FO[0]._orderby.value = '';
	FO[0]._desc.value = '';
	FO[0]._currpage.value = '1';
}

function onEnterPress(e){
	var event = window.event || e;
	var keyCode = event.keyCode || event.which;
	if (keyCode==13){
		jumpPage();
	}
}

function resetAll() {
	var elements = document.forms[0].elements;
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
function resetQuery(clearName){
	var name;
	if(clearName){
		name = document.getElementsByName(clearName)[0];
	}else{
		name = document.getElementsByName("sm_name")[0];
	}
	name.value="";
}
function selectAllField(fieldName, b) {
	var c = document.getElementsByName(fieldName);
	if (c == null)
		return;

	if (c.length != null) {
		for (var i = 0; i < c.length; ++i)
			c[i].checked = b && !(c[i].disabled);
	} else {
		c.checked = b;
	}
}
//10.12.26 jack add param of "msg" for internationalization question of window tips
function isSelectedOne(fldName,msg) {
	var c = document.getElementsByName(fldName);
	var flag = false;
	if (c && c.length > 0) {
		for (var i = 0; i < c.length; i++) {
			if (c[i].checked) {
				flag = true;
				break;
			}
		}
	}

	if (!flag) {
		alert(msg);
		return false;
	}else{
		if(window.confirm("确定要删除记录吗？"))   {
			return true;
		}else{
			return false;
		}
	}
}

function isSelectedTwo(fldName,msg) {
	var c = document.getElementsByName(fldName);
	var flag = false;
	if (c && c.length > 0) {
		for (var i = 0; i < c.length; i++) {
			if (c[i].checked) {
				flag = true;
				break;
			}
		}
	}

	if (!flag) {
		alert(msg);
		return false;
	}else{
		if(window.confirm("确定要恢复记录吗？"))   {
			return true;
		}else{
			return false;
		}
	}
}

function isSelectedThree(fldName,msg) {
	var c = document.getElementsByName(fldName);
	var flag = false;
	if (c && c.length > 0) {
		for (var i = 0; i < c.length; i++) {
			if (c[i].checked) {
				flag = true;
				break;
			}
		}
	}

	if (!flag) {
		alert(msg);
		return false;
	}else{
		return true;
	}
}

function isSelectedGetShowMsg(fldName,msg,showmsg,isshow) {
	var c = document.getElementsByName(fldName);
	var flag = false;
	if (c && c.length > 0) {
		for (var i = 0; i < c.length; i++) {
			if (c[i].checked) {
				flag = true;
				break;
			}
		}
	}

	if (!flag) {
		alert(msg);
		return false;
	}else{
		if(isshow)
		{
			if(window.confirm(showmsg))   {
				return true;
			}else{
				return false;
			}
		}
	}
}

/**判断是否选中***/
function selective(fldName,msg) {
	var c = document.getElementsByName(fldName);
	var flag = false;
	if (c && c.length > 0) {
		for (var i = 0; i < c.length; i++) {
			if (c[i].checked) {
				flag = true;
				break;
			}
		}
	}
	if (!flag) {
		alert(msg);
		return false;
	}else{
		return true;
	}
}


/*
 * 修饰table js方法
 * 1、隔行变色 2、mouseover变色
 * */
function cssListTable(){
	jQuery("#contentTable tr").css("background-color","white");
	jQuery("#contentTable tr:even").css("background-color","#EEF0F2");
	jQuery("#contentTable tr").mouseover(function(){
		jQuery(this).addClass("mouseontr");
	}).mouseout(function(){
		jQuery(this).removeClass("mouseontr");
	});
}

/*
 * 初始化list div的位置
 * */
function initWinPosition(){
	if(jQuery("#tabContainerid")){
		jQuery("#tabContainerid").css("width",jQuery(document).width());
		jQuery("#tabContainerid").css("height",jQuery(document).height());
	}
	if(jQuery("#activityTable")){
		jQuery("#activityTable").css("width",jQuery("#tabContainerid").width());
	}
}

function checkbackURL(){
	resetBackURL();
	return true;
}

/**
 * 重新设置BackURL
 */
function resetBackURL() {
	var oBackURL = document.getElementsByName("_backURL")[0];
	if (oBackURL && oBackURL.value) {
		// 序列化查询表单字段
		var params = Form
				.serializeElements(
						$$('#searchFormTable input, #searchFormTable select, #searchFormTable textarea'),
						false); // prototype.js
		oBackURL.value = document.forms[0].action;
		if (params) { // 没有查询表单
			oBackURL.value += "?" + params;
		}
	}
}