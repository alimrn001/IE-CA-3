<%--
  Created by IntelliJ IDEA.
  User: mehrani
  Date: ۲۰۲۳/۳/۲۹
  Time: ۱۶:۱۳
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.baloot.IE_CA_3.Baloot.Baloot" %>
<%@ page import="java.util.List" %>
<%@ page import="com.baloot.IE_CA_3.Baloot.Commodity.Commodity" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Baloot baloot = Baloot.getInstance();
    List<Commodity> commoditiesList;
    if(baloot.searchFilterIsApplied() || baloot.sortingIsAppliedForCommodities()) {
        commoditiesList = baloot.getFilteredCommodities();
    }
    else {
        commoditiesList = new ArrayList<>(baloot.getBalootCommodities().values());
    }
    //List<Commodity> commoditiesList = new ArrayList<>(baloot.getBalootCommodities().values());
%>

<html>
<head>
    <meta charset="UTF-8">
    <title>Commodities</title>
    <style>
        table{
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
    <a href="/IE_CA_3_war_exploded/">Home</a>
    <p id="username">username: <%=baloot.getLoggedInUsername()%></p>
    <br><br>
    <form action="" method="POST">
        <label>Search:</label>
        <input type="text" name="search" value="">
        <button type="submit" name="action" value="search_by_category">Search By Cagtegory</button>
        <button type="submit" name="action" value="search_by_name">Search By Name</button>
        <button type="submit" name="action" value="clear">Clear Search</button>
    </form>
    <br><br>
    <form action="" method="POST">
        <label>Sort By:</label>
        <button type="submit" name="action" value="sort_by_rate">Rate</button>
    </form>
    <br><br>
    <table>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Provider Name</th>
            <th>Price</th>
            <th>Categories</th>
            <th>Rating</th>
            <th>In Stock</th>
            <th>Links</th>
        </tr>
        <% for(Commodity commodity : commoditiesList) { %>
            <tr>
                <td><%=commodity.getId()%></td>
                <td><%=commodity.getName()%></td>
                <td><%=baloot.getProviderNameById(commodity.getProviderId())%></td>
                <td><%=commodity.getPrice()%></td>
                <td><%=commodity.getCategoriesAsString()%></td>
                <td><%=new DecimalFormat("0.00").format(commodity.getRating())%></td>
                <td><%=commodity.getInStock()%></td>
            <td><a href="/commodities/<%=commodity.getId()%>">Link</a></td>
            </tr>
        <%}%>
    </table>
</body>
</html>
