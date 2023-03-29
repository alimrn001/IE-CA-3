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
        Baloot baloot = Baloot.getInstance();
        if (baloot.userIsLoggedIn()) {
            String action = request.getParameter("action");

            if(action.equals("search_by_category")) {
                String searchFilterName = request.getParameter("search");
                executeSearchFilterTask(request, response, searchFilterName, 1);
            }
            else if(action.equals("search_by_name")) {
                String searchFilterName = request.getParameter("search");
                executeSearchFilterTask(request, response, searchFilterName, 0);
            }
            else if(action.equals("clear")) {
                executeClearFilterTask(request, response);
            }
        }
        else {
            response.sendRedirect("http://localhost:8181/IE_CA_3_war_exploded/login");
        }
    }

    private void executeSearchFilterTask(HttpServletRequest request, HttpServletResponse response, String searchString, int nameOrCategory) {
        Baloot baloot = Baloot.getInstance();
        try {
            if (nameOrCategory == 0) {
                if(!searchString.equals(""))
                    System.out.println("is size : " + baloot.getCommoditiesFilteredByName(searchString).size());
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("commodities.jsp");
                requestDispatcher.forward(request, response);
            } else if (nameOrCategory == 1) {
                if(!searchString.equals(""))
                    System.out.println("is size : " + baloot.getCommoditiesFilteredByCategory(searchString).size());
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("commodities.jsp");
                requestDispatcher.forward(request, response);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void executeClearFilterTask(HttpServletRequest request, HttpServletResponse response) {
        Baloot baloot = Baloot.getInstance();
        try {
            baloot.clearSearchFilters();
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("commodities.jsp");
            requestDispatcher.forward(request, response);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
