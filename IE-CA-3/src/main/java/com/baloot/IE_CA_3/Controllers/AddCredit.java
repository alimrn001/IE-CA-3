package com.baloot.IE_CA_3.Controllers;

import com.baloot.IE_CA_3.Baloot.Baloot;
import com.baloot.IE_CA_3.Baloot.Exceptions.NegativeCreditAddingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AddCredit", urlPatterns = "/credit")
public class AddCredit extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        if (baloot.userIsLoggedIn()) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("addCredit.jsp");
            requestDispatcher.forward(request, response);
        }
        else {
            response.sendRedirect("http://localhost:8181/IE_CA_3_war_exploded/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        String action = request.getParameter("action");
        if(baloot.userIsLoggedIn()) {
            if(action.equals("add_credit")) {
                int creditAmount = Integer.parseInt(request.getParameter("credit"));
                try {
                    baloot.addCreditToUser(baloot.getLoggedInUsername(), creditAmount);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("successful.jsp");
                    requestDispatcher.forward(request, response);
                }
                catch(NegativeCreditAddingException e) {
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
