<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/res/common/inc.jsp" %>
<script type="text/javascript" src="${ctx}/res/component/dialog/jquery.teabo.dialog.js"></script>

<script type="text/javascript">
function logout() {
	if (confirm('你确定退出登录吗?')) {
		window.location.href = "../security/logout.jsp";
	}
}
function changPWD(){
	$.Dialog.show({
		width : 900,
		height : 500,
		title : "修改密码",
		url : contextPath + "/system/admin/userPwd.action",
		showButton : false
		/* ok : function(result){
			alert(result);
		} */
	});
	
	//system/admin/userPwd.action
}

function changInfo(){
	$.Dialog.show({
		width : 900,
		height : 500,
		title : "用户信息",
		url : contextPath + "/admin/userInfo.action",
		showButton : false
		/* ok : function(result){ 
			alert(result);
		} */
	});
}
</script>

</head>
<body>
	<!--头部-->
	<div id="header" class="header">
		<img class="logo" src="${ctx}/res/images/logo/logo.jpg" />
		<div class="admin">
			<div class="user">
				<span class="curr_user">当前用户：</span><a href="javascript:void(0);"  onClick="" class="real_user" style="cursor: default ;" 
					>${ sessionScope.USER.name}</a>
			</div>
			<div class="modify_pwd">
				<a href="javascript:void(0);" onClick="changPWD();" title="修改密码">修改密码</a>
			</div>
			<div class="quit">
				<a href="javascript:void(0);" onClick="logout();" title="退出">退出</a>
			</div>
		</div>
	</div>
	<!--导航-->
	<div id="nav" class="nav">
		<ul class="menu">
			<li class="first selected"><a href="#"
				onclick="changeModule('000')">首页</a></li>
			<c:forEach items="${topmenus}" var="menu">
				<li><a href="#" onclick="changeModule('${menu.id}')">${menu.name}</a></li>
			</c:forEach>
		</ul>
	</div>
	<!-- 主体内容-->
	<div id="content" class="content" style="visibility: hidden;">
		<iframe id="content_if" style="width:100%" frameborder=0  scrolling=no src=""></iframe>
	</div>
	<!-- 版权-->
	<div id="footer" class="footer">
		<p>版权所有&nbsp;&nbsp;@2010&nbsp;&nbsp;广州阳光耐特电子有限公司&nbsp;&nbsp;版本：1.0.0_34125&nbsp;&nbsp;粤ICP10895</p>
	</div>
</body>
</html>
<script type="text/javascript">
	function changeModule(id) {
		if ('000'==id){
			jQuery("#content_if").attr("src",
					contextPath + "/control/homepage.jsp");
		} else{
			jQuery("#content_if").attr("src",
					contextPath + "/control/module.action?pid=" + id);
		}
	}

	function fitLayout() {
		var header = document.getElementById("header");
		var nav = document.getElementById("nav");
		var footer = document.getElementById("footer");
		var container = document.getElementById("content");
		var content_if = document.getElementById("content_if");
		var containerHeight = jQuery(window).height() - 2;
		var contentTableHeight = containerHeight;
		if (header) {
			contentTableHeight = contentTableHeight - header.offsetHeight;
		}
		if (nav) {
			contentTableHeight = contentTableHeight - nav.offsetHeight;
		}
		if (footer) {
			contentTableHeight = contentTableHeight - footer.offsetHeight;
		}
		container.style.width = "100%";
		container.style.height = contentTableHeight + 'px';
		content_if.style.height = (contentTableHeight) + 'px';
		container.style.visibility = "visible";
	}

	jQuery(document).ready(function() {
		fitLayout();
	});

	jQuery(window).resize(function() {
		fitLayout();
	});
</script>