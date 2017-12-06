/**
 * 编辑
 */
function doEdit(id, QString) {
	view(EditActionURL, id, QString);
}
/**
 * 查看
 */
function doView(id, QString) {
	view(ViewActionURL, id, QString);
}

function view(url, id, QString) {
	if (url) {
		if (url.indexOf("?") == -1) {
			url += '?id=' + id;
		} else {
			url += '&id=' + id;
		}
		if (QString) {
			url += "&" + QString;
		}
		document.forms[0].action = url;
		document.forms[0].submit();
	}
}

function doDelete(){
    if(isSelectedOne("_selects","请选择！")){
    	document.forms[0].action=DelActionURL;
    	document.forms[0].submit();
    }
}

function doNew(){
	document.forms[0].action=NewActionURL;
	document.forms[0].submit();
}