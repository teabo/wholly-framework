<s:if test="datas.pageNo != null && datas.pageNo!=''">
	<input type="hidden" name="_currpage" value='<s:property value="datas.pageNo"/>' />
</s:if><s:else>
	<input type="hidden" name="_currpage" value='<s:property value="#parameters._currpage"/>' />
</s:else>
<s:if test="datas.linesPerPage != null && datas.linesPerPage!=''">
	<input type="hidden" name="_pagelines" value='<s:property value="datas.linesPerPage"/>' />
</s:if><s:else>
	<input type="hidden" name="_pagelines" value='<s:property value="#parameters._pagelines"/>' />
</s:else>
<s:if test="datas.rowCount != null && datas.rowCount!=''">
 	<input type="hidden" name="_rowcount" value='<s:property value="datas.rowCount"/>' />
</s:if><s:else>
	<input type="hidden" name="_rowcount" value='<s:property value="#parameters._rowcount"/>' />
</s:else>

<s:if test="#parameters._backURL[0]!=null && #parameters._backURL[0]!=''">
<s:hidden name="_backURL" value="%{#parameters._backURL[0]}" />
</s:if>
<s:else>
<input type="hidden" name="_backURL" value='' />
<script>
var _backURL = document.getElementsByName("_backURL")[0];
var url = ""+document.forms[0].action;
if (url.indexOf("//") != -1) {
	url = url.substring(url.indexOf("//")+2);
	if (contextPath!='' && contextPath!='/')
		url = url.substring(url.indexOf(contextPath)+contextPath.length());
	url = url.substring(url.indexOf("/"));
}
_backURL.value = url;
</script>
</s:else>