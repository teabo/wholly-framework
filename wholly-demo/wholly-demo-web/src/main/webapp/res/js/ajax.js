/**
 * 发送ajax GET请求
 * @param url--url 
 * @param parameters(参数) 
 * @param callback(回调方法名，不需要引号,这里只有成功的时候才调用)
 * @param asynchronous (true(异步)|false(同步)) 
 */
var GET = function(url, parameters, callback, asynchronous){
	ajaxRequest("get", url, parameters, callback, asynchronous);
};

/**
 * 发送ajax POST请求
 * @param url--url 
 * @param parameters(参数) 
 * @param callback(回调方法名，不需要引号,这里只有成功的时候才调用)
 * @param asynchronous (true(异步)|false(同步)) 
 */
var POST = function(url, parameters, callback, asynchronous){
	ajaxRequest("post", url, parameters, callback, asynchronous);
};
/**
 * 发送ajax请求 
 * @param method (post/get) 
 * @param url--url 
 * @param parameters(参数) 
 * @param callback(回调方法名，不需要引号,这里只有成功的时候才调用)
 * @param asynchronous (true(异步)|false(同步))
 */
var ajaxRequest = function(method, url, parameters, callback, asynchronous) {
	var transport = getTransport();
	transport.onreadystatechange = function() {
		if (transport.readyState == 4) {
			// HTTP响应已经完全接收才调用
			if (callback) {
				callback(returnValue(transport));
			}
		}
	};
	if (asynchronous==undefined){
		asynchronous = true;
	}
	transport.open(method, url, asynchronous);
	if (!parameters) {
		parameters = null;
	}
	transport.send(parameters);
};

var returnValue = function(transport){
	var rtn = transport.responseText;
	if (rtn){
		var value = eval('(' + rtn + ')');
		if (value==undefined){
			return rtn;
		}
		return value;
	}
	return undefined;
};

var ajaxOptions = {
	contentType : 'application/x-www-form-urlencoded',
	encoding : 'UTF-8',
	evalJSON : true,
	evalJS : true
};
/**
 * 得到ajax对象
 */
var getTransport = function() {
	var xmlHttp;
	try {
		// Firefox, Opera 8.0+, Safari
		xmlHttp = new XMLHttpRequest();
	} catch (e) {
		// Internet Explorer
		try {
			xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {
				alert("您的浏览器不支持AJAX！");
				return false;
			}
		}
	}
	return xmlHttp;
};

var setRequestHeaders = function(transport, method) {
	var headers = {
		'X-Requested-With' : 'XMLHttpRequest',
		'Accept' : 'text/javascript, text/html, application/xml, text/xml, */*'
	};

	if (this.method == 'post') {
		headers['Content-type'] = ajaxOptions.contentType
				+ (ajaxOptions.encoding ? '; charset=' + ajaxOptions.encoding
						: '');

		/*
		 * Force "Connection: close" for older Mozilla browsers to work around a
		 * bug where XMLHttpRequest sends an incorrect Content-length header.
		 * See Mozilla Bugzilla #246651.
		 */
		if (transport.overrideMimeType
				&& (navigator.userAgent.match(/Gecko\/(\d{4})/) || [ 0, 2005 ])[1] < 2005)
			headers['Connection'] = 'close';
	}

	for ( var name in headers)
		transport.setRequestHeader(name, headers[name]);
};