package com.baloot.IE_CA_3.controllers;

import com.baloot.IE_CA_3.Baloot.Baloot;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;


@WebServlet(name = "CommoditiesList", urlPatterns = "/commodities")
public class CommoditiesList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if(baloot.userIsLoggedIn()) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("commodities.jsp");
            requestDispatcher.forward(request, response);
        }
        else {
            response.sendRedirect("http://localhost:8181/IE_CA_3_war_exploded/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ovdodb");
    }
}
