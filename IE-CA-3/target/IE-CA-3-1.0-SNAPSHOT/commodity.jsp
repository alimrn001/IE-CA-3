<%@ page import="com.baloot.IE_CA_3.Baloot.Commodity.Commodity" %>
<%@ page import="com.baloot.IE_CA_3.Baloot.Baloot" %>
<%@ page import="com.baloot.IE_CA_3.Baloot.Comment.Comment" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DecimalFormat" %><%--
  Created by IntelliJ IDEA.
  User: mehrani
  Date: ۲۰۲۳/۳/۳۰
  Time: ۱۰:۵۳
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Baloot baloot = Baloot.getInstance();
    int commodityId = Integer.parseInt(request.getParameter("id"));
    Commodity commodity = baloot.getBalootCommodity(commodityId);
    List<Comment> commentList = new ArrayList<>(baloot.getCommodityComments(commodityId).values());
    List<Commodity> recommendedCommodities = new ArrayList<>(baloot.getRecommendedCommodities(commodityId));
%>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Commodity</title>
    <style>
        li {
            padding: 5px;
        }
        table {
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
<span>username: <%=baloot.getLoggedInUsername()%></span>
<br>
<ul>
    <li id="id">Id: <%=commodity.getId()%></li>
    <li id="name">Name: <%=commodity.getName()%></li>
    <li id="providerName">Provider Name: <%=baloot.getProviderNameById(commodity.getProviderId())%></li>
    <li id="price">Price: <%=commodity.getPrice()%></li>
    <li id="categories"><%=commodity.getCategoriesAsString()%></li>
    <li id="rating">Rating: <%=new DecimalFormat("0.00").format(commodity.getRating())%></li>
    <li id="inStock">In Stock: <%=commodity.getInStock()%></li>
</ul>

<label>Add Your Comment:</label>
<form action="" method="post">
    <input type="text" name="comment" value="" />
    <button type="submit">submit</button>
</form>
<br>
<form action="" method="POST">
    <label>Rate(between 1 and 10):</label>
    <input type="hidden" id="commodity_id" name="commodity_id" value=<%=commodity.getId()%> >
    <input type="number" id="quantity" name="quantity" min="1" max="10">
    <button type="submit" name="action" value="rate">Rate</button>
</form>
<br>
<form action="" method="POST">
    <input type="hidden" id="commodity_id" name="commodity_id" value=<%=commodity.getId()%> >
    <button type="submit" name="action" value="buylist">Add to BuyList</button>
</form>
<br />
<table>
    <caption><h2>Comments</h2></caption>
    <tr>
        <th>username</th>
        <th>comment</th>
        <th>date</th>
        <th>likes</th>
        <th>dislikes</th>
    </tr>
    <% for(Comment comment : commentList) { %>
    <tr>
        <td><%=comment.getUsername()%></td>
        <td><%=comment.getText()%></td>
        <td><%=comment.getDate().toString()%></td>
        <td>
            <form action="" method="POST">
                <label for=""><%=comment.getLikesNo()%></label>
                <input
                        id="form_comment_id"
                        type="hidden"
                        name="comment_id"
                        value="<%=comment.getCommentId()%>"
                />
                <button type="submit" name="action" value="like">like</button>
            </form>
        </td>
        <td>
            <form action="" method="POST">
                <label for=""><%=comment.getDislikesNo()%></label>
                <input
                        id="form_comment_id"
                        type="hidden"
                        name="comment_id"
                        value=<%=comment.getCommentId()%>
                />
                <button type="submit" name="action" value="dislike">dislike</button>
            </form>
        </td>
    </tr>
    <%}%>
</table>
<br><br>
<table>
    <caption><h2>Suggested Commodities</h2></caption>
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
    <% for(Commodity recommended : recommendedCommodities) { %>
    <tr>
        <td><%=recommended.getId()%></td>
        <td><%=recommended.getName()%></td>
        <td><%=baloot.getProviderNameById(recommended.getProviderId())%></td>
        <td><%=recommended.getPrice()%></td>
        <td><%=recommended.getCategoriesAsString()%></td>
        <td><%=new DecimalFormat("0.00").format(recommended.getRating())%></td>
        <td><%=recommended.getInStock()%></td>
        <td><a href="<%=recommended.getId()%>">Link</a></td>
    </tr>
    <%}%>
</table>
</body>
</html>

