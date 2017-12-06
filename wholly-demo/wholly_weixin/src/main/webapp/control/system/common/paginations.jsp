<%@ page pageEncoding="utf-8" isELIgnored="false"%>
<%@include file="list.jsp" %>
<c:if test="${datas.pageCount>0 }">
<div id="pageTable" class="pageTips">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr class="lasttr" align="right"><td>
	<o:PageNavigation dpName="datas"
				css="linktag" />
	</td></tr>
</table>
</div>
</c:if>