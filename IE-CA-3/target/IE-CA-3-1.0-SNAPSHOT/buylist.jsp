<%@ page import="com.baloot.IE_CA_3.Baloot.Baloot" %>
<%@ page import="com.baloot.IE_CA_3.Baloot.Commodity.Commodity" %>
<%@ page import="java.util.List" %>
<%@ page import="com.baloot.IE_CA_3.Baloot.User.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.DecimalFormat" %><%--
  Created by IntelliJ IDEA.
  User: mehrani
  Date: ۲۰۲۳/۳/۳۰
  Time: ۵:۵۵
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = null;
    int totalBuyListPrice = 0;
    Baloot baloot = Baloot.getInstance();
    List<Commodity> userBuyListCommodities = new ArrayList<>();
    try {
        user = baloot.getBalootUser(baloot.getLoggedInUsername());
        userBuyListCommodities = new ArrayList<>(baloot.getUserBuyListCommodities(user.getUsername()).values());
        totalBuyListPrice = baloot.getUserBuyListTotalPrice(user.getUsername());
    }
    catch (Exception e) {
        e.printStackTrace();
    }
%>

<html lang="en"><head>
    <meta charset="UTF-8">
    <title>User</title>
    <style>
        li {
            padding: 5px
        }
        table{
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
<ul>
    <li id="username">Username: <%=user.getUsername()%></li>
    <li id="email">Email: <%=user.getEmail()%></li>
    <li id="birthDate">Birth Date: <%=user.getBirthDate().toString()%></li>
    <li id="address"><%=user.getAddress()%></li>
    <li id="credit">Credit: <%=user.getCredit()%></li>
    <li>Current Buy List Price: <%=totalBuyListPrice%></li>
    <li>
        <a href="credit">Add Credit</a>
    </li>
    <li>
        <form action="" method="POST">
            <label>Submit & Pay</label>
            <input id="form_payment" type="hidden" name="userId" value="Farhad">
            <br>
            <label>discount code</label>
            <input id="coupon_code" type="text" name="discount_code" value="">
            <button type="submit" name="action" value="payment">Payment</button>
        </form>
    </li>
</ul>
<table>
    <caption>
        <h2>Buy List</h2>
    </caption>
    <tbody><tr>
        <th>Id</th>
        <th>Name</th>
        <th>Provider Name</th>
        <th>Price</th>
        <th>Categories</th>
        <th>Rating</th>
        <th>In Stock</th>
        <th></th>
        <th></th>
    </tr>

    <%for(Commodity commodity : userBuyListCommodities) { %>
        <tr>
            <td><%=commodity.getId()%></td>
            <td><%=commodity.getName()%></td>
            <td><%=baloot.getProviderNameById(commodity.getProviderId())%></td>
            <td><%=commodity.getPrice()%></td>
            <td><%=commodity.getCategoriesAsString()%></td>
            <td><%=new DecimalFormat("0.00").format(commodity.getRating())%></td>
            <td><%=commodity.getInStock()%></td>
            <td><a href="/commodities/<%=commodity.getId()%>">Link</a></td>
            <td>
                <form action="" method="POST">
                    <input id="form_commodity_id" type="hidden" name="commodityId" value=<%=commodity.getId()%>>
                    <button type="submit" name="action" value="remove">Remove</button>
                </form>
            </td>
        </tr>
    <%}%>
    </tbody>
</table>
</body>
</html>