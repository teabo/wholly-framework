<%@page import="com.whollyframework.constans.Environment"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=Environment.getInstance().getWebSiteTitle() %>-登录</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/res/css/login/login.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/res/css/login/login_loading.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/res/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/js/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/component/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/component/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
function getLoadingHtml(msg) { 
		if(!msg) msg = "加载"; 
		var html = "<div id='loadingDiv'>" 
		+ "<div style='height: 1325px; display: none; opacity: 0;' class='overlay'></div>" 
		+ "<div style='opacity: 0; margin-top: 250px;' id='AjaxLoading' class='showbox'>" 
		+ "<div class='loadingWord'>" 
		+ "<img src='${pageContext.request.contextPath}/res/images/main/wait.gif' />" + msg + "中，请稍候..."  
		+ "</div>" 
		+ "</div>" 
		+ "<div style='height: 1200px;'></div>" 
		+ "</div>"; 
		return html; 
} 

		function ajaxLoadingInit(msg) { 
			var loadingDiv = getLoadingHtml(msg); 
			var h = $(document).height(); 
			$(".overlay").css({"height": h}); 
			var div = $("body").find("#loadingDiv"); 
			div.remove(); 
			$("body").append($(loadingDiv)); 
		} 
	/** 
	* 开始显示loading，在ajax执行之前调用 
	* @param msg 操作消息，"加载", "保存", "删除" 
	*/ 
	function startLoading(msg) { 
		ajaxLoadingInit(msg); 
		$(".overlay").css({'display':'block','opacity':'0.8'}); 
		$(".showbox").stop(true).animate({'margin-top':'300px','opacity':'1'},200); 
	} 
	/** 
	* 加载完成后隐藏，在ajax执行完成后（complete）调用 
	*/ 
	function endLoading() { 
		$(".showbox").stop(true).animate({'margin-top':'250px','opacity':'0'},400); 
		$(".overlay").css({'display':'none','opacity':'0'}); 
	} 
	
	 var img = $("<img id=\"progressImgage\"  src=\"${pageContext.request.contextPath}/res/images/main/wait.gif\" />"); //Loading小图标
//	 var img = $("<div class='loadingWord'><img id=\"progressImgage\"  src=\"${pageContext.request.contextPath}/res/images/main/wait.gif\" />"+ "登陆中..."+ "</div>" ); //Loading小图标
     var mask = $("<div id=\"maskOfProgressImage\"></div>").addClass("mask").addClass("hide"); //Div遮罩
     var PositionStyle = "fixed";
     
     function endLoadingEffect() {
    	 img.hide();
         mask.hide();
     }
     
     function loadingEffect(aimDiv) {
    	//是否将Loading固定在aimDiv中操作,否则默认为全屏Loading遮罩
         if ($("#"+aimDiv) != null && $("#"+aimDiv) != "" && $("#"+aimDiv) != undefined) {
             $("#"+aimDiv).css("position", "relative").append(img).append(mask);
             PositionStyle = "absolute";
         }
         else {
             $("body").append(img).append(mask);
         }
         img.css({
             "z-index": "2000",
             "display": "none"
         });
         mask.css({
             "position": PositionStyle,
             "top": "0",
             "right": "0",
             "bottom": "0",
             "left": "0",
             "z-index": "1000",
             "background-color": "#484848",
             "display": "none"
         });
         
         img.show().css({
             "position": PositionStyle,
             "top": "50%",
             "left": "50%",
             "margin-top": function () { return -1 * img.height() / 2; },
             "margin-left": function () { return -1 * img.width() / 2; }
         });
         mask.show().css("opacity", "0.5");
         mask.show().css("filter", "alpha(opacity=50)");
     }

	$(document).ready(function() {
		/** 登录处理 */
		var loginForm = $("#login-form").ajaxForm({
			url : "${pageContext.request.contextPath}/admin/login.action",
			dataType : "json",
			success : function(response) {
// 				$.messager.progress("close");
				// endLoading();
				endLoadingEffect();
				
				if (response.code === 0) {
					window.location.href = "${pageContext.request.contextPath}/control/index.action";
				} else {
// 					$("#login-error").text(response.message);
					showMessage('error',response.message);
					
					loadimage();
				}
			}
		});
		/** 登录校验 */
		$("#login-submit").bind("click", function() {
// 			$.messager.progress();
 			//$("#login-form").append("<div><img src='${pageContext.request.contextPath}/res/images/main/wait.gif' /><div>");
// 			startLoading("登录");
			loadingEffect("login-form");
 			
			if (loginForm.form("validate")) {
				loginForm.submit();
			} else {
				$.messager.progress("close");
			}
		});
		$("#login-reset").bind("click", function() {
			$("#username").val("");
			$("#password").val("");
		});
		loadimage();
	});
	
	$(function(){
		document.onkeydown = function(e){ 
		    var ev = document.all ? window.event : e;
		    if(ev.keyCode==13) {
		    	$("#login-submit").click();
		     }
		};
	});   

	function loadimage() {
		document.getElementById("auimg").src = "${pageContext.request.contextPath}/s/image?"
				+ Math.random();
	}
	
	function showMessage(key, content) {
		if (content) $("#"+key).html('<span class="alert_tip">'+content+'</span>');
		if ("notice"==key)
			msgkey = key;
		$("#"+key).css('display','');
		//$("#"+key).css("color","red");
	}
	
	</script>
	</head>
	<body class="login_bg"  style="overflow-x: hidden;overflow-y: hidden;overflow: hidden;">
	 <!--头部部分-->
		<div class="login_top_bg">
			<div class="logo">
				<img src="${pageContext.request.contextPath}/res/images/login/login_logo.jpg" />
			</div>
		</div>
		<!--中间主体部分-->
		<div class="login_middle_bg">
			<div class="login_content_bg">
				<div class="form_position">
					<form id="login-form" method="post" style="position:relative;">
						<div id="error" style="display:none;" class="transparent_message transparent_error" onclick="$(this).css('display','none')">
						</div>
						<table style="width:100%;margin-top:20px;border-collapse:collapse;font-size:12px;">
							<tr class="tr1">
								<td class="td_left">用户名：</td>
								<td class="td_right"><input type="text" class="txt username" id="username" name="username" value="admin"
	                            data-options="required:true,validType:'length[2,16]'" /></td>
							</tr>
							<tr class="tr2">
								<td class="td_left">密&nbsp;&nbsp;&nbsp;&nbsp;码：</td>
								<td class="td_right"><input type="password" class="txt pwd" id="password" name="password" value="123"
	                            data-options="required:true,validType:'length[4,16]'" ></td>
							</tr>
							<tr class="tr3">
								<td>&nbsp;</td>
								<td class="td_right">
									<input type="text" class="validate_num" value="输入验证码" onfocus="this.value='';" name="authImg" data-options="required:true,validType:'length[1,6]'" />
									<img id="auimg" src="${pageContext.request.contextPath}/res/images/login/validate_num.jpg" alt="验证码" width="84" 
										height="36" onclick="javascript:loadimage();" class="validatenum_img" />  
								</td>
							</tr>
							<tr class="tr4">
								<td>&nbsp;</td>
								<td class="td_right">
									<input id="login-submit" type="button" value="登&nbsp;&nbsp;录" title="登录" class="form_btn enter" />
									<input id="login-reset" type="button" value="重&nbsp;&nbsp;置" title="重置" class="form_btn clear" />
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
		<!--底部版权-->
		<div class="login_footer">广州阳光耐特电子有限公司&nbsp;&nbsp;技术支持 客户端要求：IE6.0或以上，1024*768像素或以上&nbsp;&nbsp;版本：1.0.0_34125</div>
	</body>
</html>
