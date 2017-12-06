/**
 * 提示信息.
 * 
 * User: chris hsu Date: 2014-03-06
 * 
 * @author chris hsu
 */
(function($) {
var currentPath=function(){
	var url = $("script[src$='jquery.teabo.dialog.js']").src;
	if (url) {
		return url.substring(0,url.lastIndexOf('jquery.teabo.dialog.js'));
	}
	return contextPath + '/res/component/dialog/';
};		
// 定义整个Dialog对象
$.fn.Dialog = (function() {

	var Dialog=function( selector, context ){
		return new Dialog.fn.init(selector, context);
	},
	IMGFOLDERPATH,
	CONTEXT_PATH = '',
	DIALOG_OPTIONS = {
		ID : "dialog0001",
		isModal : true,
		width : 400,
		height : 300,
		Top : 0,
		Left : 0,
		ParentWindow : null,
		onLoad : null,
		Window : null,
		title : "",
		url : null,
		innerHTML : null,
		innerElementId : null,
		DialogArguments : {},
		WindowFlag : false,
		message : null,
		messageTitle : null,
		showMessage : false,
		showButton : true,
		Icon : null,
		bgdivID : null
	},options = {},_Array=new Array(),result,OKButton,CancelButton,
	isIE = navigator.userAgent.toLowerCase().indexOf("msie") != -1,
	isIE6 = navigator.userAgent.toLowerCase().indexOf("msie 6.0") != -1,
	isGecko = navigator.userAgent.toLowerCase().indexOf("gecko") != -1,
	isQuirks = document.compatMode == "BackCompat",
	Drag={},_DialogInstance,Page={};
	
	Dialog.fn = Dialog.prototype={
		constructor:Dialog,
		init : function(configs, context, rootDialog) {
			if (typeof configs === "string") {
				if (!configs) {
					configs = {
							ID : configs
					};
				}
			} else {
				if (!configs){
					options = $.extend({}, DIALOG_OPTIONS, {ID : new Date().getTime()});
					return this;
				} else if (context){
					options = configs;
					return this;
				} else if (!configs.ID) {
					configs.ID = new Date().getTime();
				}
			}
			options = $.extend({}, DIALOG_OPTIONS, configs || {});
			
			return $.Dialog(options, $);
		}
	};
	Dialog.fn.init.prototype = Dialog.fn;
	Dialog.extend = Dialog.fn.extend = function() {
		var options, name, src, copy, copyIsArray, clone,
		target = arguments[0] || {},
		i = 1,
		length = arguments.length,
		deep = false;

		// Handle a deep copy situation
		if ( typeof target === "boolean" ) {
			deep = target;
			target = arguments[1] || {};
			// skip the boolean and the target
			i = 2;
		}

		// Handle case when target is a string or something (possible in deep copy)
		if ( typeof target !== "object" && !$.isFunction(target) ) {
			target = {};
		}

		// extend $ itself if only one argument is passed
		if ( length === i ) {
			target = this;
			--i;
		}

		for ( ; i < length; i++ ) {
			// Only deal with non-null/undefined values
			if ( (options = arguments[ i ]) != null ) {
				// Extend the base object
				for ( name in options ) {
					src = target[ name ];
					copy = options[ name ];

					// Prevent never-ending loop
					if ( target === copy ) {
						continue;
					}

					// Recurse if we're merging plain objects or arrays
					if ( deep && copy && ( $.isPlainObject(copy) || (copyIsArray = $.isArray(copy)) ) ) {
						if ( copyIsArray ) {
							copyIsArray = false;
							clone = src && $.isArray(src) ? src : [];

						} else {
							clone = src && $.isPlainObject(src) ? src : {};
						}

						// Never move original objects, clone them
						target[ name ] = $.extend( deep, clone, copy );

						// Don't bring in undefined values
					} else if ( copy !== undefined ) {
						target[ name ] = copy;
					}
				}
			}
		}

		// Return the modified object
		return target;
	};
	
	Dialog.extend({
		inits : function(configs) {
			if (!configs) return;
			if (typeof configs === "string") {
				if (!configs) {
					configs = {
						ID : configs
					};
				}
			} else {
				if (!configs.ID) {
					configs.ID = new Date().getTime();
				}
			}
			options = $.extend({}, DIALOG_OPTIONS, configs || {});
			
			IMGFOLDERPATH = currentPath() + "images/";
		},
		getId : function(){
			options = this.options || options;
			return options.ID;
		},
		getTitle : function(){
			options = this.options || options;
			return options.title;
		},
		getOptions : function(){
			return this.options || options;
		},
		OKButton : function(){
			return OKButton;
		},
		CancelButton : function(){
			return CancelButton;
		},
		getTopLevelWindow : function() {
			//alert(1)window.top
			var win;
			if(window.parent.parent){
				win = window.parent.parent;
				try{
					win.document;
					return win;
				} catch (ex){}
				
			}
			if(window.parent){
				win = window.parent 
				try{
					win.document;
					return win;
				} catch (ex){}
			}
			win = window.top
			try{
				win.document;
				return win;
			} catch (ex){
				return window;
			}
		},
		DM : function(ele) {
			  if (typeof(ele) == 'string'){
				  
				var TopWindow = this.getTopLevelWindow();
				ele = TopWindow.document.getElementById(ele);
				//console.log(ele);
				if(!ele){
					return null;
				}
			  }
			  return ele;
		},
		TG : function(tagName,ele){
			ele = this.DM(ele);
			ele = ele || document;
			var ts = ele.getElementsByTagName(tagName);//此处返回的不是数组
			var arr = [];
			var len = ts.length;
			for(var i=0;i<len;i++){
				arr.push(this.DM(ts[i]));
			}
			return arr;
		},
		pushArray : function(e, win){
			if (win){
				if (win.EDialog){
					win.EDialog.pushArray(e);
				} else {
					if (win._Array==undefined){
						win._Array = new Array();
					}
					win._Array.push(e);
				}
			} else {
				_Array.push(e);
			}
		},
		getArray : function(win){
			if (win){
				if (win.EDialog){
					return win.EDialog.getArray();
				} else {
					return win._Array;
				}
			} else {
				return _Array;
			}
		},
		size : function(win){
			return this.getArray(win).length;
		},
		remove : function(id, win){
			this.getArray(win).removeDialog(id);
		},
		getDialog : function(dlg_id){
			var pw = EDialog.getTopLevelWindow();
			var _Array = EDialog.getArray(pw);
			if (_Array && _Array.length>0){
				if (dlg_id){
					return _Array.getDialog(dlg_id);
				} else {
					return _Array[_Array.length-1];
				}
			}
			return null;
		},
		getTopOne : function(win){
			var _Array = this.getArray(win);
			if (_Array && (_Array.length>1)) {
				return _Array[_Array.length-2];
			}
			return null;
		},
		stopEvent : function(event){//阻止一切事件执行,包括浏览器默认的事件
			event = window.event||event;
			if(!event){
				return;
			}
			if(isGecko){
				if (!event.defaultPrevented){
					var prevent = event.preventDefault; // 引用默认preventDefault
			        event.preventDefault = function() { //重写preventDefault
			          this.defaultPrevented = true;
			          prevent.call(this);
			        };
				}
				event.stopPropagation();
			}
			event.cancelBubble = true;
			event.returnValue = false;
		},
		hideE : function(ele) {
			ele = ele || this;
			ele = this.DM(ele);
			if (ele.style) ele.style.display = 'none';
		},
		showE : function(ele) {
			ele = ele || this;
			ele = this.DM(ele);
		    if (ele.style) ele.style.display = '';
		},
		visibleE : function(ele) {
			ele = ele || this;
			ele = this.DM(ele);
			if(ele.style.display=="none"){
				return false;
			}
		  return true;
		},
		show : function(configs) {
			this.inits(configs);
			var pw = this.getTopLevelWindow();
			//console.log(pw);
			var doc = pw.document;
			var cw = doc.compatMode == "BackCompat"?doc.body.clientWidth:doc.documentElement.clientWidth;
			var ch = doc.compatMode == "BackCompat"?doc.body.clientHeight:doc.documentElement.clientHeight;// 必须考虑文本框处于页面边缘处，控件显示不全的问题
			var sl = Math.max(doc.documentElement.scrollLeft, doc.body.scrollLeft);
			var st = Math.max(doc.documentElement.scrollTop, doc.body.scrollTop);// 考虑滚动的情况
			var sw = Math.max(doc.documentElement.scrollWidth, doc.body.scrollWidth);
			var sh = Math.max(doc.documentElement.scrollHeight, doc.body.scrollHeight);// 考虑滚动的情况
			sw=Math.max(sw,cw);
			sh=Math.max(sh,ch);
			if ( !options.ParentWindow ){
				options.ParentWindow = window ;
			}
			options.DialogArguments._DialogInstance = this;
			options.DialogArguments.ID = options.ID;

			if(!options.height){
				options.height = options.width/2;
			}

			if(options.Top==0){
				options.Top = (ch - options.height - 30) / 2 + st - 8;
			}
			if(options.Left==0){
				options.Left = (cw - options.width - 12) / 2 +sl;
			}
			if(options.showButton){// 按钮行高36
				options.Top -= 18;
			}
			if(options.WindowFlag){
				this.showWindow();
				return;
			}
			var dlg_id = options.ID;
			var arr = [];
			arr.push("<table style='-moz-user-select:none;' oncontextmenu='EDialog.stopEvent(event);' onselectstart='EDialog.stopEvent(event);' border='0' cellpadding='0' cellspacing='0' width='"+(options.width+26)+"'>");
			arr.push("  <tr style='cursor:move;' id='_draghandle_"+dlg_id+"'>");
			arr.push("    <td width='13' height='33' style=\"background-image:url("+IMGFOLDERPATH+"dialog_lt.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_lt.png', sizingMethod='crop');\"><div style='width:13px;'></div></td>");
			arr.push("    <td height='33' style=\"background-image:url("+IMGFOLDERPATH+"dialog_ct.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_ct.png', sizingMethod='crop');\"><div style=\"float:left;font-weight:bold; color:#FFFFFF; padding:9px 0 0 4px;\"><img src=\""+IMGFOLDERPATH+"icon_dialog.gif\" align=\"absmiddle\">&nbsp;"+options.title+"</div>");
			arr.push("      <div style=\"position: relative;cursor:pointer; float:right; margin:5px 0 0; _margin:4px 0 0;height:17px; width:28px; background-image:url("+IMGFOLDERPATH+"dialog_closebtn.gif)\" onMouseOver=\"this.style.backgroundImage='url("+IMGFOLDERPATH+"dialog_closebtn_over.gif)'\" onMouseOut=\"this.style.backgroundImage='url("+IMGFOLDERPATH+"dialog_closebtn.gif)'\" drag='false' onClick=\"EDialog.getDialog('"+dlg_id+"').CancelButton().onclick.apply(EDialog.getDialog('"+dlg_id+"').CancelButton(),[]);\"></div></td>");
			arr.push("    <td width='13' height='33' style=\"background-image:url("+IMGFOLDERPATH+"dialog_rt.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_rt.png', sizingMethod='crop');\"><div style=\"width:13px;\"></div></td>");
			arr.push("  </tr>");
			arr.push("  <tr drag='false'><td width='13' style=\"background-image:url("+IMGFOLDERPATH+"dialog_mlm.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_mlm.png', sizingMethod='crop');\"></td>");
			arr.push("    <td align='center' valign='top'><a href='#;' id='_forTab_"+dlg_id+"'></a>");
			arr.push("    <table width='100%' border='0' cellpadding='0' cellspacing='0' bgcolor='#FFFFFF'>");
			arr.push("        <tr id='_MessageRow_"+dlg_id+"' style='display:none'>");
			arr.push("          <td height='50' valign='top'><table id='_MessageTable_"+dlg_id+"' width='100%' border='0' cellspacing='0' cellpadding='8' style=\" background:#EAECE9 url("+IMGFOLDERPATH+"dialog_bg.jpg) no-repeat right top;\">");
			arr.push("              <tr><td width='25' height='50' align='right'><img id='_MessageIcon_"+dlg_id+"' src='"+IMGFOLDERPATH+"window.gif' width='32' height='32'></td>");
			arr.push("                <td align='left' style='line-height:16px;'>");
			arr.push("                <h5 class='fb' id='_MessageTitle_"+dlg_id+"'>&nbsp;</h5>");
			arr.push("                <div id='_Message_"+dlg_id+"'>&nbsp;</div></td>");
			arr.push("              </tr></table></td></tr>");
			arr.push("        <tr><td align='center' valign='top'><div style='position:relative;width:"+options.width+"px;height:"+options.height+"px;'>");
			arr.push("         <div  id='_Covering_"+dlg_id+"' style='position:absolute; height:100%; width:100%;display:none;'>&nbsp;</div>");
			if(options.innerHTML){
				arr.push(options.innerHTML);
			}else if(options.innerElementId){
			}else if(options.url){
				arr.push("          <iframe src='");
				if(options.url.substr(0,7)=="http://" || options.url.substr(0,1)=="/"){
					arr.push(options.url);
				}else{
					arr.push(CONTEXT_PATH+options.url);
				}
				arr.push("' id='_DialogFrame_"+dlg_id+"' allowTransparency='true'  width='100%' height='100%' frameborder='0' scrolling='no' style=\"background-color: #transparent; border:none;\"></iframe>");
			}
			arr.push("        </div></td></tr>");
			arr.push("        <tr drag='false' id='_ButtonRow_"+dlg_id+"'><td height='36'>");
			arr.push("            <div id='_DialogButtons_"+dlg_id+"' style='text-align:right; border-top:#dadee5 1px solid; padding:8px 20px; background-color:#f6f6f6;'>");
			arr.push("           	<input id='_ButtonOK_"+dlg_id+"' type='button' onclick=\"EDialog.getDialog('"+dlg_id+"').onFireEvent();\" value='确 定'>");
			arr.push("           	<input id='_ButtonCancel_"+dlg_id+"'  type='button' onclick=\"EDialog.getDialog('"+dlg_id+"').onCancelEvent();\" value='取 消'>");
			arr.push("            </div></td></tr>");
			
			arr.push("      </table><a href='#;' onfocus='EDialog.DM(\"_forTab_"+dlg_id+"\").focus();'></a></td>");
			arr.push("    <td width='13' style=\"background-image:url("+IMGFOLDERPATH+"dialog_mrm.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_mrm.png', sizingMethod='crop');\"></td></tr>");
			arr.push("  <tr><td width='13' height='13' style=\"background-image:url("+IMGFOLDERPATH+"dialog_lb.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_lb.png', sizingMethod='crop');\"></td>");
			arr.push("    <td style=\"background-image:url("+IMGFOLDERPATH+"dialog_cb.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_cb.png', sizingMethod='crop');\"></td>");
			arr.push("    <td width='13' height='13' style=\"background-image:url("+IMGFOLDERPATH+"dialog_rb.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_rb.png', sizingMethod='crop');\"></td>");
			arr.push("  </tr></table>");

			var bgdiv = EDialog.DM("_DialogBGDiv_"+dlg_id);
			if(!bgdiv){
				bgdiv = pw.document.createElement("div");
				bgdiv.id = "_DialogBGDiv_"+dlg_id;
				this.hideE(bgdiv);
			 	EDialog.TG("body",pw.document)[0].appendChild(bgdiv);
				if(isIE6){
					var bgIframeBox=pw.document.createElement('<div style="position:relative;width:100%;height:100%;"></div>');
					var bgIframe=pw.document.createElement('<iframe src="about:blank" scrolling="no" style="filter:alpha(opacity=1);" width="100%" height="100%"></iframe>');
					var bgIframeMask=pw.document.createElement('<div src="about:blank" style="position:absolute;background-color:#333;filter:alpha(opacity=1);width:100%;height:100%;"></div>');
					bgIframeBox.appendChild(bgIframeMask);
					bgIframeBox.appendChild(bgIframe);
					
					bgdiv.appendChild(bgIframeBox);
					
					var bgIframeDoc = bgIframe.contentWindow.document;
					bgIframeDoc.open();
					bgIframeDoc.write("<body style='background-color:#333' oncontextmenu='return false;'></body>") ;
					bgIframeDoc.close();
				}
			}

			var div = EDialog.DM("_DialogDiv_"+dlg_id);
			if(!div){
				div = pw.document.createElement("div");
				this.hideE(div);
				div.id = "_DialogDiv_"+dlg_id;
				// div.className = "dialogdiv";
				// div.setAttribute("dragStart","EDialog.dragStart");
			 	EDialog.TG("body",pw.document)[0].appendChild(div);
			}
			this.DialogDiv = div;
			div.innerHTML = arr.join('\n');
			if(options.innerElementId){
				var innerElement=DM(options.innerElementId);
				innerElement.style.position="";
				innerElement.style.display="";
				if(isIE){
					var fragment=pw.document.createElement("div");
					fragment.innerHTML=innerElement.outerHTML;
					innerElement.outerHTML="";
					EDialog.DM("_Covering_"+dlg_id).parentNode.appendChild(fragment);
				}else{
					EDialog.DM("_Covering_"+dlg_id).parentNode.appendChild(innerElement);
				}
			}
			EDialog.DM("_DialogDiv_"+dlg_id).DialogInstance = this;
			if(options.url)
				EDialog.DM("_DialogFrame_"+dlg_id).DialogInstance = this;
			Drag.init(EDialog.DM("_draghandle_"+dlg_id),EDialog.DM("_DialogDiv_"+dlg_id));// 注册拖拽方法
			if(!isIE){
				EDialog.DM("_DialogDiv_"+dlg_id).dialogId=dlg_id;
				EDialog.DM("_DialogDiv_"+dlg_id).onDragStart = function(){
					EDialog.DM("_Covering_"+this.dialogId).style.display="";
				};
				EDialog.DM("_DialogDiv_"+dlg_id).onDragEnd = function(){
					EDialog.DM("_Covering_"+this.dialogId).style.display="none";
				};
			}

			OKButton = EDialog.DM("_ButtonOK_"+dlg_id);
			CancelButton = EDialog.DM("_ButtonCancel_"+dlg_id);

			// 显示标题图片
			if(options.showMessage){
				this.showE(EDialog.DM("_MessageRow_"+dlg_id));
				if(options.messageTitle){
					EDialog.DM("_MessageTitle_"+dlg_id).innerHTML = options.messageTitle;
				}
				if(options.message){
					EDialog.DM("_Message_"+dlg_id).innerHTML = options.message;
				}
			}
			
			if(!options.AlertFlag){
				this.showE(bgdiv);
				options.bgdivID = "_DialogBGDiv_"+dlg_id;
			}else{
				bgdiv = EDialog.DM("_AlertBGDiv");
				if(!bgdiv){
					bgdiv = pw.document.createElement("div");
					bgdiv.id = "_AlertBGDiv";
					this.hideE(bgdiv);
				 	EDialog.TG("body",pw.document)[0].appendChild(bgdiv);
					if(isIE6){
						var bgIframeBox=pw.document.createElement('<div style="position:relative;width:100%;height:100%;"></div>');
						var bgIframe=pw.document.createElement('<iframe src="about:blank"  scrolling="no" style="filter:alpha(opacity=1);" width="100%" height="100%"></iframe>');
						var bgIframeMask=pw.document.createElement('<div src="about:blank" style="position:absolute;background-color:#333;filter:alpha(opacity=1);width:100%;height:100%;"></div>');
						bgIframeBox.appendChild(bgIframeMask);
						bgIframeBox.appendChild(bgIframe);
						bgdiv.appendChild(bgIframeBox);
						var bgIframeDoc = bgIframe.contentWindow.document;
						bgIframeDoc.open();
						bgIframeDoc.write("<body style='background-color:#333' oncontextmenu='return false;'></body>") ;
						bgIframeDoc.close();
					}
					bgdiv.style.cssText = "background-color:#333;position:absolute;left:0px;top:0px;opacity:0.4;filter:alpha(opacity=40);width:100%;height:" + sh + "px;z-index:991";
				}
				this.showE(bgdiv);
				options.bgdivID = "_AlertBGDiv";
			}
			this.DialogDiv.style.cssText = "position:absolute; display:block;z-index:"+(options.AlertFlag?992:990)+";left:"+options.Left+"px;top:"+options.Top+"px";

			// 判断当前窗口是否是对话框，如果是，则将其置在bgdiv之后
			if(!options.AlertFlag){
				var win = window;
				var flag = false;
				try{
					while(win!=win.parent){// 需要考虑父窗口是弹出窗口中的一个iframe的情况
						if(win._DialogInstance){
							win._DialogInstance.DialogDiv.style.zIndex = 959;
							flag = true;
							break;
						}
						win = win.parent;
					}
				}catch(ex){
					
				}
				if(!flag){
					bgdiv.style.cssText = "background-color:#000;position:absolute;left:0px;top:0px;opacity:0.36;filter:alpha(opacity=36);width:100%;height:" + sh + "px;z-index:960";
				}
				// this.ParentWindow.$D = this;
			}
			// 显示按钮栏
			if(!options.showButton){
				this.hideE(EDialog.DM("_ButtonRow_"+dlg_id));
			} else {
				OKButton.focus();
			}
			
			var pwbody=doc.getElementsByTagName(isQuirks?"BODY":"HTML")[0];
			pwbody.style.overflow="hidden";// 禁止出现滚动条

			EDialog.pushArray(this, pw);// 放入队列中，以便于ESC时正确关闭
		},
		showWindow : function(configs) {
			this.inits(configs);
			if (isIE) {
				options.ParentWindow.showModalessDialog(options.url,
						options.DialogArguments, "dialogWidth:"
								+ options.width + ";dialogHeight:"
								+ options.height
								+ ";help:no;scroll:no;status:no");
			}
			if (isGecko) {
				var sOption = "location=no,menubar=no,status=no;toolbar=no,dependent=yes,dialog=yes,minimizable=no,modal=yes,alwaysRaised=yes,resizable=no";
				options.Window = options.ParentWindow.open('',
						options.url, sOption, true);
				var w = options.Window;
				if (!w) {
					alert("发现弹出窗口被阻止，请更改浏览器设置，以便正常使用本功能!");
					return;
				}
				w.moveTo(options.Left, options.Top);
				w.resizeTo(options.width, options.height + 30);
				w.focus();
				w.location.href = options.url;
				w.Parent = options.ParentWindow;
				w.dialogArguments = options.DialogArguments;
			}
		},
		onFireEvent : function(){
			options = this.options || options;
			if(options.ok){
				options.ok(this.result);
			}
			this.close();
		},
		onCancelEvent : function(){
			options = this.options || options;
			if(options.cancel){
				options.cancel();
			}
			this.close();
		},
		close : function(){
			options = this.options || options;
			var pw = this.getTopLevelWindow();
			if(options.innerElementId){
				var innerElement=this.DM(options.innerElementId);
				innerElement.style.display="none";
				if(isIE){
					//ie下不能跨窗口拷贝元素
					var fragment=pw.document.createElement("div");
					fragment.innerHTML=innerElement.outerHTML;
					innerElement.outerHTML="";
					this.TG("body",pw.document)[0].appendChild(fragment);
				}else{
					this.TG("body",pw.document)[0].appendChild(innerElement);
				}

			}
			if(options.WindowFlag){
				options.ParentWindow.$D = null;
				options.ParentWindow.$DW = null;
				options.Window.opener = null;
				options.Window.close();
				options.Window = null;
			}else{
				//如果上级窗口是对话框，则将其置于bgdiv前
				var doc=pw.document;
				var flag = false;
				if(this.getTopOne(pw)){
					flag = true;
					this.getTopOne(pw).DialogDiv.style.zIndex = 960;
				}
				if(options.AlertFlag){
					this.hideE(this.DM("_AlertBGDiv"));
				}
				if(!flag&&!options.AlertFlag){//此处是为处理弹出窗口被关闭后iframe立即被重定向时背景层不消失的问题
					pw.eval('window._OpacityFunc = function(){EDialog.hideE(EDialog.DM("'+options.bgdivID+'"));}');
					pw._OpacityFunc();
				}
				this.DialogDiv.outerHTML = "";
				var pwbody=doc.getElementsByTagName(isQuirks?"BODY":"HTML")[0];
				pwbody.style.overflow="auto";//还原滚动条
				EDialog.remove(options.ID, pw);
			}
			var bgdiv = EDialog.DM(options.bgdivID);
			if (bgdiv) bgdiv.parentNode.removeChild(bgdiv);
		},
		closeWindow : function(event){
			window.Args._DialogInstance.close();
		},
		getInstance : function(id){
			var pw = EDialog.getTopLevelWindow();
			var f = EDialog.DM("_DialogDiv_"+id);
			if(!f){
				return null;
			}
			return f.DialogInstance;
		},
		setResult : function(rtn){
			var dialog = this.getDialog();
			if (dialog){
				dialog.result = rtn;
			}
		},
		getResult : function(){
			var dialog = this.getDialog();
			if (dialog){
				return dialog.result;
			}
			return null;
		},
		getReturnValue : function(){
			return EDialog.getTopLevelWindow().result;
		},
		doReturn : function(rtn){
			var dialog = this.getDialog();
			if (dialog){
				dialog.result = rtn;
				dialog.onFireEvent();
			}
			
		},
		doExit : function(){
			var dialog = this.getDialog();
			if (dialog){
				dialog.onCancelEvent();
			}
		}
	});
		
	Array.prototype.removeDialog = function(s){
		for(var i=0;i<this.length;i++){
			var dlg = this[i];
			if(dlg && s == dlg.getId()){
				this.splice(i, 1);
				EDialog.getTopLevelWindow().result = dlg.getResult();
			}
		}
	};
	Array.prototype.getDialog = function(s){
		for(var i=0;i<this.length;i++){
			var dlg = this[i];
			if(dlg && s == dlg.getId()){
				return dlg;
			}
		}
		return null;
	};
		
		if(window.HTMLElement && !isIE){//给FF添加IE专有的属性和方法
			HTMLElement.prototype.__defineGetter__("parentElement",function(){
				if(this.parentNode==this.ownerDocument)return null;
				return this.parentNode;
			});
			HTMLElement.prototype.__defineSetter__("outerHTML",function(sHTML){
				var r=this.ownerDocument.createRange();
				r.setStartBefore(this);
				var df=r.createContextualFragment(sHTML);
				this.parentNode.replaceChild(df,this);
				return sHTML;
			});
			HTMLElement.prototype.__defineGetter__("outerHTML",function(){
				var attr;
				var attrs=this.attributes;
				var str="<"+this.tagName;
				for(var i=0;i<attrs.length;i++){
					attr=attrs[i];
					if(attr.specified)
						str+=" "+attr.name+'="'+attr.value+'"';
				}
				if(!this.canHaveChildren)
					return str+">";
				return str+">"+this.innerHTML+"</"+this.tagName+">";
			});
			HTMLElement.prototype.__defineSetter__("innerText",function(sText){
				var parsedText=document.createTextNode(sText);
				this.innerHTML=parsedText;
				return parsedText;
			});
			HTMLElement.prototype.__defineGetter__("innerText",function(){
				var r=this.ownerDocument.createRange();
				r.selectNodeContents(this);
				return r.toString();
			});
		}
		
		_DialogInstance = window.frameElement?window.frameElement.DialogInstance:null;
		Page.onDialogLoad = function(){
			if(_DialogInstance){
				if(_DialogInstance.getTitle()){
					document.title = _DialogInstance.getTitle();
				}
				window.Args = _DialogInstance.getOptions().DialogArguments;
				_DialogInstance.getOptions().Window = window;
				window.Parent = _DialogInstance.getOptions().ParentWindow;
			}
		};

		Page.onDialogLoad();

		PageOnLoad=function (){
			var d = _DialogInstance;
			if(d){
				try{
					d.getOptions().ParentWindow.$D = d;
					d.getOptions().ParentWindow.$DW = d.Window;
					var flag = false;
					if(!d.getOptions().AlertFlag){
						var win = d.getOptions().ParentWindow;
						while(win!=win.parent){
							if(win._DialogInstance){
								flag = true;
								break;
							}
							win = win.parent;
						}
						if(!flag){
							EDialog.DM(d.getOptions().bgdivID).style.opacity="0.4";
							EDialog.DM(d.getOptions().bgdivID).style.filter="alpha(opacity=40)";
						}
					}
					if(d.getOptions().AlertFlag){
						EDialog.showE(EDialog.DM("_AlertBGDiv"));
					}
					if(d.getOptions().showButton&&E.visible(d.CancelButton)){
						d.CancelButton.focus();
					}
					if(d.onLoad){
						d.onLoad();
					}
				}catch(ex){
					//alert("DialogOnLoad:"+ex.message+"\t("+ex.fileName+" "+ex.lineNumber+")");
				}
			}
		};

		Dialog.onKeyDown = function(event){
			if(event.shiftKey&&event.keyCode==9){//shift键
				var pw = EDialog.getTopLevelWindow();
				if(EDialog.size(pw)>0){
					stopEvent(event);
					return false;
				}
			}
			if(event.keyCode==27){//ESC键
				var DialogArr=EDialog.getArray(EDialog.getTopLevelWindow());
				if(DialogArr.length>0){
					var diag = DialogArr[DialogArr.length-1];
					diag.CancelButton.onclick.apply(diag.CancelButton,[]);
				}
			}
		};

		Dialog.dragStart = function(evt){
			
		};
		Dialog.setPositions=function(){
			if(window.parent!=window)return;
			var DialogArr=EDialog.getArray(EDialog.getTopLevelWindow());
			if(DialogArr==null||DialogArr.length==0)return;

			for(var i=0;i<DialogArr.length;i++)
			{
				if (DialogArr[i].setPosition)
					DialogArr[i].setPosition();
			}
		};
		Dialog.prototype.setPosition=function(){
			var pw = EDialog.getTopLevelWindow();
			var doc = pw.document;
			var cw = doc.compatMode == "BackCompat"?doc.body.clientWidth:doc.documentElement.clientWidth;
			var ch = doc.compatMode == "BackCompat"?doc.body.clientHeight:doc.documentElement.clientHeight;//必须考虑文本框处于页面边缘处，控件显示不全的问题
			var sl = Math.max(doc.documentElement.scrollLeft, doc.body.scrollLeft);
			var st = Math.max(doc.documentElement.scrollTop, doc.body.scrollTop);//考虑滚动的情况
			var sw = Math.max(doc.documentElement.scrollWidth, doc.body.scrollWidth);
			var sh = Math.max(doc.documentElement.scrollHeight, doc.body.scrollHeight);
			sw=Math.max(sw,cw);
			sh=Math.max(sh,ch);
			this.options.Top = (ch - this.options.height - 30) / 2 + st - 8;//有8像素的透明背景
			this.options.Left = (cw - this.options.width - 12) / 2 +sl;
			if(this.options.showButton){//按钮行高36
				this.options.Top -= 18;
			}
			this.DialogDiv.style.top=this.options.Top+"px";
			this.DialogDiv.style.left=this.options.Left+"px";
			//pw.DM(this.options.bgdivID).style.width= sw + "px";
			EDialog.DM(this.options.bgdivID).style.height= sh + "px";
		};

		Drag={
		    "obj":null,
			"init":function(ihandle, dragBody, e){
				if (e == null) {
					ihandle.onmousedown=Drag.start;
				}
				ihandle.root = dragBody;

				if(isNaN(parseInt(ihandle.root.style.left)))ihandle.root.style.left="0px";
				if(isNaN(parseInt(ihandle.root.style.top)))ihandle.root.style.top="0px";
				ihandle.root.onDragStart=new Function();
				ihandle.root.onDragEnd=new Function();
				ihandle.root.onDrag=new Function();
				if (e !=null) {
					var handle=Drag.obj=ihandle;
					e=Drag.fixe(e);
					var top=parseInt(handle.root.style.top);
					var left=parseInt(handle.root.style.left);
					handle.root.onDragStart(left,top,e.pageX,e.pageY);
					handle.lastMouseX=e.pageX;
					handle.lastMouseY=e.pageY;
					document.onmousemove=Drag.drag;
					document.onmouseup=Drag.end;
				}
			},
			"start":function(e){
				var pw = EDialog.getTopLevelWindow();
				var handle=Drag.obj=this;
				e=Drag.fixEvent(e);
				var top=parseInt(handle.root.style.top);
				var left=parseInt(handle.root.style.left);
				//alert(left)
				handle.root.onDragStart(left,top,e.pageX,e.pageY);
				handle.lastMouseX=e.pageX;
				handle.lastMouseY=e.pageY;
				pw.document.onmousemove=Drag.drag;
				pw.document.onmouseup=Drag.end;
				return false;
			},
			"drag":function(e){
				e=Drag.fixEvent(e);
									
				var handle=Drag.obj;
				var mouseY=e.pageY;
				var mouseX=e.pageX;
				var top=parseInt(handle.root.style.top);
				var left=parseInt(handle.root.style.left);
				
				if(isIE){
					Drag.obj.setCapture();
				}else{
					e.preventDefault();
				}//作用是将所有鼠标事件捕获到handle对象，对于firefox，以用preventDefault来取消事件的默认动作：

				var currentLeft,currentTop;
				currentLeft=left+mouseX-handle.lastMouseX;
				currentTop=top+(mouseY-handle.lastMouseY);
				handle.root.style.left=currentLeft +"px";
				handle.root.style.top=currentTop+"px";
				handle.lastMouseX=mouseX;
				handle.lastMouseY=mouseY;
				handle.root.onDrag(currentLeft,currentTop,e.pageX,e.pageY);
				return false;
			},
			"end":function(){
				if(isIE){
					Drag.obj.releaseCapture();
				};//取消所有鼠标事件捕获到handle对象
				var pw = EDialog.getTopLevelWindow();
				pw.document.onmousemove=null;
				pw.document.onmouseup=null;
				Drag.obj.root.onDragEnd(parseInt(Drag.obj.root.style.left),parseInt(Drag.obj.root.style.top));
				Drag.obj=null;
			},
			"fixEvent":function(e){//格式化事件参数对象
				var pw = EDialog.getTopLevelWindow();
				var sl = Math.max(pw.document.documentElement.scrollLeft, pw.document.body.scrollLeft);
				var st = Math.max(pw.document.documentElement.scrollTop, pw.document.body.scrollTop);
				if(typeof e=="undefined")e=pw.event;
				if(typeof e.layerX=="undefined")e.layerX=e.offsetX;
				if(typeof e.layerY=="undefined")e.layerY=e.offsetY;
				if(typeof e.pageX == "undefined")e.pageX = e.clientX + sl - pw.document.body.clientLeft;
				if(typeof e.pageY == "undefined")e.pageY = e.clientY + st - pw.document.body.clientTop;
				return e;
			}
		};

		if(isIE){
			document.attachEvent("onkeydown",Dialog.onKeyDown);
			window.attachEvent("onload",PageOnLoad);
			window.attachEvent('onresize',Dialog.setPositions);
		}else{
			document.addEventListener("keydown",Dialog.onKeyDown,false);
			window.addEventListener("load",PageOnLoad,false);
			window.addEventListener('resize',Dialog.setPositions,false);
		}
		
		return Dialog;
	})();
window.EDialog = window.$.Dialog = $.fn.Dialog;
})($);