<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/res/common/inc.jsp"%>
</head>
<body style="visibility: hidden;">
	<!-- 主体内容-->
	<div id="container" class="content" style="visibility: hidden;">
		<div id="lefttree" class="lefttree">
			<!--左侧导航目录树-->
			<div id="ny_zb" class="ny_zb">
				<div class="ny_zblb1">
					<ul class="clearfix">
						<c:forEach items="${menus}" varStatus="vs1" var="menu">
							<li id="fir_${vs1.index }" menu_src='${menu.menuUrlWithParams}'
								menu_name='${menu.name}'><a class="title_leftimg" href="#"
								onclick="changeContent('${menu.menuUrlWithParams}','${menu.name}')"
								<c:if test="${empty menu.subMenu}">style="background:none;"</c:if>>
									${menu.name}<img style="width: 20px;height: 20px" src="${menu.iconUrl}" />
							</a>
								<ul class="leftnav">
									<c:forEach items="${menu.subMenu}" varStatus="vs2" var="smenu">
										<li id="sec_${vs1.index }_${vs2.index }"
											menu_src='${smenu.menuUrlWithParams}'
											menu_name='${smenu.name}' style="padding-left: 20px"><a
											href="#"
											onclick="changeContent('${smenu.menuUrlWithParams}','${smenu.name}')">${smenu.name}</a></li>
									</c:forEach>
								</ul></li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		<div id="rightcom" class="rightcom">
			<div id="rightContent" style="width: 99%; height: 93%; margin: 10px auto;">
				<h4 class="title">
					<span id="smenu_name">网络配置</span>
				</h4>
				<iframe id="content_right_if" style="width: 100%; height: 100%;padding: 0;margin: 0;border:1px solid #DFE3E6;"
					frameborder=0 scrolling="no" src=""></iframe>
			</div>
		</div>
	</div>
</body>
</html>

<script type="text/javascript">
	function changeContent(url, name) {
		if (url == '') {
			return;
		}
		jQuery("#content_right_if").attr("src", url);
		jQuery("#smenu_name").html(name);
	}
	function fitLayout() {
		var container = document.getElementById("container");
		var lefttree = document.getElementById("lefttree");
		var rightcom = document.getElementById("rightcom");
		var rightContent = document.getElementById("rightContent");
		var content_right_if = document.getElementById("content_right_if");
		var ny_zb = document.getElementById("ny_zb");
		var containerHeight = jQuery(window).height() - 4;
		var containerWidth = jQuery(window).width() - 4;

		container.style.height = containerHeight + 'px';
		lefttree.style.height = (containerHeight - 10) + 'px';
		rightcom.style.height = (containerHeight - 22) + 'px';
		rightContent.style.height = (containerHeight - 44) + 'px';
		rightContent.style.width = (containerWidth - lefttree.offsetWidth - 20) + 'px';
		content_right_if.style.width = (containerWidth - lefttree.offsetWidth - 22) + 'px';
		content_right_if.style.height = (containerHeight - 74) + 'px';
		ny_zb.style.height = (containerHeight - 10) + 'px';

		container.style.visibility = "visible";
	}

	jQuery(document).ready(
			function() {
				fitLayout();
				jQuery("#fir_0").trigger("click");
				//alert($("#sec_0_0").length);
				if ($("#sec_0_0").length > 0) {
					jQuery("#sec_0_0").trigger("click");
					jQuery("#sec_0_0").attr("class", "selected");
					changeContent(jQuery("#sec_0_0").attr("menu_src"), jQuery(
							"#sec_0_0").attr("menu_name"));
				} else {
					changeContent(jQuery("#fir_0").attr("menu_src"), jQuery(
							"#fir_0").attr("menu_name"));
				}
			});

	jQuery(window).resize(function() {
		fitLayout();
	});
</script>