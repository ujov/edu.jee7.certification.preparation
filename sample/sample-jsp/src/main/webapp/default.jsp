<%@ page import="java.io.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Hello World JSP</title>
</head>
<body>
	<p>
		Today is
		<%
	Date date = new Date();
	out.print("<h2 align = \"center\">" + date.toString() + "</h2>");
	%>
	</p>
	<hr>
	<jsp:useBean id="familiy" class="edu.jee7.certification.preparation.jsp.Familiy" type="edu.jee7.certification.preparation.jsp.Familiy"/>
	<c:out value="${familiy}" />
	<c:forEach items="${familiy.members}" var="member">
		<br>
	</c:forEach>
	<hr>
	<h2>Ages</h2>
	<c:out value="${familiy.memberArr[1].age}" />
</body>
</html>