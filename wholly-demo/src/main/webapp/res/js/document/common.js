function adjustDocumentLayout(){
		var container = document.getElementById("container");
		var activityTable = document.getElementById("activityTable");
		var contentTable = document.getElementById("contentTable");
		var containerHeight = $(window).height() - 2;
		if (containerHeight>0){
			container.style.height = containerHeight + 'px';
			var contentTableHeight = containerHeight;
			if (activityTable) {
				contentTableHeight = contentTableHeight - activityTable.offsetHeight;
			}
			contentTable.style.width = "100%";
			contentTable.style.height = contentTableHeight + 'px';
		}
		container.style.visibility = "visible";
}

function currForm(formid){
	if (!formid) formid = "formItem";
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

function dosubmit(url) {
	var isValid = true;
	if (window.checkStringAsDefault) {
		isValid = checkStringAsDefault();
	}
	if (isValid){
		var formItem = document.getElementById("formItem");
		if (url == "saveAndNew") {
			formItem.action = savaAndNewAction;
		}
		formItem.action=formItem.action+".action";
		toggleButton("button_act");
		formItem.submit();
	}
}

function doexit() {
	toggleButton("button_act");
	var oBackURL = document.getElementsByName("_backURL")[0];
	
	var backurl;
	if (oBackURL && oBackURL.value) {
		backurl = oBackURL.value;
	}else{
		backurl = listAction;
	}
	var params = serialize('searchFormTable', 'input');
	if (backurl.indexOf("?") != -1) {
		var subURL=backurl.substring(0,backurl.indexOf("?"))+".action";
		backurl=subURL+backurl.substring(backurl.indexOf("?"),backurl.length);
		if (params) { // 没有查询表单
			backurl += "&" + params;
		}
	} else {
		backurl+=".action";
		if (params) {
			backurl += "?" + params;
		}
	}
	window.location = backurl;
}
//导入
function showImportXsl(){
	toggleButton("button_act");
	var form = document.forms[0];
	
	var url=contextPath+"/portal/xls/showImporXlsDiv.action";
	CEISO.dialog.show( {
		width : 600,
		height : 200,
		url : url,
		title : '导入xls文件',
		close : function(result) {
		
			if (result){
				var fm = document.forms[0];
				fm.action+="?type=import";
				fm.submit();
			}
			toggleButton("button_act");
		}
	});
	return false;
	
}
//去所有空格   
String.prototype.trimAll = function() {
	return this.replace(/(^\s*)|(\s*)|(\s*$)/g, "");
};