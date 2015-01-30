var IMGFOLDERPATH = contextPath + '/res/dialog/images/';//图片路径配置
var CONTEXT_PATH = '';//弹出框内页面路径配置
var isIE = navigator.userAgent.toLowerCase().indexOf("msie") != -1;
var isIE6 = navigator.userAgent.toLowerCase().indexOf("msie 6.0") != -1;
var isGecko = navigator.userAgent.toLowerCase().indexOf("gecko") != -1;
var isQuirks = document.compatMode == "BackCompat";

function showDialog(options){
	var dlg = new Dialog({
		ID:new Date().getTime(),
		Width : options.width,
		Height : options.height,
		URL : options.url,
		DialogArguments : options.args?options.args:{},
		Title : options.title,
		ShowMessageRow: options.showMessage!=undefined?options.showMessage:false,
		MessageTitle : options.messageTitle?options.messageTitle:null,
		Message : options.message?options.message:null,
		ShowButtonRow : options.showButton!=undefined?options.showButton:true,
		OKEvent : function(event){
			if (options.ok){
				options.ok(event.result);
			}
		},
		CancelEvent : function(event){
			if (options.cancel){
				options.cancel(event.result);
			}
		}
	});
	dlg.show();
	return dlg;
}
function DM(ele) {
	  if (typeof(ele) == 'string'){
		ele = document.getElementById(ele);
		if(!ele){
			return null;
		}
	  }
	  if(ele){
		Core.attachMethod(ele);
		}
	  return ele;
}

function TG(tagName,ele){
	ele = DM(ele);
	ele = ele || document;
	var ts = ele.getElementsByTagName(tagName);//此处返回的不是数组
	var arr = [];
	var len = ts.length;
	for(var i=0;i<len;i++){
		arr.push(DM(ts[i]));
	}
	return arr;
}
function stopEvent(event){//阻止一切事件执行,包括浏览器默认的事件
	event = window.event||event;
	if(!event){
		return;
	}
	if(isGecko){
		event.preventDefault();
		event.stopPropagation();
	}
	event.cancelBubble = true;
	event.returnValue = false;
}

Array.prototype.remove = function(s){
  for(var i=0;i<this.length;i++){
    if(s == this[i]){
    	this.splice(i, 1);
    }
  }
};
if(window.HTMLElement){//给FF添加IE专有的属性和方法
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

var E = {};
E.ATTR = function(attr,ele) {
	ele = ele || this;
	ele = DM(ele);
	return ele.getAttribute?ele.getAttribute(attr):null;
};
E.getTopLevelWindow = function(){
	var pw = window;
	while(pw!=pw.parent){
		pw = pw.parent;
	}
	return pw;
};
E.hide = function(ele) {
	ele = ele || this;
	ele = DM(ele);
  ele.style.display = 'none';
};
E.show = function(ele) {
	ele = ele || this;
	ele = DM(ele);
  ele.style.display = '';
};
E.visible = function(ele) {
	ele = ele || this;
	ele = DM(ele);
	if(ele.style.display=="none"){
		return false;
	}
  return true;
};

E.utils={
		/**
	     * 遍历数组，对象，nodeList
	     * @name each
	     * @grammar E.utils.each(obj,iterator,[context])
	     * @since 1.2.4+
	     * @desc
	     * * obj 要遍历的对象
	     * * iterator 遍历的方法,方法的第一个是遍历的值，第二个是索引，第三个是obj
	     * * context  iterator的上下文
	     * @example
	     * E.utils.each([1,2],function(v,i){
	     *     console.log(v)//值
	     *     console.log(i)//索引
	     * })
	     * E.utils.each(document.getElementsByTagName('*'),function(n){
	     *     console.log(n.tagName)
	     * })
	     */
	    each : function(obj, iterator, context) {
	        if (obj == null) return;
	        if (obj.length === +obj.length) {
	            for (var i = 0, l = obj.length; i < l; i++) {
	                if(iterator.call(context, obj[i], i, obj) === false)
	                    return false;
	            }
	        } else {
	            for (var key in obj) {
	                if (obj.hasOwnProperty(key)) {
	                    if(iterator.call(context, obj[key], key, obj) === false)
	                        return false;
	                }
	            }
	        }
	    },
		/**
	     * 深度克隆对象，从source到target
	     * @name clone
	     * @grammar E.utils.clone(source) => anthorObj 新的对象是完整的source的副本
	     * @grammar E.utils.clone(source,target) => target包含了source的所有内容，重名会覆盖
	     */
	    clone:function (source, target) {
	        var tmp;
	        target = target || {};
	        for (var i in source) {
	            if (source.hasOwnProperty(i)) {
	                tmp = source[i];
	                if (typeof tmp == 'object') {
	                    target[i] = this.isArray(tmp) ? [] : {};
	                    this.clone(source[i], target[i]);
	                } else {
	                    target[i] = tmp;
	                }
	            }
	        }
	        return target;
	    },
		/**
		 * 将source对象中的属性扩展到target对象上
		 * @name extend
		 * @grammar E.utils.extend(target,source)  => Object  //覆盖扩展
		 * @grammar E.utils.extend(target,source,true)  ==> Object  //保留扩展
		 */
		extend:function (t, s, b) {
			if (s) {
				for (var k in s) {
					if (!b || !t.hasOwnProperty(k)) {
						t[k] = s[k];
					}
				}
			}
			return t;
		},
		extend2:function (t) {
			var a = arguments;
			for (var i = 1; i < a.length; i++) {
				var x = a[i];
				for (var k in x) {
					if (!t.hasOwnProperty(k)) {
						t[k] = x[k];
					}
				}
			}
			return t;
		}
};
/**
 * 判断str是否为字符串
 * @name isString
 * @grammar E.utils.isString(str) => true|false
 */
/**
 * 判断array是否为数组
 * @name isArray
 * @grammar E.utils.isArray(obj) => true|false
 */
/**
 * 判断obj对象是否为方法
 * @name isFunction
 * @grammar E.utils.isFunction(obj)  => true|false
 */
/**
 * 判断obj对象是否为数字
 * @name isNumber
 * @grammar E.utils.isNumber(obj)  => true|false
 */
E.utils.each(['String', 'Function', 'Array', 'Number', 'RegExp', 'Object'], function (v) {
	E.utils['is' + v] = function (obj) {
        return Object.prototype.toString.apply(obj) == '[object ' + v + ']';
    };
});

var Core = {};
Core.attachMethod = function(ele){
	if(!ele||ele["ATTR"]){
		return;
	}
	if(ele.nodeType==9){
		return;
	}
	var win;
	try{
		if(isGecko){
			win = ele.ownerDocument.defaultView;
		}else{
			win = ele.ownerDocument.parentWindow;
		}
		for(var prop in E){
			ele[prop] = win.E[prop];
		}
	}catch(ex){
		//alert("Core.attachMethod:"+ele)//有些对象不能附加属性，如flash
	}
};

var DIALOG_OPTIONS ={
		ID:"dialog0001",
		isModal : true,
		Width : 400,
		Height : 300,
		Top : 0,
		Left : 0,
		ParentWindow : null,
		onLoad : null,
		Window : null,
		Title : "",
		URL : null,
		innerHTML:null,
		innerElementId:null,
		DialogArguments : {},
		WindowFlag : false,
		Message : null,
		MessageTitle : null,
		ShowMessageRow : false,
		ShowButtonRow : true,
		Icon : null,
		bgdivID:null	
};

function Dialog(configs){
	if (typeof configs === "string"){
		if(!configs){
			configs={ID: configs};
		}
	} else {
		if(!configs.ID){
			configs.ID = new Date().getTime();
		}
	}
	this.options = E.utils.extend(E.utils.clone(configs || {}), DIALOG_OPTIONS, true);
	this.result = null;
}

Dialog._Array = [];

Dialog.prototype.showWindow = function(){
	if(isIE){
		this.options.ParentWindow.showModalessDialog( this.options.URL, this.options.DialogArguments, "dialogWidth:" + this.options.Width + ";dialogHeight:" + this.options.Height + ";help:no;scroll:no;status:no") ;
	}
	if(isGecko){
		var sOption  = "location=no,menubar=no,status=no;toolbar=no,dependent=yes,dialog=yes,minimizable=no,modal=yes,alwaysRaised=yes,resizable=no";
		this.options.Window = this.options.ParentWindow.open( '', this.options.URL, sOption, true ) ;
		var w = this.options.Window;
		if ( !w ){
			alert( "发现弹出窗口被阻止，请更改浏览器设置，以便正常使用本功能!" ) ;
			return ;
		}
		w.moveTo( this.options.Left, this.options.Top ) ;
		w.resizeTo( this.options.Width, this.options.Height+30 ) ;
		w.focus() ;
		w.location.href = this.options.URL ;
		w.Parent = this.options.ParentWindow;
		w.dialogArguments = this.options.DialogArguments;
	}
};

Dialog.prototype.show = function(){
	var pw = E.getTopLevelWindow();
	var doc = pw.document;
	var cw = doc.compatMode == "BackCompat"?doc.body.clientWidth:doc.documentElement.clientWidth;
	var ch = doc.compatMode == "BackCompat"?doc.body.clientHeight:doc.documentElement.clientHeight;//必须考虑文本框处于页面边缘处，控件显示不全的问题
	var sl = Math.max(doc.documentElement.scrollLeft, doc.body.scrollLeft);
	var st = Math.max(doc.documentElement.scrollTop, doc.body.scrollTop);//考虑滚动的情况
	var sw = Math.max(doc.documentElement.scrollWidth, doc.body.scrollWidth);
	var sh = Math.max(doc.documentElement.scrollHeight, doc.body.scrollHeight);//考虑滚动的情况
	sw=Math.max(sw,cw);
	sh=Math.max(sh,ch);
//	alert("\n"+cw+"\n"+ch+"\n"+sw+"\n"+sh)

	if ( !this.options.ParentWindow ){
		this.options.ParentWindow = window ;
	}
	this.options.DialogArguments._DialogInstance = this;
	this.options.DialogArguments.ID = this.options.ID;

	if(!this.options.Height){
		this.options.Height = this.options.Width/2;
	}

	if(this.options.Top==0){
		this.options.Top = (ch - this.options.Height - 30) / 2 + st - 8;
	}
	if(this.options.Left==0){
		this.options.Left = (cw - this.options.Width - 12) / 2 +sl;
	}
	if(this.options.ShowButtonRow){//按钮行高36
		this.options.Top -= 18;
	}
	if(this.options.WindowFlag){
		this.showWindow();
		return;
	}
	var dlg_id = this.options.ID;
	var arr = [];
	arr.push("<table style='-moz-user-select:none;' oncontextmenu='stopEvent(event);' onselectstart='stopEvent(event);' border='0' cellpadding='0' cellspacing='0' width='"+(this.options.Width+26)+"'>");
	arr.push("  <tr style='cursor:move;' id='_draghandle_"+dlg_id+"'>");
	arr.push("    <td width='13' height='33' style=\"background-image:url("+IMGFOLDERPATH+"dialog_lt.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_lt.png', sizingMethod='crop');\"><div style='width:13px;'></div></td>");
	arr.push("    <td height='33' style=\"background-image:url("+IMGFOLDERPATH+"dialog_ct.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_ct.png', sizingMethod='crop');\"><div style=\"float:left;font-weight:bold; color:#FFFFFF; padding:9px 0 0 4px;\"><img src=\""+IMGFOLDERPATH+"icon_dialog.gif\" align=\"absmiddle\">&nbsp;"+this.options.Title+"</div>");
	arr.push("      <div style=\"position: relative;cursor:pointer; float:right; margin:5px 0 0; _margin:4px 0 0;height:17px; width:28px; background-image:url("+IMGFOLDERPATH+"dialog_closebtn.gif)\" onMouseOver=\"this.style.backgroundImage='url("+IMGFOLDERPATH+"dialog_closebtn_over.gif)'\" onMouseOut=\"this.style.backgroundImage='url("+IMGFOLDERPATH+"dialog_closebtn.gif)'\" drag='false' onClick=\"Dialog.getInstance('"+dlg_id+"').CancelButton.onclick.apply(Dialog.getInstance('"+dlg_id+"').CancelButton,[]);\"></div></td>");
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
	arr.push("        <tr><td align='center' valign='top'><div style='position:relative;width:"+this.options.Width+"px;height:"+this.options.Height+"px;'>");
	arr.push("         <div  id='_Covering_"+dlg_id+"' style='position:absolute; height:100%; width:100%;display:none;'>&nbsp;</div>");
	if(this.options.innerHTML){
		arr.push(this.options.innerHTML);
	}else if(this.options.innerElementId){
	}else if(this.options.URL){
		arr.push("          <iframe src='");
		if(this.options.URL.substr(0,7)=="http://" || this.options.URL.substr(0,1)=="/"){
			arr.push(this.options.URL);
		}else{
			arr.push(CONTEXT_PATH+this.options.URL);
		}
		arr.push("' id='_DialogFrame_"+dlg_id+"' allowTransparency='true'  width='100%' height='100%' frameborder='0' style=\"background-color: #transparent; border:none;\"></iframe>");
	}
	arr.push("        </div></td></tr>");
	arr.push("        <tr drag='false' id='_ButtonRow_"+dlg_id+"'><td height='36'>");
	arr.push("            <div id='_DialogButtons_"+dlg_id+"' style='text-align:right; border-top:#dadee5 1px solid; padding:8px 20px; background-color:#f6f6f6;'>");
	arr.push("           	<input id='_ButtonOK_"+dlg_id+"' type='button' onclick=\"Dialog.getInstance('"+dlg_id+"').onFireEvent();\" value='确 定'>");
	arr.push("           	<input id='_ButtonCancel_"+dlg_id+"'  type='button' onclick=\"Dialog.getInstance('"+dlg_id+"').onCancelEvent();\" value='取 消'>");
	arr.push("            </div></td></tr>");
	
	arr.push("      </table><a href='#;' onfocus='DM(\"_forTab_"+dlg_id+"\").focus();'></a></td>");
	arr.push("    <td width='13' style=\"background-image:url("+IMGFOLDERPATH+"dialog_mrm.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_mrm.png', sizingMethod='crop');\"></td></tr>");
	arr.push("  <tr><td width='13' height='13' style=\"background-image:url("+IMGFOLDERPATH+"dialog_lb.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_lb.png', sizingMethod='crop');\"></td>");
	arr.push("    <td style=\"background-image:url("+IMGFOLDERPATH+"dialog_cb.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_cb.png', sizingMethod='crop');\"></td>");
	arr.push("    <td width='13' height='13' style=\"background-image:url("+IMGFOLDERPATH+"dialog_rb.png) !important;background-image: none;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+IMGFOLDERPATH+"dialog_rb.png', sizingMethod='crop');\"></td>");
	arr.push("  </tr></table>");
	this.TopWindow = pw;

	var bgdiv = pw.DM("_DialogBGDiv");
	if(!bgdiv){
		bgdiv = pw.document.createElement("div");
		bgdiv.id = "_DialogBGDiv";
		E.hide(bgdiv);
	 	pw.TG("body")[0].appendChild(bgdiv);
		if(isIE6){
			var bgIframeBox=pw.document.createElement('<div style="position:relative;width:100%;height:100%;"></div>');
			var bgIframe=pw.document.createElement('<iframe src="about:blank" style="filter:alpha(opacity=1);" width="100%" height="100%"></iframe>');
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

	var div = pw.DM("_DialogDiv_"+dlg_id);
	if(!div){
		div = pw.document.createElement("div");
		E.hide(div);
		div.id = "_DialogDiv_"+dlg_id;
		//div.className = "dialogdiv";
		//div.setAttribute("dragStart","Dialog.dragStart");
	 	pw.TG("body")[0].appendChild(div);
	}
	/*div.onmousedown = function(evt){
		var w = E.getTopLevelWindow();
		//w.DragManager.onMouseDown(evt||w.event,this);//拖拽处理
	}*/

	this.DialogDiv = div;
	div.innerHTML = arr.join('\n');
	if(this.options.innerElementId){
		var innerElement=DM(this.options.innerElementId);
		innerElement.style.position="";
		innerElement.style.display="";
		if(isIE){
			var fragment=pw.document.createElement("div");
			fragment.innerHTML=innerElement.outerHTML;
			innerElement.outerHTML="";
			pw.DM("_Covering_"+dlg_id).parentNode.appendChild(fragment);
		}else{
			pw.DM("_Covering_"+dlg_id).parentNode.appendChild(innerElement);
		}
	}
	pw.DM("_DialogDiv_"+dlg_id).DialogInstance = this;
	if(this.options.URL)
		pw.DM("_DialogFrame_"+dlg_id).DialogInstance = this;
	pw.Drag.init(pw.DM("_draghandle_"+dlg_id),pw.DM("_DialogDiv_"+dlg_id));//注册拖拽方法
	if(!isIE){
		pw.DM("_DialogDiv_"+dlg_id).dialogId=dlg_id;
		pw.DM("_DialogDiv_"+dlg_id).onDragStart = function(){
			var topWin = E.getTopLevelWindow();
			topWin.DM("_Covering_"+this.dialogId).style.display="";
		};
		pw.DM("_DialogDiv_"+dlg_id).onDragEnd = function(){
			var topWin = E.getTopLevelWindow();
			topWin.DM("_Covering_"+this.dialogId).style.display="none";
		};
	}

	this.OKButton = pw.DM("_ButtonOK_"+dlg_id);
	this.CancelButton = pw.DM("_ButtonCancel_"+dlg_id);

	//显示标题图片
	if(this.options.ShowMessageRow){
		E.show(pw.DM("_MessageRow_"+dlg_id));
		if(this.options.MessageTitle){
			pw.DM("_MessageTitle_"+dlg_id).innerHTML = this.options.MessageTitle;
		}
		if(this.options.Message){
			pw.DM("_Message_"+dlg_id).innerHTML = this.options.Message;
		}
	}
	
	if(!this.options.AlertFlag){
		E.show(bgdiv);
		this.options.bgdivID = "_DialogBGDiv";
	}else{
		bgdiv = pw.DM("_AlertBGDiv");
		if(!bgdiv){
			bgdiv = pw.document.createElement("div");
			bgdiv.id = "_AlertBGDiv";
			E.hide(bgdiv);
		 	pw.TG("body")[0].appendChild(bgdiv);
			if(isIE6){
				var bgIframeBox=pw.document.createElement('<div style="position:relative;width:100%;height:100%;"></div>');
				var bgIframe=pw.document.createElement('<iframe src="about:blank" style="filter:alpha(opacity=1);" width="100%" height="100%"></iframe>');
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
		E.show(bgdiv);
		this.options.bgdivID = "_AlertBGDiv";
	}
	this.DialogDiv.style.cssText = "position:absolute; display:block;z-index:"+(this.options.AlertFlag?992:990)+";left:"+this.options.Left+"px;top:"+this.options.Top+"px";

	//判断当前窗口是否是对话框，如果是，则将其置在bgdiv之后
	if(!this.options.AlertFlag){
		var win = window;
		var flag = false;
		while(win!=win.parent){//需要考虑父窗口是弹出窗口中的一个iframe的情况
			if(win._DialogInstance){
				win._DialogInstance.DialogDiv.style.zIndex = 959;
				flag = true;
				break;
			}
			win = win.parent;
		}
		if(!flag){
			bgdiv.style.cssText = "background-color:#333;position:absolute;left:0px;top:0px;opacity:0.4;filter:alpha(opacity=40);width:100%;height:" + sh + "px;z-index:960";
		}
		//this.ParentWindow.$D = this;
	}
	//显示按钮栏
	if(!this.options.ShowButtonRow){
		pw.DM("_ButtonRow_"+dlg_id).hide();
	} else {
		this.OKButton.focus();
	}
	
	var pwbody=doc.getElementsByTagName(isQuirks?"BODY":"HTML")[0];
	pwbody.style.overflow="hidden";//禁止出现滚动条

	pw.Dialog._Array.push(dlg_id);//放入队列中，以便于ESC时正确关闭
};

Dialog.prototype.addParam = function(paramName,paramValue){
	this.options.DialogArguments[paramName] = paramValue;
};

Dialog.prototype.onFireEvent = function(){
	if(this.options.OKEvent){
		this.options.OKEvent(this);
	}
	this.close();
};

Dialog.prototype.onCancelEvent = function(){
	if(this.options.CancelEvent){
		this.options.CancelEvent(this);
	}
	this.close();
};

Dialog.prototype.close = function(){
	if(this.options.innerElementId){
		var innerElement=E.getTopLevelWindow().DM(this.options.innerElementId);
		innerElement.style.display="none";
		if(isIE){
			//ie下不能跨窗口拷贝元素
			var fragment=document.createElement("div");
			fragment.innerHTML=innerElement.outerHTML;
			innerElement.outerHTML="";
			TG("body")[0].appendChild(fragment);
		}else{
			TG("body")[0].appendChild(innerElement);
		}

	}
	if(this.options.WindowFlag){
		this.options.ParentWindow.$D = null;
		this.options.ParentWindow.$DW = null;
		this.options.Window.opener = null;
		this.options.Window.close();
		this.options.Window = null;
	}else{
		//如果上级窗口是对话框，则将其置于bgdiv前
		var pw = E.getTopLevelWindow();
		var doc=pw.document;
		var win = window;
		var flag = false;
		while(win!=win.parent){
			if(win._DialogInstance){
				flag = true;
				win._DialogInstance.DialogDiv.style.zIndex = 960;
				break;
			}
			win = win.parent;
		}
		if(this.options.AlertFlag){
			E.hide(pw.DM("_AlertBGDiv"));
		}
		if(!flag&&!this.options.AlertFlag){//此处是为处理弹出窗口被关闭后iframe立即被重定向时背景层不消失的问题
			pw.eval('window._OpacityFunc = function(){var w = E.getTopLevelWindow();E.hide(w.DM("_DialogBGDiv"));}');
			pw._OpacityFunc();
		}
		this.DialogDiv.outerHTML = "";
		var pwbody=doc.getElementsByTagName(isQuirks?"BODY":"HTML")[0];
		pwbody.style.overflow="auto";//还原滚动条
		pw.Dialog._Array.remove(this.options.ID);
	}
};

Dialog.prototype.addButton = function(id,txt,func){
	var html = "<input id='_Button_"+this.options.ID+"_"+id+"' type='button' value='"+txt+"'> ";
	var pw = E.getTopLevelWindow();
	pw.DM("_DialogButtons_"+this.options.ID).TG("input")[0].getParent("a").insertAdjacentHTML("beforeBegin",html);
	pw.DM("_Button_"+this.options.ID+"_"+id).onclick = func;
};

Dialog.close = function(evt){
	window.Args._DialogInstance.close();
};

Dialog.getInstance = function(id){
	var pw = E.getTopLevelWindow();
	var f = pw.DM("_DialogDiv_"+id);
	if(!f){
		return null;
	}
	return f.DialogInstance;
};

Dialog.AlertNo = 0;
Dialog.alert = function(msg,func,w,h){
	var pw = E.getTopLevelWindow();
	var diag = new Dialog({
		ID : "_DialogAlert"+Dialog.AlertNo++,
		Width : (w?w:300),
		Height : (h?h:120),
		Title : "系统提示",
		URL : "javascript:void(0);",
		AlertFlag : true,
		CancelEvent : function(event){
			if(func){
				func(event);
			}
		}});
	diag.show();
	pw.DM("_AlertBGDiv").style.display="";
	E.hide(pw.DM("_ButtonOK_"+diag.options.ID));
	var win = pw.DM("_DialogFrame_"+diag.options.ID).contentWindow;
	var doc = win.document;
	doc.open();
	doc.write("<body oncontextmenu='return false;'></body>") ;
	var arr = [];
	arr.push("<table height='100%' border='0' align='center' cellpadding='10' cellspacing='0'>");
	arr.push("<tr><td align='right'><img id='Icon' src='"+IMGFOLDERPATH+"icon_alert.gif' width='34' height='34' align='absmiddle'></td>");
	arr.push("<td align='left' id='Message' style='font-size:9pt'>"+msg+"</td></tr></table>");
	var div = doc.createElement("div");
	div.innerHTML = arr.join('');
	doc.body.appendChild(div);
	doc.close();
	var h = Math.max(doc.documentElement.scrollHeight, doc.body.scrollHeight);
	var w = Math.max(doc.documentElement.scrollWidth, doc.body.scrollWidth);
	if(w>300){
		win.frameElement.width = w;
	}
	if(h>120){
		win.frameElement.height = h;
	}

	diag.CancelButton.value = "确 定";
	diag.CancelButton.focus();
	pw.DM("_DialogButtons_"+diag.options.ID).style.textAlign = "center";
};

Dialog.confirm = function(msg,func1,w,h){
	var pw = E.getTopLevelWindow();
	var diag = new Dialog({
		ID:"_DialogAlert"+Dialog.AlertNo++,
		Width : (w?w:300),
		Height : (h?h:120),
		Title : "信息确认",
		URL : "javascript:void(0);",
		AlertFlag : true,
		CancelEvent : function(event){
			event.result = false;
			if(func1){
				func1(event.result);
			}
		},
		OKEvent : function(event){
			event.result = true;
			if(func1){
				func1(event.result);
			}
		}});
	diag.show();
	pw.DM("_AlertBGDiv").style.dispaly="";
	var win = pw.DM("_DialogFrame_"+diag.options.ID).contentWindow;
	var doc = win.document;
	doc.open();
	doc.write("<body oncontextmenu='return false;'></body>") ;
	var arr = [];
	arr.push("<table height='100%' border='0' align='center' cellpadding='10' cellspacing='0'>");
	arr.push("<tr><td align='right'><img id='Icon' src='"+IMGFOLDERPATH+"icon_query.gif' width='34' height='34' align='absmiddle'></td>");
	arr.push("<td align='left' id='Message' style='font-size:9pt'>"+msg+"</td></tr></table>");
	var div = doc.createElement("div");
	div.innerHTML = arr.join('');
	doc.body.appendChild(div);
	doc.close();
	diag.OKButton.focus();
	pw.DM("_DialogButtons_"+diag.options.ID).style.textAlign = "center";
};

var _DialogInstance = window.frameElement?window.frameElement.DialogInstance:null;
var Page={};
Page.onDialogLoad = function(){
	if(_DialogInstance){
		if(_DialogInstance.options.Title){
			document.title = _DialogInstance.options.Title;
		}
		window.Args = _DialogInstance.options.DialogArguments;
		_DialogInstance.options.Window = window;
		window.Parent = _DialogInstance.options.ParentWindow;
	}
};

Page.onDialogLoad();

PageOnLoad=function (){
	var d = _DialogInstance;
	if(d){
		try{
			d.options.ParentWindow.$D = d;
			d.options.ParentWindow.$DW = d.Window;
			var flag = false;
			if(!d.options.AlertFlag){
				var win = d.options.ParentWindow;
				while(win!=win.parent){
					if(win._DialogInstance){
						flag = true;
						break;
					}
					win = win.parent;
				}
				if(!flag){
					E.getTopLevelWindow().DM("_DialogBGDiv").style.opacity="0.4";
					E.getTopLevelWindow().DM("_DialogBGDiv").style.filter="alpha(opacity=40)";
				}
			}
			if(d.options.AlertFlag){
				E.show(E.getTopLevelWindow().DM("_AlertBGDiv"));
			}
			if(d.options.ShowButtonRow&&E.visible(d.CancelButton)){
				d.CancelButton.focus();
			}
			if(d.onLoad){
				d.onLoad();
			}
		}catch(ex){alert("DialogOnLoad:"+ex.message+"\t("+ex.fileName+" "+ex.lineNumber+")");}
	}
};

Dialog.onKeyDown = function(event){
	if(event.shiftKey&&event.keyCode==9){//shift键
		var pw = E.getTopLevelWindow();
		if(pw.Dialog._Array.length>0){
			stopEvent(event);
			return false;
		}
	}
	if(event.keyCode==27){//ESC键
		var pw = E.getTopLevelWindow();
		if(pw.Dialog._Array.length>0){
			//Page.mousedown();
			//Page.click();
			var diag = pw.Dialog.getInstance(pw.Dialog._Array[pw.Dialog._Array.length-1]);
			diag.CancelButton.onclick.apply(diag.CancelButton,[]);
		}
	}
};

Dialog.dragStart = function(evt){
	//DragManager.doDrag(evt,this.getParent("div"));//拖拽处理
};
Dialog.setPosition=function(){
	if(window.parent!=window)return;
	var pw = E.getTopLevelWindow();
	var DialogArr=pw.Dialog._Array;
	if(DialogArr==null||DialogArr.length==0)return;

	for(var i=0;i<DialogArr.length;i++)
	{
		pw.DM("_DialogDiv_"+DialogArr[i]).DialogInstance.setPosition();
	}
};
Dialog.prototype.setPosition=function(){
	var pw = E.getTopLevelWindow();
	var doc = pw.document;
	var cw = doc.compatMode == "BackCompat"?doc.body.clientWidth:doc.documentElement.clientWidth;
	var ch = doc.compatMode == "BackCompat"?doc.body.clientHeight:doc.documentElement.clientHeight;//必须考虑文本框处于页面边缘处，控件显示不全的问题
	var sl = Math.max(doc.documentElement.scrollLeft, doc.body.scrollLeft);
	var st = Math.max(doc.documentElement.scrollTop, doc.body.scrollTop);//考虑滚动的情况
	var sw = Math.max(doc.documentElement.scrollWidth, doc.body.scrollWidth);
	var sh = Math.max(doc.documentElement.scrollHeight, doc.body.scrollHeight);
	sw=Math.max(sw,cw);
	sh=Math.max(sh,ch);
	this.options.Top = (ch - this.options.Height - 30) / 2 + st - 8;//有8像素的透明背景
	this.options.Left = (cw - this.options.Width - 12) / 2 +sl;
	if(this.options.ShowButtonRow){//按钮行高36
		this.options.Top -= 18;
	}
	this.DialogDiv.style.top=this.options.Top+"px";
	this.DialogDiv.style.left=this.options.Left+"px";
	//pw.DM(this.options.bgdivID).style.width= sw + "px";
	pw.DM(this.options.bgdivID).style.height= sh + "px";
};

var Drag={
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
		var handle=Drag.obj=this;
		e=Drag.fixEvent(e);
		var top=parseInt(handle.root.style.top);
		var left=parseInt(handle.root.style.left);
		//alert(left)
		handle.root.onDragStart(left,top,e.pageX,e.pageY);
		handle.lastMouseX=e.pageX;
		handle.lastMouseY=e.pageY;
		document.onmousemove=Drag.drag;
		document.onmouseup=Drag.end;
		return false;
	},
	"drag":function(e){
		e=Drag.fixEvent(e);
							
		var handle=Drag.obj;
		var mouseY=e.pageY;
		var mouseX=e.pageX;
		var top=parseInt(handle.root.style.top);
		var left=parseInt(handle.root.style.left);
		
		if(isIE){Drag.obj.setCapture();}else{e.preventDefault();};//作用是将所有鼠标事件捕获到handle对象，对于firefox，以用preventDefault来取消事件的默认动作：

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
		if(isIE){Drag.obj.releaseCapture();};//取消所有鼠标事件捕获到handle对象
		document.onmousemove=null;
		document.onmouseup=null;
		Drag.obj.root.onDragEnd(parseInt(Drag.obj.root.style.left),parseInt(Drag.obj.root.style.top));
		Drag.obj=null;
	},
	"fixEvent":function(e){//格式化事件参数对象
		var sl = Math.max(document.documentElement.scrollLeft, document.body.scrollLeft);
		var st = Math.max(document.documentElement.scrollTop, document.body.scrollTop);
		if(typeof e=="undefined")e=window.event;
		if(typeof e.layerX=="undefined")e.layerX=e.offsetX;
		if(typeof e.layerY=="undefined")e.layerY=e.offsetY;
		if(typeof e.pageX == "undefined")e.pageX = e.clientX + sl - document.body.clientLeft;
		if(typeof e.pageY == "undefined")e.pageY = e.clientY + st - document.body.clientTop;
		return e;
	}
};

if(isIE){
	document.attachEvent("onkeydown",Dialog.onKeyDown);
	window.attachEvent("onload",PageOnLoad);
	window.attachEvent('onresize',Dialog.setPosition);
}else{
	document.addEventListener("keydown",Dialog.onKeyDown,false);
	window.addEventListener("load",PageOnLoad,false);
	window.addEventListener('resize',Dialog.setPosition,false);
}