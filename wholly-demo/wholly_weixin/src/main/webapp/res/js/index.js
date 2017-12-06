var window_height = document.documentElement.clientHeight-129;
var currentStatus = {};
var currentTab = {};
var initTabs = {};
var current_tabsId;
function initNewTab(nodeData, tabs_id) {
	if (!tabs_id) {
		tabs_id = "main-tab";
	}
	current_tabsId = tabs_id;
	var mainTab = $('#' + tabs_id);
	if (nodeData != null) {
		var currTitle = currentTab[tabs_id];
		if (!currTitle){
			mainTab.tabs({
				onSelect : function(title) {
					updateTab(title, tabs_id);
				}
			});
			currentTab[tabs_id] = true;
		}
		
		if (mainTab.tabs("getTab", nodeData.text) === null) {
			var tab_id = nodeData.id + "_tab";
			currentStatus[tab_id] = {'status':'new'};
			mainTab.tabs("add",
							{
								id : tab_id,
								title : nodeData.text,
								content : '<div id="'
										+ nodeData.id
										+ '_content" style="padding: 0;margin: 0;"><iframe class="center_iframe" id="'
										+ nodeData.id
										+ '_frm" scrolling="auto" frameborder="0"  src="'
										+ contextPath
										+ nodeData.href
										+ '" style="width:100%;height:'+window_height+'px;padding: 0;margin: 0;"></iframe></div>',
								fit:true,
								closable : true
							}).tabs("getSelected");
			
		} else {
			mainTab.tabs("select", nodeData.text);
		}
	}
}

function updateTab(title, tabs_id){
	if (!tabs_id) {
		tabs_id = "main-tab";
	}
	var mainTab = $('#' + tabs_id);
	var currTitle = currentTab[tabs_id];
	if (currTitle){
		if (currTitle == title) return;
		currentTab[tabs_id] = title;
		var refresh_tab = mainTab.tabs('getTab', title);
		if(refresh_tab && refresh_tab.find('iframe').length > 0){
			var options = refresh_tab.panel("options");
			//dump_obj(options);
			if (currentStatus[options.id].status == 'new'){
				currentStatus[options.id].status = 'update';
			} else {
				mainTab.tabs('update', {
					tab : refresh_tab,
					options : {
						selected : true
					}
				});
			}
		}
	} else {
		currentTab[tabs_id] = title;
	}
}

function doRefresh() {
	if (!parent.current_tabsId) {
		parent.current_tabsId = "main-tab";
	}
	var mainTab = parent.$('#' + parent.current_tabsId);
	mainTab.tabs("select", "首页");
}

function dump_obj(myObject) {  
	var s = "";
	var i = 0;
	for (var property in myObject) {
		if (i>=10){
			alert(s);
			s = ""; i=0;
		}
		s = s + "\n "+property +": " + myObject[property] ;  
		i++;
	}  
	alert(s);  
}  