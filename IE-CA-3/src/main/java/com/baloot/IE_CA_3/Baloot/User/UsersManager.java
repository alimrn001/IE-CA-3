package com.baloot.IE_CA_3.Baloot.User;

import com.baloot.IE_CA_3.Baloot.Exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersManager {

    private final Map<String, User> balootUsers = new HashMap<>();

    private String loggedInUserName = "";


    public boolean userExists(String username) {
        return balootUsers.containsKey(username);
    }


    public boolean userEmailExists(String userEmail) {
        boolean emailExists = false;
        for(Map.Entry<String, User> userEntry : balootUsers.entrySet()) {
            if (userEntry.getValue().getEmail().equals(userEmail)) {
                emailExists = true;
                break;
            }
        }
        return emailExists; // is email unique for users ?? if not how to identify user account in comment by just email ? how to find its id??
    }


    public void addUser(User user) throws Exception {
        if(balootUsers.containsKey(user.getUsername())) {
            user.setBuyList(balootUsers.get(user.getUsername()).getBuyList());
            balootUsers.put(user.getUsername(), user);
        }
        else {
            if((user.getUsername().contains("!")) || (user.getUsername().contains("#")) || (user.getUsername().contains("@"))) {
                throw new UsernameWrongCharacterException();
            }
            else {
                balootUsers.put(user.getUsername(), user);
            }
        }
    }


    public void addCreditToUser(String username, int credit) throws Exception {
        if(userExists(username)) {
            if(credit <= 0)
                throw new NegativeCreditAddingException();
            balootUsers.get(username).addCredit(credit);
            return;
        }
        throw new UserNotExistsException();
    }


    public User getBalootUser(String username) throws Exception {
        if(!userExists(username))
            throw new UserNotExistsException();
        return balootUsers.get(username);
    }


    public Map<String, User> getBalootUsers() {
        return balootUsers;
    }


    public void handleLogin(String username, String password) throws Exception {
        if(userExists(username)) {
            User user = getBalootUser(username);
            if(user.getPassword().equals(password)) {
                this.loggedInUserName = username;
                return;
            }
        }
        throw new LoginFailedException();
    }


    public void handleLogout() throws Exception {
        if(!loggedInUserExists())
            throw new LogoutFailedException();
        this.loggedInUserName = "";
    }


    public void addCouponForUser(String username, String couponCode) throws Exception {
        if(userExists(username)) {
            if(balootUsers.get(username).userHasUsedDiscountCoupon(couponCode))
                throw new DiscountCouponHasExpiredException();
            balootUsers.get(username).addDiscountCodeToUsedCoupons(couponCode);
        }
        else
            throw new UserNotExistsException();
    }


    public String getLoggedInUser() {
        return this.loggedInUserName;
    }


    public boolean loggedInUserExists() {
        return !(this.loggedInUserName.equals(""));
    }


    public boolean selectedUserHasLoggedIn(String username) {
        return (this.loggedInUserName.equals(username));
    }


    public Map<String, User> getUsers() {
        return balootUsers;
    }


    public ArrayList<Integer> getUserBuyListItems(String username) throws Exception {
        if(userExists(username)) {
            return balootUsers.get(username).getBuyList();
        }
        throw new UserNotExistsException();
    }


    public ArrayList<Integer> getUserPurchasedListItems(String username) throws Exception {
        if(userExists(username)) {
            return balootUsers.get(username).getPurchasedList();
        }
        throw new UserNotExistsException();
    }

}
