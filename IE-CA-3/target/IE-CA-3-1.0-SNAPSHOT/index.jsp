<%@ page import="com.baloot.IE_CA_3.Baloot.Baloot" %>
<%@ page import="com.baloot.IE_CA_3.Baloot.User.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
<body>
<%
    String loggedInUser = Baloot.getInstance().getLoggedInUsername();
    User user = null;
    try {
        user = Baloot.getInstance().getBalootUser(loggedInUser);
    }
    catch (Exception e) {
        e.printStackTrace();
    }

%>
<ul>
    <li id="username">username: <%=loggedInUser%></li>
    <li id="user_credit">credit: <%=user.getCredit()%></li>
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