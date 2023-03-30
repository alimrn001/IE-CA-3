package com.baloot.IE_CA_3.Controllers;

import com.baloot.IE_CA_3.Baloot.Baloot;

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

    }
}
