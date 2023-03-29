package com.baloot.IE_CA_3.Controllers;

import com.baloot.IE_CA_3.Baloot.Commodity.Commodity;
import com.baloot.IE_CA_3.Baloot.Exceptions.LoginFailedException;
import com.baloot.IE_CA_3.Baloot.Provider.Provider;
import com.baloot.IE_CA_3.Baloot.User.User;
import com.baloot.IE_CA_3.Baloot.Utilities.LocalDateAdapter;
import com.baloot.IE_CA_3.HTTPReqHandler.HTTPReqHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.baloot.IE_CA_3.Baloot.Baloot;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "Login", urlPatterns = "/login")
public class Login extends HttpServlet {
    final static String UsersAPI = "http://5.253.25.110:5000/api/users";
    final static String ProvidersAPI = "http://5.253.25.110:5000/api/providers";
    final static String CommoditiesAPI = "http://5.253.25.110:5000/api/commodities";

    public void init() throws ServletException {
        try {
            retrieveUsersDataFromAPI(UsersAPI);
            retrieveProvidersDataFromAPI(ProvidersAPI);
            retrieveCommoditiesDataFromAPI(CommoditiesAPI);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("login.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Baloot baloot = Baloot.getInstance();
        try {
            baloot.handleLogin(username, password);
            response.sendRedirect("http://localhost:8181/IE_CA_3_war_exploded/");
        }
        catch (LoginFailedException e) {
            response.sendRedirect("http://localhost:8181/IE_CA_3_war_exploded/login"); // ???
        }
        catch (Exception e) {
            System.out.println(e.getMessage()); // unreachable
        }
    }


    private void retrieveUsersDataFromAPI(String url) throws Exception {
        String userDataJsonStr = new HTTPReqHandler().httpGetRequest(url);
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        List<User> userList = gson.fromJson(userDataJsonStr, userListType);
        for (User user : userList) {
            user.initializeGsonNullValues();
            Baloot.getInstance().addUser(user);
        }
    }


    private void retrieveProvidersDataFromAPI(String url) throws Exception {
        String providerDataJsonStr = new HTTPReqHandler().httpGetRequest(url);
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        Type providerListType = new TypeToken<ArrayList<Provider>>(){}.getType();
        List<Provider> providerList = gson.fromJson(providerDataJsonStr, providerListType);
        for(Provider provider : providerList) {
            provider.initializeGsonNullValues();
            Baloot.getInstance().addProvider(provider);
        }
    }


    private void retrieveCommoditiesDataFromAPI(String url) throws Exception {
        String commodityDataJsonStr = new HTTPReqHandler().httpGetRequest(url);
        Gson gson = new GsonBuilder().create();
        Type commodityListType = new TypeToken<ArrayList<Commodity>>(){}.getType();
        List<Commodity> commodityList = gson.fromJson(commodityDataJsonStr, commodityListType);
        for(Commodity commodity : commodityList) {
            commodity.initializeGsonNullValues();
            Baloot.getInstance().addCommodity(commodity);
        }
    }

}
