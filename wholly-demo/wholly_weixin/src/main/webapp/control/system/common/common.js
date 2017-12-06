//调整窗口
function adjustDataIteratorSize() {
	
	var container = document.getElementById("container");
	var searchForm = document.getElementById("searchFormTable");
	var pageTable = document.getElementById("pageTable");
	var activityTable = document.getElementById("activityTable");
	var dataTable = document.getElementById("dataTable");
	var containerHeight = document.body.clientHeight - 4;
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
