
/**
 * 数字输入
 * onKeypress
 * @returns {Boolean}
 */
function checkNumber(){
	var key=event.keyCode;
	if(key>=48&&key<=57||key==110||key>=96&&key<=105||key==8||key==46||key==190){
		return true;
	}
	return false;
}

/**
 * 数字输入 包括 ":"
 * onKeypress
 * @returns {Boolean}
 */ 
function checkTime(){
	var key=event.keyCode;
	if(key>=48&&key<=57||key==110||key==8||key==46||key==190||key==58){
		return true;
	}
	return false;
}

/**
 * 字符串包含非法字符
 * 
 * @returns {Boolean}
 */ 
function checkNonChar(str){
	var pat=new RegExp("[^a-zA-Z0-9\_\u4e00-\u9fa5]","i");
    if(pat.test(str))   
    {   
        return true;   
    }
    return false;
}


/**
 * 验证是否为数字, 格式: xxx.xx
 * 
 * @author Andy
 * @date 2011-09-20
 */
function validateNumber (number) {
	var patrn=/^([1-9]\d{0,18}|0)(\.\d{1,4})?$/;
	if (!patrn.exec(number)) return false;
	return true;
}

/**
 * 验证是否为数字, 格式: xxx
 * 
 */
function validatePhone (number) {
	var patrn=/^\d{0,15}$/;
	if (!patrn.exec(number)) return false;
	return true;
}

function validateInteger(number) {
	var patrn=/^([1-9]\d{0,10}|0)$/;
	if (!patrn.exec(number) || number>2147483648) return false;
	return true;
}

var vobj = null;
function validateRate(number) {
	var patrn=/^([1-9]\d{0,3}|0)$/;
	if (!patrn.exec(obj.value) || obj.value>100) {
		obj.focus();
		isSubmit = false;
		vobj = obj.name;
		showMessage('error',"请输入正确的百分比！");
	}else{
		if (!isSubmit && obj.name==vobj){
			hideMessage('error');
			isSubmit = true;
			vobj = null;
		}
	}
}

function validateInt(obj) {
	var patrn=/^([1-9]\d{0,10}|0)$/;
	if (!patrn.exec(obj.value) || obj.value>2147483648) {
		obj.focus();
		isSubmit = false;
		vobj = obj.name;
		showMessage('error',"请输入正确的整数(支持最大整数位数10位,最大值不能超过2147483648)！");
	}else{
		if (!isSubmit && obj.name==vobj){
			hideMessage('error');
			isSubmit = true;
			vobj = null;
		}
	}
}

function validateDouble(obj) {
	var patrn=/^([1-9]\d{0,18}|0)(\.\d{1,4})?$/;
	if (!patrn.exec(obj.value)) {
		showMessage('error',"请输入正确的数值(支持最大整数位数19位,小数位数4位)！");
		obj.focus();
		isSubmit = false;
	}else{
		if (!isSubmit && obj.name==vobj){
			hideMessage('error');
			isSubmit = true;
			vobj = null;
		}
	}
}


/**
 * 所有校验
 */
var Validations = {
	"great-than" : function(v, els, args) { // 是否大于args.value
		var isPositiveNumber = Validations["is-positive-number"];
		if (isPositiveNumber(v)) {
			return new Number(v) > args.value;
		}
		return false;
	},
	"equal-great-than" : function(v, els, args) { // 是否大于args.value
		var isPositiveNumber = Validations["is-positive-number"];
		if (isPositiveNumber(v)) {
			return new Number(v) >= args.value;
		}
		return false;
	},
	"is-positive-number" : function(v) {// 是否为正数
		return /^\d+\.*\d*$/.test(v);
	},
	"required-one" : function(v, els) { // 是否必须选中一个
		return $A(els).any(function(el) {
					return $F(el);
				});
	},
	"required" : function(v) { // 是否必须
		if (v && v != '0') {
			return true;
		}
		return false;
	}
};

/**
 * 通过返回true, 不通过返回false, 依赖Prototype validator = {fieldName: "需校验的字段名称", type:
 * "调用的校验function(如：required)", msg:"错误信息" }; validators = [validator1,
 * validator2];
 */
function doValidate(validators) {
	var msg = "";
	for (var i = 0; i < validators.length; i++) {
		var validator = validators[i];
		var els = document.getElementsByName(validator.fieldName);
		var validation = Validations[validator.type];
		var isPassed = false;
		if (els && els.length > 1) {
			isPassed = validation($F(els[0]), els);
		} else {
			isPassed = validation($F(els[0]), els, validator.args);
		}
		if (!isPassed)
			msg += validator.msg + ", ";
	}

	if (validators && validators.length > 0) {
		msg = msg.substring(0, msg.lastIndexOf(","));
	}

	var errorId = "error";
	if (msg) {
		if (errorMsg) { // 查看"/common/msg.jsp"
			showMessage(errorId, msg);
		} else {
			jQuery.messager.alert("提示",msg,'info');
		}
		return false;
	} else {
		hideMessage(errorId);
	}
	return true;
}

/**
 * 验证是否安装了flashplayer
 * @returns {___anonymous4783_4824}
 */
function flashChecker() {
	var hasFlash = 0;// 是否安装了flash
	var flashVersion = 0;// flash版本

	if (document.all) {
		var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
		if (swf) {
			hasFlash = 1;
			VSwf = swf.GetVariable("$version");
			flashVersion = parseInt(VSwf.split(" ")[1].split(",")[0]);
		}
	} else {
		if (navigator.plugins && navigator.plugins.length > 0) {
			var swf = navigator.plugins["Shockwave Flash"];
			if (swf) {
				hasFlash = 1;
				var words = swf.description.split(" ");
				for (var i = 0; i < words.length; ++i) {
					if (isNaN(parseInt(words[i])))
						continue;
					flashVersion = parseInt(words[i]);
				}
			}
		}
	}
	return {
		f : hasFlash,
		v : flashVersion
	};
}


function checkSelection(obj) {
	if (obj != null) {
		for (var i = 0; i < obj.length; ++i) {
			if (obj[i].checked) {
				return true;
			}
		}
	}
	return false;
}


function checkDuringDate(startdate, enddate) {
	if (document.getElementById(startdate) != null && document.getElementById(enddate).value == '')
		return true;
	if (document.getElementById(startdate) != null && document.getElementById(enddate).value == '')
		return true;

	var sd = Date.parse(formatdate(document.getElementById(startdate).value));
	var ed = Date.parse(formatdate(document.getElementById(enddate).value));

	return ed >= sd;
}

function checkDates(startdate, startdatename, enddate, enddatename) {
	if (document.getElementById(startdate).value == ''
			|| document.getElementById(startdate).value == '1900-01-01') {
		jQuery.messager.alert("提示",'必需填写' + startdatename,'info');
		return false;
	}

	if (document.getElementById(enddate).value == ''
			|| document.getElementById(enddate).value == '1900-01-01') {
		jQuery.messager.alert("提示",'必需填写' + enddatename,'info');
		return false;
	}

	var sd = Date.parse(formatdate(document.getElementById(startdate).value));
	var ed = Date.parse(formatdate(document.getElementById(enddate).value));
	if (!(ed > sd)) {
		jQuery.messager.alert("提示",enddatename + '必需大于' + startdatename,'info');
		return false;
	}

	return true;
}