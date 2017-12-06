<%@page import="com.whollyframework.constants.SysEnvironment"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
	String contextPath = request.getContextPath();
	request.setAttribute("ctx", contextPath);
%>
<script type="text/javascript">
var contextPath = '<%=contextPath%>';
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=SysEnvironment.getInstance().getWebSiteTitle() %></title>
<link rel="shortcut icon" href="${ctx}/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="${ctx}/res/css/portal/base.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/res/css/portal/lefttree.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/res/css/portal/rightcom.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/res/css/portal/jquery.tableui.css" />
<script type="text/javascript" src="${ctx}/res/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctx}/res/js/base.js"></script>
<style type="text/css">

ul.menu li.selected a {
	font-weight: bold;
	color: #224076;
}

ul.leftnav li.selected,ul.leftnav li.selected a {
	font-weight: bold;
	color: #214C9B;
	background: #fff;
	border-right: 0px;
	padding-right: 5.2%;
	_padding-right: 0%;
	border-radius: 3px;
	font-size: 12px;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$('ul.menu li a').click(function() {
			$('ul.menu li').removeClass('selected');
			$(this).parent('li').addClass('selected');
		});
	});
	$(document).ready(function() {
		$('ul.leftnav li a').click(function() {
			$('ul.leftnav li').removeClass('selected');
			$(this).parent('li').addClass('selected');
		});
	});
	$(function() {
		$(".ny_zblb1 ul li").click(function() {
			var thisSpan = $(this);
			$(".ny_zblb1 ul li ul").prev("a").removeClass("cur");
			$("ul", this).prev("a").addClass("cur");
			$(this).children("ul").slideDown("fast");
			$(this).siblings().children("ul").slideUp("fast");
		})
	});
	$(function() {
		$(".table_solid").tableUI();
	});
	$(function() {
		$('.click_addalert').click(function() {
			$('.add_alert').show();
		});
		$('.alert_close,.alert_del').click(function() {
			$('.add_alert').hide();
		});
	});
</script>























