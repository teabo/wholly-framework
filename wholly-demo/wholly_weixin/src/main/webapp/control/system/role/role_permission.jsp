<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/res/common/tags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>授权管理</title>
<%
	String contextPath = request.getContextPath();
%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/res/css/main/tab.css" />
<%@include file="/res/common/index.jsp"%>
<%@include file="/res/js/document/common.jsp"%>
<script type="text/javascript" src="<%=contextPath %>/res/component/dialog/jquery.teabo.dialog.js"></script>

<script type="text/javascript">
	
	function permissionBind(){
		//角色id字符串
 		var roleid = curr_role;
		if(roleid==""){
			alert("请选择角色！");
		}else{
			//var resChecks = jQuery("#res_tree").tree('getChecked');
			var resChecks = jQuery("#res_tree").tree('getChecked', ['checked','indeterminate']);
			var resArr = new Array();
			$.each(resChecks,function(i,n){
				resArr.push(n.id);
			});
			var resids = resArr.join(",");
			var QString = "roleid=" + roleid +"&resids="+resids;
			//alert(QString);
		 	jQuery.post(contextPath+'/control/system/role/rolePermissionBind.action',
					QString, function(data){
					if(data.code==1){
						alert("保存成功");						
					}else{
						//alert(data.msg);
					}
				}); 
		}
	}
	
	function loadRoles(){
		//role_tree
		var ul = jQuery("#role_tree");
					ul.html("");
		jQuery.each(${roles},function(k,v){
			//alert(v.id+"=="+v.name);
			var li = jQuery("<li style='list-style-type:none;margin-top:5px;margin-left:10px;'/>");
			var label = jQuery("<label />");
			//var radio = jQuery("<input type='radio' onclick='loadConfigFields(\""+n.id+"\")' name='configRadio' style='margin-right:2px;'/>");
			var a = jQuery("<a onclick='loadResource(\""+v.id+"\")' id=\""+v.id+"\"/>");
			a.css("color","black");
			a.html(v.name);
	 		li.hover(
				  function () {
				    $(this).css("backgroundColor","#B8D2FC");
				  },
				  function () {
				    $(this).css("backgroundColor","");
				  }
				); 
			label.append(a);
			//label.append(n.name);
			li.append(label);
			ul.append(li);
		});
	}
	
	var curr_role = "";
	function loadResource(role_id){
		jQuery("#"+curr_role).css("color","black").css("font-weight","normal");
		jQuery("#"+role_id).css("color","blue").css("font-weight","bold");
		curr_role = role_id;
		//alert(curr_role);
		jQuery.ajax({
            url: contextPath+"/control/system/role/rolePermissionLoadAjax.action?roleid="+role_id,
            type: "post",
            async: "true",  // true 为异步，false 为同步
            dataType: "json",
            success: function(data){
            	if(data.code==1){
            		//jQuery.each(data.data,function);
            		jQuery("#res_tree").tree('loadData',data.data);
            	}
           }
  		 });
	}
	
	jQuery(function(){
		//alert("start");
		//var grid_prop = countTableWidth();
		loadRoles();
		//loadQueryConfigsByApp(app_id);
		jQuery("#res_tree").tree({
			checkbox:true,
			lines:true,
			cascadeCheck : true
		});
		jQuery("#res_tree").tree('loadData',${menuTrees});
		loadRoles();
		if(jQuery("#role_tree a:first")){
			var first_rid = jQuery("#role_tree a:first").attr("id");
			loadResource(first_rid);			
		}
		//alert("end");
		adjustDocumentLayout();
	});
</script>

<style>
.left_title {
	cursor: pointer;
}

.left_title h2 {
	background:lightblue;
	color: #002588;
	line-height: 24px;
	padding: 0 8px 0 20px;
	font-size:14px;
}

label:HOVER {
	cursor: pointer;
}

.short{
	border:0px;
	height:25px;
	color:#fff;
	letter-spacing:1px;
	margin-right:15px;
	width:45px;
	background:url("${pageContext.request.contextPath}/res/images/main/short.gif") no-repeat;
}

.role_ul{
	margin-left : 20px;
	padding-top : 5px;
}

.add{
    color: #002588;
    cursor: pointer;
    display: inline-block;
    font-size: 13px;
    height: 24px;
    line-height: 24px;
    padding-left: 25px;
    padding-right: 8px;
    paddinf-bottom:2px;
    margin-right:6px;
    float:right;
    border:2px solid #87b8f2;
	background:#fff url("${pageContext.request.contextPath}/res/images/act/add.jpg") no-repeat 4px center;
}

.click{
	color:red;
	font-weight: bold;
}
.outclick{
	color:black;
	font-weight: normal;
}
</style>
</head>
<body>
	<body style="padding: 0; margin: 0;">
		<div id="container" class="front-align-top">
			<form id="formItem" name="formItem" action="save" method="post">
				<div id="contentTable" class="front-scroll-auto" align="center">
					<div id="top_div">
						<h2 class="title">权限分配</h2>
					</div>
					<div id="layout_div" class="front-boder" 
						style="width: 98%; height: 350px; padding: 0;margin-top:5px;">
						<div id="layout_left" class="front-boder"
							style="width: 250px; text-align: left; float: left;">
							<div id="left_top" class="left_title">
								<h2>角色选择</h2>
							</div>
							<div id="left_content" style="color: black">
								<ul id="role_tree" class="role_ul" style="line-height:20px;">
								</ul>
							</div>
						</div>
						<div id="layout_right" class="front-boder"
							style="text-align: left; float: left; margin-left: 5px;">
							<div id="rigth_top" class="left_title">
								<h2>资源选择<input type="button" id="btns-add" class="add" onclick="permissionBind();" value="保存" /></h2>
							</div>
							<div id="rigth_content" >
								<div class="nTab">
									<div class="TabTitle">
										<ul class="myTab0">
											<li class="active"  onclick="nTabs(this,0);">菜单资源</li>
										</ul>
									</div>
									<div id="TabContent" class="TabContent">
										<div id="myTab0_Content0" style="overflow:auto;">
											<ul id="res_tree" style="margin-left:20px;margin-top:8px;">
											</ul>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</body>
	<script>
		
		function nTabs(thisObj,Num){
			if(thisObj.className == "active") return;
			var tabObj = "myTab0";
			var tabList = jQuery(".myTab0 li");
			for(var i=0; i <tabList.length; i++)
			{
			  if (i == Num)
			  {
			      thisObj.className = "active"; 
			      document.getElementById(tabObj+"_Content"+i).style.display = "block";
			  }else{
				   tabList.get(i).className = "normal"; 
				   document.getElementById(tabObj+"_Content"+i).style.display = "none";
			  }
			} 
		}

		function adjustDocumentLayout() {
			
			var container = document.getElementById("container");
			var activityTable = document.getElementById("activityTable");
			var contentTable = document.getElementById("contentTable");
			var rigth_content = document.getElementById("rigth_content");
			var tabContent = document.getElementById("TabContent");
			var myTab0_Content0 = document.getElementById("myTab0_Content0");
			
			var containerHeight = jQuery(window).height() - 2;
			container.style.height = containerHeight + 'px';
			var contentTableHeight = containerHeight;
			if (activityTable) {
				contentTableHeight = contentTableHeight
						- activityTable.offsetHeight;
			}
			contentTable.style.width = "100%";
			contentTable.style.height = contentTableHeight + 'px';

			var layoutContainer = document.getElementById("layout_div");
			var layout_left = document.getElementById("layout_left");
			var layout_right = document.getElementById("layout_right");
			var top_div = document.getElementById("top_div");
			if (top_div) {
				contentTableHeight = contentTableHeight - top_div.offsetHeight;
			}
			layoutContainer.style.width = "98%";
			layout_right.style.width=(jQuery("#layout_div").width()-260)+ 'px';
			layout_left.style.height = (contentTableHeight - 9) + 'px';
			layout_right.style.height = (contentTableHeight - 9) + 'px';
			rigth_content.style.height = (contentTableHeight - 45)+'px' ;
			layoutContainer.style.height = (contentTableHeight - 7) + 'px';
			
			tabContent.style.height = (contentTableHeight - 70)+'px' ;
			myTab0_Content0.style.height = (contentTableHeight - 70)+'px' ;
			
			container.style.visibility = "visible";
		}
		
		jQuery(window).resize(function() {
			adjustDocumentLayout();
		});
	</script>
</html>
