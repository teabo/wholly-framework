<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="activityTable" class="activity_box">
<c:if test="${formPurview.saveAndNew ==null}">
	<input type="button" name='button_act' disabled="disabled" id="btn_save" onclick="dosubmit('saveAndNew')" value="保存新建" class="save_new"/>
</c:if>
<c:if test="${formPurview.saveBtn == null }">
	<input type="button" name='button_act' disabled="disabled" id="btn_save" onclick="dosubmit()" value="保&nbsp;&nbsp;存" class="save"/>
</c:if>
<c:if test="${formPurview.exitBtn==null }">
	<input type="button"  id="btn_return" onclick="doexit()" value="返&nbsp;&nbsp;回" class="return"/>
</c:if>

</div>
