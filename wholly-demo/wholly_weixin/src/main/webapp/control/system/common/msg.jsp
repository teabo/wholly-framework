<%@ page pageEncoding="UTF-8" isELIgnored="false"%>
<script>
var msgkey;
var onloaded = false;
function showMessage(key, content) {
	if (content) $("#"+key).html(content);
	if ("notice"==key)
		msgkey = key;
	$("#"+key).css('display','');
}
function hideMessage(key) {
	$("#"+key).html("");
	msgkey ="";
	$("#"+key).css('display','none');
}

function initMSG(){
	var keys = ["error","notice"];
	for(var i=0; i<keys.length; i++) {
		var html = $("#"+keys[i]).html();
		if (html.trimAll()!='') {
			showMessage(keys[i]);
		}
	}
}
$('body').click(function() {
	if (onloaded){
		if (msgkey){
			$("#"+msgkey).css('display','none');
			msgkey = "";
		}
	}else{
		onloaded = true;
	}
});

$(document).ready(function() {
	initMSG();
});
</script>
<div id="error" style="display:none;" class="transparent_message transparent_error" onclick="$(this).css('display','none')">
	<s:if test="hasFieldErrors()">
		<s:iterator value="fieldErrors">   
     		<s:iterator value="value"> 
				*<s:property />&nbsp;&nbsp;
			</s:iterator>
		</s:iterator>
	</s:if>
</div>	
<div id="notice" style="display:none;" class="transparent_message transparent_notice" onclick="$(this).css('display','none')">	
	<s:if test="hasActionMessages()">
		<s:iterator value="actionMessages">
			<s:property /><br/>
		</s:iterator>
	</s:if>
</div>