<c:choose>
	<c:when test="${datas.pageNo != null }">
<input type="hidden" name="_currpage" value='${datas.pageNo }' />
	</c:when>
	<c:otherwise>
<input type="hidden" name="_currpage" value='${param._currpage }' />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${datas.linesPerPage != null }">
<input type="hidden" name="_pagelines" value='${datas.linesPerPage }' />
	</c:when>
	<c:otherwise>
<input type="hidden" name="_pagelines" value='${param._pagelines }' />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${datas.rowCount != null }">
<input type="hidden" name="_rowcount" value='${datas.rowCount }' />
	</c:when>
	<c:otherwise>
<input type="hidden" name="_rowcount" value='${param._rowcount }' />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${param.domain != null }">
<input type="hidden" name="domain" value='${param.domain }' />
	</c:when>
	<c:otherwise>
<input type="hidden" name="domain" value='${sessionScope.FRONT_USER.domainid }' />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${param.application != null }">
<input type="hidden" name="application" value='${param.application }' />
	</c:when>
	<c:when test="${content.applicationid != null }">
<input type="hidden" name="application" value='${content.applicationid }' />
	</c:when>
	<c:otherwise>
<input type="hidden" name="application" value='${application }' />
	</c:otherwise>
</c:choose>
