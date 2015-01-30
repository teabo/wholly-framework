<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/res/common/tags.jsp" %>
<html>
  <head>
    <title>成功提示</title>
    <%@include file="/res/common/index.jsp" %>
    <script type="text/javascript">
    /**
	* @desc 刷新父页面
	*/
	function OKClick()
	{
		CEISO.dialog.doReturn("OK");
	}

	jQuery(document).ready(function() {
		OKClick();
	});
    </script>
  </head>
  
  <body>
    请求处理成功！
  </body>
  
</html>
