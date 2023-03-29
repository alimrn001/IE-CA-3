package com.baloot.IE_CA_3.Controllers;

import com.baloot.IE_CA_3.Baloot.Baloot;
import com.baloot.IE_CA_3.Baloot.Exceptions.LogoutFailedException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "Logout", value = "/logout")
public class Logout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Baloot baloot = Baloot.getInstance();
        try {
            baloot.handleLogout();
            response.sendRedirect("http://localhost:8181/IE_CA_3_war_exploded/login");
        }
        catch(LogoutFailedException e) {
            response.sendRedirect("http://localhost:8181/IE_CA_3_war_exploded/login");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
