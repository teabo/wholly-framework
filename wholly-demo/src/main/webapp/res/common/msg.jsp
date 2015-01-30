<%@ page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/component/msgbox/transparent_message.js"></script>

<script>
var errorMsg;
var noticeMsg;
function showMessage(key, content) {
	if (content){
		$("#"+key).html(content);
	} 
	//$("#"+key).show();
	eval(key + "Msg.show()");
	if(key=="error"){
		hideMessage("notice");
	}
}
function hideMessage(key) {
	$("#"+key).html("");
	//$("#"+key).hide();
	eval(key + "Msg.hide()");
}

function initMSG(){
	var keys = ["error","notice"];
	errorMsg = new TransparentMenu(keys[0], {hideDelay:8});
	noticeMsg = new TransparentMenu(keys[1], {hideDelay:3, hideMode: "click"});
	for(var i=0; i<keys.length; i++) {
		var str = $("#"+keys[i]).html();
	    if (str && $.trim(str)!="") {
			showMessage(keys[i]);
		} 
	}
}

$(function(){
	initMSG();
});
</script>


<div id="error" style="display:none;" class="transparent_message transparent_error" onclick="$(this).css('display','none')">
	<c:if test="${hasFieldErrors }">
		<c:forEach var="item" items="${fieldErrors}" varStatus="index">
			*${item.value }&nbsp;&nbsp;
		</c:forEach>
	</c:if>
</div>

	
<div id="notice" style="display:none;" class="transparent_message transparent_notice" onclick="$(this).css('display','none')">	
	<c:if test="${hasActionMessages }">
		<c:forEach var="item" items="${actionMessages}" varStatus="index">
			${item }<br/>
		</c:forEach>
	</c:if>
</div>
