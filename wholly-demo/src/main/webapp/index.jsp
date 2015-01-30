<%@ page session="false" pageEncoding="UTF-8"%>

<%
RequestDispatcher dispatcher = request.getRequestDispatcher("/security/login.jsp");
dispatcher.forward(request, response);
%>

