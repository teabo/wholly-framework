var hkey_root, hkey_path, hkey_key;
hkey_root = "HKEY_CURRENT_USER";
// 地址的写法很严格的用双斜杠
hkey_path = "//Software//Microsoft//Internet Explorer//PageSetup";
hkey_MainPath = "//Software//Microsoft//Internet Explorer//Main";
hkey_BackgroundKey = "//Print_Background"; // 设置打印背景色 值为 yes或no

// 设置网页打印的页眉页脚为空
function pagesetup_null() {
	try {
		var RegWsh = new ActiveXObject("WScript.Shell");
		hkey_key = "//header";
		RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");
		hkey_key = "//footer";
		RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");

		// 不打印页面背景
		RegWsh.RegWrite(hkey_root + hkey_MainPath + hkey_BackgroundKey, "no");
	} catch (e) {
	}
}
// 设置网页打印的页眉页脚为默认值
function pagesetup_default() {
	try {
		var RegWsh = new ActiveXObject("WScript.Shell");
		hkey_key = "//header";
		RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&w&b,&p/&P");
		hkey_key = "//footer";
		RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&u&b&d");
		// 打印页面背景
		RegWsh.RegWrite(hkey_root + hkey_MainPath + hkey_BackgroundKey, "yes");
	} catch (e) {
	}
}
function printSetup() {
	var PrintControl = document.getElementById("PrintControl");
	PrintControl.execwb(8, 1); // 打印页面设置
}
function doPreview(obj) {
	pagesetup_null();
	//var PrintControl = document.getElementById("PrintControl");
	//PrintControl.execwb(7, 1);// 打印页面预览
	showNormalWindow(contextPath + '/print.jsp','_print',screen.width,screen.height,'');
}
function print(obj) {
	if (confirm('确定打印吗？')) {
		pagesetup_null();
		var PrintControl = document.getElementById("PrintControl");
		PrintControl.execwb(6, 1);
	}
}
