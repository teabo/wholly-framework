<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<s:token></s:token>
<div id="activityTable" class="list_butt">
<s:if test="formPurview.saveAndNew">
	<input id="btnSaveAndNew" name='button_act' disabled="disabled" type="button" value="保存并新建" onclick="javascript:dosubmit('saveAndNew');"  class="butt long" />
</s:if>
<s:if test="formPurview.saveBtn">
	<input  id="btnSave" name='button_act' disabled="disabled" type="button" value="保存"  onclick="javascript:dosubmit();" class="butt short"/>
</s:if>
<s:if test="formPurview.exitBtn">
	<input type="button" value="返回"  onclick='javascript:doexit();' class="butt short" />
</s:if>
</div>
