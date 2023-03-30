package com.baloot.IE_CA_3.Controllers;

import com.baloot.IE_CA_3.Baloot.Baloot;
import com.baloot.IE_CA_3.Baloot.Exceptions.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "BuyList", value = "/buyList")
public class BuyList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if(baloot.userIsLoggedIn()) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("buylist.jsp");
            requestDispatcher.forward(request, response);
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
            if(action.equals("payment")) {
                try {
                    String discountStr = request.getParameter("discount_code");
                    int discount = 0;
                    if(!discountStr.equals(""))
                        discount = baloot.getDiscountCouponValueByCode(discountStr);
                    System.out.println("here and discount is : " + discount);
                    baloot.purchaseUserBuyList(baloot.getLoggedInUsername(), discount);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("successful.jsp");
                    requestDispatcher.forward(request, response);
                }
                catch(NotEnoughCreditException e) {
                    baloot.setCurrentSystemException(e.getMessage());
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
                    requestDispatcher.forward(request, response);
                }
                catch(ItemNotAvailableInStockException e) {
                    baloot.setCurrentSystemException(e.getMessage());
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
                    requestDispatcher.forward(request, response);
                }
                catch(DiscountCouponHasExpiredException e) {
                    baloot.setCurrentSystemException(e.getMessage());
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
                    requestDispatcher.forward(request, response);
                }
                catch (DiscountCouponNotExistsException e) {
                    baloot.setCurrentSystemException(e.getMessage());
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
                    requestDispatcher.forward(request, response);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            else if(action.equals("remove")) {
                try {
                    String commodityIdToBeRemoved = request.getParameter("commodityId");
                    baloot.addRemoveBuyList(baloot.getLoggedInUsername(), Integer.parseInt(commodityIdToBeRemoved), false);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("buylist.jsp");
                    requestDispatcher.forward(request, response);
                }
                catch(ItemNotInBuyListForRemovingException e) {
                    baloot.setCurrentSystemException(e.getMessage());
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("error.jsp");
                    requestDispatcher.forward(request, response);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            response.sendRedirect("http://localhost:8181/IE_CA_3_war_exploded/login");
        }
    }
}
