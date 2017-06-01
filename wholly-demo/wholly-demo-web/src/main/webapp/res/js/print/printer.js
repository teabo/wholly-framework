var LODOP;
function printSetup(obj, x, y, width, height) {
	CreateOneFormPage(obj, x, y, width, height);
	LODOP.PRINT_SETUP(); // 打印页面设置
}
function preview(obj, x, y, width, height) {
	CreateOneFormPage(obj, x, y, width, height);
	LODOP.PREVIEW(); // 打印页面预览
}
function print(obj, x, y, width, height) {
	if (confirm('确定打印吗？')) {
		CreateOneFormPage(obj, x, y, width, height);
		LODOP.PRINT();
	}
}

function CreateOneFormPage(obj, x, y, width, height) {
	LODOP = getLodop(document.getElementById('LODOP'), document
			.getElementById('LODOP_EM'));
	LODOP.PRINT_INIT("打印");
	LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Auto-Width");
	LODOP.SET_PRINT_STYLE("FontSize", 9);
	LODOP.SET_PRINT_STYLE("Bold", 1);// commFont
	var strBodyStyle = "<style>button {display: none;}input {display: none;}</style>";
	var strFormHtml = strBodyStyle;
	if (obj) {
		strFormHtml += "<body style='padding: 0; margin: 0;'>" + obj.innerHTML
				+ "</body>";
	} else {
		strFormHtml += document.documentElement.innerHTML;
	}
	if (x == null || x == undefined) {
		x = 50;
	}
	if (y == null || y == undefined) {
		y = 45;
	}
	if (width == null || width == undefined) {
		width = "78%";
	}
	if (height == null || height == undefined) {
		height = "100%";
	}
	LODOP.ADD_PRINT_HTM(y, x, width, height, strFormHtml);
}

function doPreview(url) {
	if (url) {
	} else {
		url = 'print.action';
	}
	var wkimid = document.getElementById("sys_workItemId").value;
	var procid = document.getElementById("processid").value;
	if (url.indexOf("?") != -1) {
		url += "&";
	} else {
		url += "?";
	}
	url += 'content.processid=' + procid + '&content.sys_workItemId=' + wkimid
			+ '&id=' + document.getElementById("id").value+'&content.sys_ShowType=history';
	showNormalWindow(url, '打印', screen.width, screen.height, '');

}


function doPreviewOne(url) {
	if (url) {
	} else {
		url = 'printOne.action';
	}
	var id = document.getElementById("id").value;
	if (url.indexOf("?") != -1) {
		url += "&";
	} else {
		url += "?";
	}
	url += 'content.id=' + id;
	showNormalWindow(url, '打印', screen.width, screen.height, '');
}

function doPreviewTwo(url) {
	if (url) {
	} else {
		url = 'printTwo.action';
	}
	var djrq = document.getElementById("djrq").value;
	var gcdd = document.getElementById("gcdd").value;
	var jld = document.getElementById("jld").value;
	if (url.indexOf("?") != -1) {
		url += "&";
	} else {
		url += "?";
	}
	url += 'djrq=' + djrq + '&gcdd=' + gcdd + '&jld=' + jld;
	showNormalWindow(url, '打印', screen.width, screen.height, '');
}

function doPreviewThree(url) {
	if (url) {
	} else {
		url = 'printThree.action';
	}
	var zh = document.getElementById("zh").value;
	var gcrq = document.getElementById("gcrq").value;
	var sm_bdmc = document.getElementById("sm_bdmc").value;
	if (url.indexOf("?") != -1) {
		url += "&";
	} else {
		url += "?";
	}
	url += 'zh=' + zh + '&gcrq=' + gcrq + '&sm_bdmc=' + sm_bdmc;
	showNormalWindow(url, '打印', screen.width, screen.height, '');
}

function doPreviewFour(url){
	if (url) {
	} else {
		url = 'printFour.action';
	}
	var id = document.getElementById("id").value;
	if (url.indexOf("?") != -1) {
		url += "&";
	} else {
		url += "?";
	}
	url += 'id=' + id;
	showNormalWindow(url, '打印', screen.width, screen.height, '');
}
function doPreviewFive(url){
	if (url) {
	} else {
		url = 'printFive.action';
	}
	var djrq = document.getElementById("startTime").value;
	if (url.indexOf("?") != -1) {
		url += "&";
	} else {
		url += "?";
	}
	url += 'djrq=' + djrq;
	showNormalWindow(url, '打印', screen.width, screen.height, '');
}
