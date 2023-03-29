<%@ page import="com.baloot.IE_CA_3.Baloot.Baloot" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
<body>
<%
    String loggedInUser = Baloot.getInstance().getLoggedInUsername();
%>
<ul>
    <li id="std_id">Student Id: <%=loggedInUser%></li>
    <li>
        <a href="commodities">Commodities</a>
    </li>
    <li>
        <a href="buyList">Buy List</a>
    </li>
    <li>
        <a href="credit">Add Credit</a>
    </li>
    <li>
        <a href="logout">Log Out</a>
    </li>
</ul>
</body>
</html>