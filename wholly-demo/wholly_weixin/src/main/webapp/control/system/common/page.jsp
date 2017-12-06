<%@ page language="java" pageEncoding="UTF-8" %>
<div id="searchFormTable" style="display: none;">
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
	<c:when test="${param.s_unit_id != null }">
<input type="hidden" name="s_unit_id" value='${param.s_unit_id }' />
	</c:when>
	<c:when test="${content.unit_id != null }">
<input type="hidden" name="s_unit_id" value='${content.unit_id }' />
	</c:when>
</c:choose>

<c:choose>
	<c:when test="${param.s_app_id != null }">
<input type="hidden" name="s_app_id" value='${param.s_app_id }' />
	</c:when>
	<c:when test="${content.app_id != null }">
<input type="hidden" name="s_app_id" value='${content.app_id }' />
	</c:when>
</c:choose>
</div>


<input type="hidden" id="id" name="content.id" value='${content.id }' />
<input type="hidden" id="istemp" name="content.istemp" value='${content.istemp }' />
<input type="hidden" id="version" name="content.version" value='${content.version }' />
<!--起草人ID -->
<input type="hidden" id="authorid" name="content.authorid" value="${content.authorid}" />
<!--起草人名称 -->
<input type="hidden" id="author" name="content.author" value="${content.author}" />
<!--起草时间 -->
<input type="hidden" id="created" name="content.created" value="${content.createdStr}" />