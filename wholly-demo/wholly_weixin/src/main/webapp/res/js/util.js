/**
 * var event = null; var currentKey = 0; var eventSource = '';
 * 
 * function doEventThing(eventTag){ event = eventTag||window.event; currentKey =
 * event.charCode||event.keyCode; eventSource =
 * event.srcElement||eventTag.target; }
 * 
 * document.onkeydown=function(){ doEventThing(arguments[0]); if(currentKey==8){
 * var obj = document.activeElement; if (obj.tagName.toUpperCase() != 'TEXTAREA' &&
 * obj.tagName.toUpperCase() != 'INPUT'){ return false; } } };
 */
$(document).keydown(function(e) {
	if (e.which == 8){
		var obj = document.activeElement;
		if (obj && !((obj.tagName.toUpperCase() == 'TEXTAREA' || obj.tagName.toUpperCase() == 'INPUT') && !(obj.readOnly||obj.disabled))){
			return false;
		}
	}
});

/**
 * 日期选择框（只显示日期）
 * 
 * @param prevId
 *            开始日期
 * @param skintype
 *            皮肤
 * @return
 */
function showDateDIV(prevId,skintype){
	var minDate = "";
	var maxDate = "2050-12-30";
	if (prevId){
		minDate = document.getElementById(prevId).value;
	}
	var skin = 'whyGreen';
	if (skintype)
		skin = skintype;
	var settings = {dateFmt:'yyyy-MM-dd', minDate:'', maxDate:'', skin:''};
	settings.minDate = minDate;
	settings.maxDate = maxDate;
	settings.skin = skin;
	WdatePicker(settings);
}

/**
 * 日期选择框（只显示年.月）
 * 
 * @param prevId
 *            开始日期
 * @param skintype
 *            皮肤
 * @return
 */
function showDateMDIV(prevId,skintype){
	var minDate = "";
	var maxDate = "2050-12";
	if (prevId){
		minDate = document.getElementById(prevId).value;
	}
	var skin = 'whyGreen';
	if (skintype)
		skin = skintype;
	var settings = {dateFmt:'yyyy-MM',skin:''};
	/*
	 * settings.minDate = minDate; settings.maxDate = maxDate;
	 */
	settings.skin = skin;
	WdatePicker(settings);
}


/**
 * 日期时间选择框（显示日期和时间）
 * 
 * @param prevId
 *            开始日期
 * @param skintype
 *            皮肤
 * @return
 */
function showDateTimeDIV(prevId,skintype){
	var minDate = "";
	var maxDate = "2050-12-30 00:00:00";
	if (prevId){
		minDate = document.getElementById(prevId).value;
	}
	var skin = 'whyGreen';
	if (skintype)
		skin = skintype;
	var settings = {dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'', maxDate:'', skin:''};
	settings.minDate = minDate;
	settings.maxDate = maxDate;
	settings.skin = skin;
	WdatePicker(settings);
}

/**
 * 时间选择框（只显示时间）
 * 
 * @param skintype
 *            皮肤
 * @return
 */
function showTimeDIV(skintype){
	var settings = {dateFmt:'HH:mm:ss', skin:'whyGreen'};
	if (skintype)
		settings.skin = skintype;
	WdatePicker(settings);
}

function setToday(obj) {
	if (document.getElementById(obj) != null && document.getElementById(obj).value == '')
		document.getElementById(obj).value = getToday;;
}

function getToday() {
	var d, s = '';
	d = new Date();
	s += d.getYear() + '-';
	s += (d.getMonth() + 1) + "-";
	s += d.getDate();
	return (s);
}

function addParam(url, name, val) {
	if (url != null && name != null) {
		if (url.indexOf('?') == -1) {
			url = url + '?' + name + '=' + val;
		} else {
			url = url + '&' + name + '=' + val;
		}
	}
	return url;
}

function headerElements(d) {
	var oSelect;
	var doc = window.top;
	if (doc) {
		oSelect = doc.document.getElementsByTagName("SELECT");
	} else {
		doc = window.top;

		if (doc) {
			oSelect = doc.document.getElementsByTagName("SELECT");
		} else {
			doc = window.top.document;
			oSelect = doc.getElementsByTagName("SELECT");
		}
	}
	if (oSelect.length > 0) {
		for (var i = 0; i < oSelect.length; i++) {
			oSelect[i].disabled = d;
		}
	}
}

function getYear(str) {
	var ary = str.split("-");
	return ary[0];
}

function getMonth(str) {
	var ary = str.split("-");
	return ary[1];
}

function getDay(str) {
	var ary = str.split("-");
	return ary[2];
}

function getSelectedValue(Obj) {
	if (Obj != null && Obj.options.length > 0) {
		return Obj.options[Obj.selectedIndex].value;
	}
	return "";
}

function selectOne(Obj, value, callback) {
	if (Obj != null) {
		for (var i = 0; i < Obj.options.length; ++i)
			if (Obj.options[i].value == value) {
				Obj.selectedIndex = i;
				if (callback) {
					callback(Obj);
				}
				break;
			}
	}
}

function selectMulti(fieldName, values) {
	var oSelect = document.getElementsByName(fieldName);
	if (oSelect && oSelect.multiple) {
		for (var i = 0; i < oSelect.options.length; ++i) {
			if (values.indexOf(oSelect.options[i].value)) {
				oSelect.selectedIndex = i;
				break;
			}
		}
	}
}

function setCheck(els, value) {
	for (var i = 0; i < els.length; ++i) {
		if (typeof(value) == "object") {
			els[i].checked = (value.indexOf(els[i].value) >= 0);
		} else {
			els[i].checked = els[i].value == value;
		}
	}
}

function getCheckedListStr(fldName) {
	var rtn = null; // 不更改原有值
	var flds = document.getElementsByName(fldName);
	if (flds && flds.length > 0) {
		if (flds[0].type == 'checkbox' || flds[0].type == 'radio') {
			rtn = '';
			for (var i = 0; i < flds.length; i++) {
				if (flds[i].checked && flds[i].value) {
					rtn += flds[i].value + ";";
				}
			}

			rtn = rtn.substring(0, rtn.lastIndexOf(';'));
		} else {
			rtn = flds[0].value;
		}
	}
	return rtn;
}


//单击列头排序
function sortTable(nColName) {
	var colEl = document.getElementById("_sortCol");
	var oColName = colEl.value;
	changeStatus(oColName, nColName);
	document.forms[0].submit();
}

//改变排序状态
function changeStatus(oCol, nCol) {
	var statusEl = document.getElementById("_sortStatus");
	var colEl = document.getElementById("_sortCol");
	if (oCol != nCol) {
		statusEl.value = "ASC";
		colEl.value = nCol;
	} else {
		if (statusEl.value == "ASC") {
			statusEl.value = "DESC";
			colEl.value = nCol;
		} 
		/*
		 * else if (statusEl.value == "DESC") { statusEl.value = ""; colEl.value =
		 * ""; }
		 */
		else {
			statusEl.value = "ASC";
			colEl.value = nCol;
		}
	}
}


var fieldsTemp = {};
var divsTemp = {};

function ev_getValue(fieldName) {
	var rtn = null;
	try {
		var tempFld = fieldsTemp[fieldName];

		if (!tempFld) {
			tempFld = addField2Temp(fieldName);
		}

		if (tempFld) {
			// alert(fieldName + " tempFld: " + tempFld.type);
			if (tempFld.type == 'radio' || tempFld.type == 'checkbox') {
				rtn = getCheckedListStr(fieldName);
			}
			// else if (tempFld.type == 'radio'){
			// }
			else {
				rtn = tempFld.value;
			}
		}
	} catch (ex) {
		alert("util.js(ev_getValue)." + fieldName + ": " + ex.message);
	}

	return rtn;
}

function addField2Temp(fieldName) {
	var oFld = document.getElementsByName(fieldName);
	if (oFld) {
		// alert(fieldName +" | "+oFld.length +" | "+ oFld.tagName);
		fieldsTemp[fieldName] = oFld[0];
	}

	return fieldsTemp[fieldName];
}

function refreshField(divid, fieldName, fieldHTML, isDecode) {
	try {
		var d = divsTemp[divid];

		if (!d) {
			var oDiv = document.getElementById(divid);
			if (oDiv) {
				divsTemp[divid] = oDiv;
				d = oDiv;
			}
		}
		
		if (d) {
			var regExp = /<script.*>(.*)<\/script>/gi;
			
			if (isDecode == true) {
				fieldHTML = HTMLDencode(fieldHTML);
			}
			// alert(fieldName + ": " + fieldHTML);
			d.innerHTML = fieldHTML; // 1.插入HTML

			if (regExp.test(fieldHTML)) { // 2.执行脚本
				eval(RegExp.$1);
			}
			// alert(fieldHTML);
			addField2Temp(fieldName);
		}
	} catch (ex) {

	}
}


function makeAllFieldAble(elements) {
	if (!elements) {
		elements = document.forms[0].elements;
	}
	for (var i = 0; i < elements.length; i++) {
		var element = elements[i];
		if (element.disabled == true) {
			// alert("****type->" + element.type);
			element.disabled = false;
		}
	}
}

function toggleButton(btnName) {
	var button_acts = document.getElementsByName(btnName);
	for (var i = 0; i < button_acts.length; i++) {
		if (button_acts[i].disabled) {
			button_acts[i].disabled = false;
		} else {
			button_acts[i].disabled = true;
		}
	}
}

function changeClass(obj, className) {
	hover(obj, function(e, em) {
		addClass(em, className);
	}, function(e, em) {
		removeClass(em, className);
	});
}
function hasClass(obj, cls) {
	return obj.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
}
function addClass(obj, cls) {
	if (!this.hasClass(obj, cls))
		obj.className += " " + cls;
}
function removeClass(obj, cls) {
	if (hasClass(obj, cls)) {
		var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
		obj.className = obj.className.replace(reg, ' ');
	}
}
function bind(elem, ev, callback) {
	if (document.all) {
		elem.attachEvent("on" + ev, callback);
	} else {
		elem.addEventListener(ev, callback, false);
	}
}
function unbind(elem, ev, callback) {
	if (typeof (callback) == "function") {
		if (document.all) {
			elem.detachEvent("on" + ev, callback);
		} else {
			elem.removeEventListener(ev, callback, false);
		}
	} else {
		if (document.all) {
			elem.detachEvent("on" + ev);
		} else {
			elem.removeEventListener(ev, false);
		}
	}
}
function hover(elem, overCallback, outCallback) {// 实现hover事件
	var isHover = false;// 判断是否悬浮在上方
	var preOvTime = new Date().getTime();// 上次悬浮时间
	function over(e) {
		var curOvTime = new Date().getTime();
		isHover = true;// 处于over状态
		if (curOvTime - preOvTime > 10) {// 时间间隔超过10毫秒，认为鼠标完成了mouseout事件
			overCallback(e, elem);
		}
		preOvTime = curOvTime;
	}
	function out(e) {
		var curOvTime = new Date().getTime();
		preOvTime = curOvTime;
		isHover = false;
		setTimeout(function() {
			if (!isHover) {
				outCallback(e, elem);
			}
		}, 10);
	}
	bind(elem, "mouseover", over);
	bind(elem, "mouseout", out);
};

function showNormalWindow(url, n, w, h, param) {
	var par = 'toolbar=no,location=no,status=no,menubar=no,resizable=yes,scrollbars=yes';
	if (param == null) {
		param = par;
	} else {
		param = param + par;
	}
	var t = (screen.height - (h)) / 2;
	var l = (screen.width - (w)) / 2;
	if (t > 22)
		t = t - 22;
	else
		t = 0;
	if (l > 22)
		l = l - 22;
	else
		l = 0;
	var ret = window.open(url, n, param + ',top=' + t + ',left=' + l
			+ ',width=' + w + ',height=' + h);
	try {
		ret.focus();
	} catch (ex) {
	}
	return false;
}

function openFrameLink(url, target, isFull) {
	if (isFull) {
		document.getElementById(target).src = url;
	} else {
		document.getElementById(target).src = contextPath + url;
	}

}

function openLink(url, target, tagA, isFull, selected_class) {
	openFrameLink(url, target, isFull);
	if (tagA) {
		var s = 'nav_f_h';
		if (selected_class){
			s = selected_class;
		}
		$('.'+s).removeClass(s);
		$("#nav_f_" + tagA).addClass(s);
	}
}

function openWindow(url, target) {
	var targets = "_blank";
	if (!target) {
		targets = target;
	}
	var fulls = "left=0,top=0,toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=no,status=yes"; // 定义弹出窗口的参数

	if (window.screen) {
		var ah = screen.availHeight - 30;
		var aw = screen.availWidth - 10;
		fulls += ",height=" + ah;
		fulls += ",innerHeight=" + ah;
		fulls += ",width=" + aw;
		fulls += ",innerWidth=" + aw;
		fulls += ",resizable";
	} else {
		fulls += ",height=600";
		fulls += ",innerHeight=600";
		fulls += ",width=800";
		fulls += ",innerWidth=800";
		fulls += ",resizable"; // 对于不支持screen属性的浏览器，可以手工进行最大化。 manually
	}
	window.open(url, targets, fulls);
}

function openModelWindow(url, target) {
	var targets = "_blank";
	if (!target) {
		targets = target;
	}
	var fulls = "left=0,screenX=0,top=0,screenY=0,scrollbars=1"; // 定义弹出窗口的参数

	if (window.screen) {
		var ah = screen.availHeight - 30;
		var aw = screen.availWidth - 10;
		fulls += ",height=" + ah;
		fulls += ",innerHeight=" + ah;
		fulls += ",width=" + aw;
		fulls += ",innerWidth=" + aw;
		fulls += ",resizable";
	} else {
		fulls += ",height=600";
		fulls += ",innerHeight=600";
		fulls += ",width=800";
		fulls += ",innerWidth=800";
		fulls += ",resizable"; // 对于不支持screen属性的浏览器，可以手工进行最大化。 manually
	}
	window.open(url, targets, fulls);
}

function getIFrameWindow(id) {
	var iframe = document.getElementById(id);
	if (iframe)
		return iframe.contentWindow;
	else
		return iframe;
}

function getIFrameDOM(id) {
	var iframe = document.getElementById(id);
	var Doc = iframe.document;
	if (iframe.contentDocument) { // For NS6
		Doc = iframe.contentDocument;
	} else if (iframe.contentWindow) { // For IE5.5 and IE6
		Doc = iframe.contentWindow.document;
	}
	return Doc || document.frames[id].document;
}

function HTMLEncode(text) {
	var textold;
	do {
		textold = text;

		text = text.replace(/&/g, "@amp;");
		text = text.replace(/"/g, "@quot;");
		text = text.replace(/</g, "@lt;");
		text = text.replace(/>/g, "@gt;");
		text = text.replace(/'/g, "@#146;");
		text = text.replace(/\ /g, "@nbsp;");
		text = text.replace(/\r/g, '&#10;');
		text = text.replace(/\n/g, '&#13;');
	} while (textold != text);

	return text;
}

function HTMLDencode(text) {
	var textold;
	if (text) {
		do {
			textold = text;

			text = text.replace("@amp;", "&");
			text = text.replace('@quot;', '"');
			text = text.replace("@lt;", "<");
			text = text.replace("@gt;", ">");
			text = text.replace("@#146;", "'");
			text = text.replace("@nbsp;", " ");
			text = text.replace("&nbsp;", " ");
			text = text.replace("&#10;", "\r");
			text = text.replace("&#13;", "\n");
		} while (textold != text);
		return text;
	} else
		return '';

}

function buildJsonArrayStr(pkey,pvalue){
	var str='';
 if(pkey.length>0){
	if(pkey[0].value != ''){
	 str+='[';
	for (var i=0;i<pkey.length;i++) {
		if (pkey[i].value != '' && pvalue[i].value != '' ){
				str += '{';
				str += pkey[i].name +':\''+pkey[i].value+'\',';
				str += pvalue[i].name +':\''+pvalue[i].value+'\'';
				str += '},';
		}
	}
	str += ']';
  }
 }
	return str;
}

//根据mapping str获取data array
function parseRelStr(str) {
	var obj = eval(str);
	if (obj instanceof Array) {
		return obj;
	} else {
		return new Array();	
	}
	
}
// 去所有空格
String.prototype.trimAll = function() {
	return this.replace(/(^\s*)|(\s*)|(\s*$)/g, "");
};

String.prototype.endWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length)
		return false;
	if (this.substring(this.length - str.length) == str)
		return true;
	else
		return false;
	return true;
};

String.prototype.startWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length)
		return false;
	if (this.substr(0, str.length) == str)
		return true;
	else
		return false;
	return true;
};

Array.prototype.contains = function (elem) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == elem) {
			return true;
		}
	}
	return false;
};

Array.prototype.toCString = function (c) {
	var splits = ",";
	if (c){splits=c;}
	var rtn = "";
	for (var i = 0; i < this.length; i++) {
		if (i >0 ) {
			rtn +=splits;
		}
		rtn += this[i];
	}
	return rtn;
};

/**
 * 
 */
Date.prototype.format = function(mask) {      
    var d = this;      
    var zeroize = function (value, length) {      
        if (!length) length = 2;      
        value = String(value);      
        for (var i = 0, zeros = ''; i < (length - value.length); i++) {      
            zeros += '0';      
        }      
        return zeros + value;      
    };        
    return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function($0) {      
        switch($0) {      
            case 'd':   return d.getDate();      
            case 'dd':  return zeroize(d.getDate());      
            case 'ddd': return ['Sun','Mon','Tue','Wed','Thr','Fri','Sat'][d.getDay()];      
            case 'dddd':    return ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'][d.getDay()];      
            case 'M':   return d.getMonth() + 1;      
            case 'MM':  return zeroize(d.getMonth() + 1);      
            case 'MMM': return ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'][d.getMonth()];      
            case 'MMMM':    return ['January','February','March','April','May','June','July','August','September','October','November','December'][d.getMonth()];      
            case 'yy':  return String(d.getFullYear()).substr(2);      
            case 'yyyy':    return d.getFullYear();      
            case 'h':   return d.getHours() % 12 || 12;      
            case 'hh':  return zeroize(d.getHours() % 12 || 12);      
            case 'H':   return d.getHours();      
            case 'HH':  return zeroize(d.getHours());      
            case 'm':   return d.getMinutes();      
            case 'mm':  return zeroize(d.getMinutes());      
            case 's':   return d.getSeconds();      
            case 'ss':  return zeroize(d.getSeconds());      
            case 'l':   return zeroize(d.getMilliseconds(), 3);      
            case 'L':   var m = d.getMilliseconds();      
                    if (m > 99) m = Math.round(m / 10);      
                    return zeroize(m);      
            case 'tt':  return d.getHours() < 12 ? 'am' : 'pm';      
            case 'TT':  return d.getHours() < 12 ? 'AM' : 'PM';      
            case 'Z':   return d.toUTCString().match(/[A-Z]+$/);      
            // Return quoted strings with the surrounding quotes removed      
            default:    return $0.substr(1, $0.length - 2);      
        }      
    });      
};   

/**
 * 获取本地Cookie
 * 
 * @param cookie_name
 *            cookie名称
 * @return
 */
function getCookie(cookie_name) {
	var allcookies = document.cookie;
	var cookie_pos = allcookies.indexOf(cookie_name);
	// 如果找到了索引，就代表cookie存在，
	// 反之，就说明不存在。
	if (cookie_pos != -1) {
		// 把cookie_pos放在值的开始，只要给值加1即可。
		cookie_pos += cookie_name.length + 1;
		var cookie_end = allcookies.indexOf(";", cookie_pos);
		if (cookie_end == -1) {
			cookie_end = allcookies.length;
		}
		return unescape(allcookies.substring(cookie_pos, cookie_end));
	}
	return null;
}

/**
 * 显示错误信息
 * 
 * @author Andy
 * @date 2011-09-20
 */
function showErrorDiv (divid, massage) {
	showMessage(divid, massage);
}

function serialize(eid, tagName){
	var toolbar = document.getElementById(eid);
	var elems = toolbar.getElementsByTagName(tagName);
	var alist = new Array();
	var vlist = new Array();
	for (var i = 0; i < elems.length; i++) {
		var fieldName = elems[i].name;
		if (!alist.contains(fieldName)){
			alist.push(fieldName);
			vlist.push(fieldName+'='+ev_getValue(fieldName));
		}
	}
	return vlist.toCString('&');
}

