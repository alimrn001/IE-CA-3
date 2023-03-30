<%@ page import="com.baloot.IE_CA_3.Baloot.Baloot" %><%--
  Created by IntelliJ IDEA.
  User: mehrani
  Date: ۲۰۲۳/۳/۳۰
  Time: ۴:۰۷
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Baloot baloot = Baloot.getInstance();
    String exceptionMsg = baloot.getCurrentSystemException();
%>

<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
</head>
<body>
    <h1>Failed to execute your function !</h1>
    <h2>Details : </h2>
    <h2>    <%=exceptionMsg%></h2>
</body>
</html>
