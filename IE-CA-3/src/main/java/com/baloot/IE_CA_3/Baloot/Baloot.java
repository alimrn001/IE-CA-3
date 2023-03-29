package com.baloot.IE_CA_3.Baloot;

import com.baloot.IE_CA_3.Baloot.Commodity.Category;
import com.baloot.IE_CA_3.Baloot.Commodity.CommoditiesManager;
import com.baloot.IE_CA_3.Baloot.Commodity.Commodity;
import com.baloot.IE_CA_3.Baloot.Comment.*;
import com.baloot.IE_CA_3.Baloot.Rating.*;
import com.baloot.IE_CA_3.Baloot.Exceptions.*;
import com.baloot.IE_CA_3.Baloot.Provider.Provider;
import com.baloot.IE_CA_3.Baloot.Provider.ProvidersManager;
import com.baloot.IE_CA_3.Baloot.User.User;
import com.baloot.IE_CA_3.Baloot.User.UsersManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Baloot {

    private final UsersManager usersManager = new UsersManager();

    private final ProvidersManager providersManager = new ProvidersManager();

    private final CommoditiesManager commoditiesManager = new CommoditiesManager();

    private final Map<String, Rating> balootRatings = new HashMap<>();

    private final Map<String, Category> balootCategorySections = new HashMap<>();

    private final Map<Integer, Comment> balootComments = new HashMap<>();

    private int latestCommentID = 0; //comments id start with 1

    private static Baloot instance;

    private Baloot() {}


    public static Baloot getInstance() {
        if(instance == null)
            instance = new Baloot();
        return instance;
    }


    public boolean userIsLoggedIn() {
        return usersManager.loggedInUserExists();
    }


    public boolean searchFilterIsApplied() {
        return commoditiesManager.getFilteringStatus();
    }


    public String getLoggedInUsername() {
        return usersManager.getLoggedInUser();
    }


    public String getProviderNameById(int providerId) {
        return providersManager.getProviderNameById(providerId);
    }


    public void handleLogin(String username, String password) throws Exception {
        usersManager.handleLogin(username, password);
    }


    public boolean commentExists(int commentId) {
        return balootComments.containsKey(commentId);
    }


    public void addUser(User user) throws Exception {
        usersManager.addUser(user);
    }


    public void addCommodity(Commodity commodity) throws Exception {
        if(!providersManager.providerExists(commodity.getProviderId()))
            throw new ProviderNotExistsException();

        commoditiesManager.addCommodity(commodity);
        providersManager.getBalootProviders().get(commodity.getProviderId()).addProvidedCommodity(commodity.getId());
        providersManager.getBalootProviders().get(commodity.getProviderId()).updateCommoditiesData(commodity.getRating());
    }


    public void addProvider(Provider provider) throws Exception {
        providersManager.addProvider(provider);
    }


    public void addComment(Comment comment) throws Exception { //fix this !!!1
        if(!usersManager.userEmailExists(comment.getUsername())) {
            throw new UserNotExistsException();
        }
        if(!commoditiesManager.commodityExists(comment.getCommodityId())) {
            throw new CommodityNotExistsException();
        }
        comment.setCommentId(latestCommentID+1);
        comment.setLikesNo(0);
        comment.setDislikesNo(0);
        comment.setNeutralVotesNo(0);
        balootComments.put(comment.getCommentId(), comment);
        latestCommentID++;
        commoditiesManager.getBalootCommodities().get(comment.getCommodityId()).addComment(comment.getCommentId());
    }


    public void addRating(String username, int commodityId, int rate) throws Exception {
        if(!usersManager.userExists(username))
            throw new UserNotExistsException();
        if(!commoditiesManager.commodityExists(commodityId))
            throw new CommodityNotExistsException();
        if(rate > 10 || rate < 1)
            throw new RatingOutOfRangeException();

        String ratingPrimaryKey = username + "_" + commodityId;
        Rating rating = new Rating(username, commodityId, rate);

        if(!balootRatings.containsKey(ratingPrimaryKey)) {
            commoditiesManager.getBalootCommodities().get(commodityId).addNewRating(rate);
            balootRatings.put(ratingPrimaryKey, rating);
            return;
        }
        int previousRate = balootRatings.get(ratingPrimaryKey).getScore();
        commoditiesManager.getBalootCommodities().get(commodityId).updateUserRating(previousRate, rate);
        balootRatings.put(ratingPrimaryKey, rating);
    }


    public void addRemoveBuyList(String username, int commodityId, boolean isAdding) throws Exception {
        User user = usersManager.getBalootUser(username);
        Commodity commodity = commoditiesManager.getBalootCommodity(commodityId);
        if(commodity.getInStock()==0 && isAdding)
            throw new ItemNotAvailableInStockException();
        if(user.itemExistsInBuyList(commodityId)) {
            if(isAdding)
                throw new ItemAlreadyExistsInBuyListException();
            user.removeFromBuyList(commodityId);
            return;
        }
        else {
            if(isAdding) {
                user.addToBuyList(commodityId);
                return;
            }
            throw new ItemNotInBuyListForRemovingException();
        }
    }


    public void purchaseUserBuyList(String username) throws Exception {
        if(!usersManager.userExists(username))
            throw new UserNotExistsException();

        ArrayList<Integer> userBuyList = usersManager.getBalootUsers().get(username).getBuyList();
        double totalPurchasePrice = 0;
        for(Integer buyListItemId : userBuyList)
            totalPurchasePrice += commoditiesManager.getBalootCommodities().get(buyListItemId).getPrice();
        if(usersManager.getBalootUsers().get(username).getCredit() < totalPurchasePrice)
            throw new NotEnoughCreditException();

        usersManager.getBalootUsers().get(username).purchaseBuyList(totalPurchasePrice);
        for(Integer buyListItemId : userBuyList)
            commoditiesManager.getBalootCommodities().get(buyListItemId).reduceInStock(1);
    }


    public void addCreditToUser(String username, double credit) throws Exception {
        User user = usersManager.getBalootUser(username);
        if(credit <= 0)
            throw new NegativeCreditAddingException();
        user.addCredit(credit);
    }


    public void voteComment(String username, int commentId, int vote) throws Exception {
        if(!usersManager.userExists(username))
            throw new UserNotExistsException();
        if(!commentExists(commentId))
            throw new CommentNotExistsException();

        boolean beenLikedBefore = usersManager.getBalootUsers().get(username).userHasLikedComment(commentId);
        boolean beenDislikedBefore = usersManager.getBalootUsers().get(username).userHasDislikedComment(commentId);

        if(vote==1) {
            usersManager.getBalootUsers().get(username).addCommentToLikedList(commentId);
            if(!beenLikedBefore)
                balootComments.get(commentId).addLike();
            if(beenDislikedBefore)
                balootComments.get(commentId).removeDislike();
        }

        else if(vote==0) { // ????
            balootComments.get(commentId).addNeutralVote();
        }

        else if(vote==-1) {
            usersManager.getBalootUsers().get(username).addCommentToDislikedList(commentId);
            if(!beenDislikedBefore)
                balootComments.get(commentId).addDislike();
            if(beenLikedBefore)
                balootComments.get(commentId).removeLike();
        }

        else
            throw new WrongVoteValueException();
    }


    public Map<Integer, Comment> getCommodityComments(int commodityId) throws Exception {
        if(!commoditiesManager.commodityExists(commodityId))
            throw new CommodityNotExistsException();
        Map<Integer, Comment> result = new HashMap<>();
        for (Map.Entry<Integer, Comment> commentEntry : balootComments.entrySet()) {
            if(commentEntry.getValue().getCommodityId()==commodityId)
                result.put(commentEntry.getKey(), commentEntry.getValue());
        }
        return result;
    }


    public Map<Integer, Commodity> getCommoditiesByCategory(String category) {
        return commoditiesManager.getCommoditiesByCategory(category);
    }


    public Map<Integer, Commodity> getCommoditiesByPriceRange(int startPrice, int endPrice) {
        return commoditiesManager.getCommoditiesByPriceRange(startPrice, endPrice);
    }


    public Map<Integer, Commodity> getCommoditiesFilteredByName(String commodityName) {
        commoditiesManager.filterCommoditiesByName(commodityName);
        return commoditiesManager.getFilteredCommodities();
    }


    public Map<Integer, Commodity> getCommoditiesFilteredByCategory(String category) {
        commoditiesManager.filterCommoditiesByCategory(category);
        return commoditiesManager.getFilteredCommodities();//might remove and make it void !
    }


    public Map<Integer, Commodity> getFilteredCommodities() {
        return commoditiesManager.getFilteredCommodities();
    }


    public void clearSearchFilters() {
        commoditiesManager.clearFilters();
    }


    public User getBalootUser(String username) throws Exception {
        return usersManager.getBalootUser(username);
    }


    public Provider getBalootProvider(int providerID) throws Exception {
        return providersManager.getBalootProvider(providerID);
    }


    public Commodity getBalootCommodity(int commodityID) throws Exception {
        return commoditiesManager.getBalootCommodity(commodityID);
    }


    public Comment getBalootComment(int commentId) throws Exception {
        if(!commentExists(commentId))
            throw new CommentNotExistsException();
        return balootComments.get(commentId);
    }


    public Map<String, User> getBalootUsers() {
        return usersManager.getBalootUsers();
    }


    public Map<Integer, Commodity> getBalootCommodities() {
        return commoditiesManager.getBalootCommodities();
    }


    public Map<Integer, Provider> getBalootProviders() {
        return providersManager.getBalootProviders();
    }


    public Map<Integer, Comment> getBalootComments() {
        return balootComments;
    }


    public Map<String, Rating> getBalootRatings() {
        return balootRatings;
    }


    public Map<String, Category> getBalootCategorySections() {
        return commoditiesManager.getBalootCategories();
    }

}
