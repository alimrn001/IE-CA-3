package com.baloot.IE_CA_3.Controllers;

import com.baloot.IE_CA_3.Baloot.Baloot;
import com.baloot.IE_CA_3.Baloot.Exceptions.ItemAlreadyExistsInBuyListException;
import com.baloot.IE_CA_3.Baloot.Exceptions.ItemNotAvailableInStockException;
import com.baloot.IE_CA_3.Baloot.Exceptions.RatingOutOfRangeException;
import com.baloot.IE_CA_3.Baloot.Exceptions.WrongVoteValueException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "CommodityItem", urlPatterns = "/commodities/*")
public class CommodityItem extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if(baloot.userIsLoggedIn()) {
            String commodityIdStr = request.getPathInfo().split("/")[1];
            try {
                int commodityId = Integer.parseInt(commodityIdStr);
                String urlToDispatch = "../commodity.jsp?id="+commodityId;
                if(!baloot.commodityExists(commodityId)) {
                    urlToDispatch = "../404.jsp";
                }
                System.out.println("commodity id : " + commodityId + " url : " + urlToDispatch);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher(urlToDispatch);
                requestDispatcher.forward(request, response);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("../404.jsp");
                requestDispatcher.forward(request, response);
            }
        }
        else {
            response.sendRedirect("http://localhost:8181/IE_CA_3_war_exploded/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if(baloot.userIsLoggedIn()) {
            String action = request.getParameter("action");
            if(action.equals("rate")) {
                handleRateCommodityRequest(request, response);
            }
            else if(action.equals("buylist")) {
                addToBuyListRequest(request, response);
            }
            else if(action.equals("like")) {
                handleVoteCommentRequest(request, response, 1);
            }
            else if(action.equals("dislike")) {
                handleVoteCommentRequest(request, response, -1);
            }
            else if(action.equals("post_comment")) {

            }
        }
        else {
            response.sendRedirect("http://localhost:8181/IE_CA_3_war_exploded/login");
        }
    }

    private void handleVoteCommentRequest(HttpServletRequest request, HttpServletResponse response, int vote) {
        Baloot baloot = Baloot.getInstance();
        try {
            String commentId = request.getParameter("comment_id");
            baloot.voteComment(baloot.getLoggedInUsername(), Integer.parseInt(commentId), vote);
            String commodityId = request.getRequestURI().split("/")[3];
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("../commodity.jsp?id=" + commodityId);
            requestDispatcher.forward(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRateCommodityRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        try {
            String rateVal = request.getParameter("quantity");
            String commodityId = request.getParameter("commodity_id");
            baloot.addRating(baloot.getLoggedInUsername(), Integer.parseInt(commodityId), Integer.parseInt(rateVal));
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("../commodity.jsp?id=" + commodityId);
            requestDispatcher.forward(request, response);
        }
        catch (RatingOutOfRangeException e) {
            baloot.setCurrentSystemException(e.getMessage());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("../error.jsp");
            requestDispatcher.forward(request, response);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void addToBuyListRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        try {
            String commodityId = request.getParameter("commodity_id");
            baloot.addRemoveBuyList(baloot.getLoggedInUsername(), Integer.parseInt(commodityId), true);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("../successful.jsp");
            requestDispatcher.forward(request, response);
        }
        catch (ItemNotAvailableInStockException e) {
            baloot.setCurrentSystemException(e.getMessage());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("../error.jsp");
            requestDispatcher.forward(request, response);
        }
        catch (ItemAlreadyExistsInBuyListException e) {
            // might be a good idea to pass exception message as parameter to error.jsp
            baloot.setCurrentSystemException(e.getMessage());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("../error.jsp");
            requestDispatcher.forward(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
