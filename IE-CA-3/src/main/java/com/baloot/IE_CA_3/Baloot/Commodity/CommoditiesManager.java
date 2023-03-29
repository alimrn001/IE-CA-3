package com.baloot.IE_CA_3.Baloot.Commodity;

import com.baloot.IE_CA_3.Baloot.Exceptions.CommodityNotExistsException;
import com.baloot.IE_CA_3.Baloot.Exceptions.CommodityWithSameIDException;
import com.baloot.IE_CA_3.Baloot.Exceptions.ProviderNotExistsException;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.stream.Collectors;

public class CommoditiesManager {

    private final Map<Integer, Commodity> balootCommodities = new HashMap<>();

    private final Map<String, Category> balootCategorySections = new HashMap<>();

    private final ArrayList<Integer> filteredCommoditiesID = new ArrayList<>();

    private boolean filterAlreadyApplied = false;

    private boolean sortIsApplied = false;

    private Map<Integer, Commodity> getNHighestRatedCommodities(int n) {
        return balootCommodities.entrySet().stream()
                .sorted((c1, c2) -> Double.compare(c2.getValue().getRating() , c1.getValue().getRating()))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public boolean commodityExists(int commodityId) {
        return balootCommodities.containsKey(commodityId);
    }


    public boolean categoryExists(String category) {
        return balootCategorySections.containsKey(category);
    }


    public void updateCategorySection(String categoryName, int commodityId) {
        if(categoryExists(categoryName)) {
            balootCategorySections.get(categoryName).addCommodityToCategory(commodityId);
        }
        else {
            Category category = new Category(categoryName);
            category.addCommodityToCategory(commodityId);
            balootCategorySections.put(categoryName, category);
        }
    }


    public void addCommodity(Commodity commodity) throws Exception {
        if(commodityExists(commodity.getId()))
            throw new CommodityWithSameIDException();

        for (String category : commodity.getCategories()) {
            updateCategorySection(category, commodity.getId());
        }
        balootCommodities.put(commodity.getId(), commodity);
    }


    public Commodity getBalootCommodity(int commodityId) throws Exception {
        if(!commodityExists(commodityId))
            throw new CommodityNotExistsException();
        return balootCommodities.get(commodityId);
    }


    public Map<Integer, Commodity> getBalootCommodities() {
        return this.balootCommodities;
    }


    public Map<String, Category> getBalootCategories() {
        return this.balootCategorySections;
    }


    public Map<Integer, Commodity> getCommoditiesByCategory(String category) {
        Map<Integer, Commodity> commodities = new HashMap<>();
        if(!categoryExists(category))
            return commodities;

        for(int categoryCommodityID : balootCategorySections.get(category).getCommodities()) {
            Commodity categoryCommodity = balootCommodities.get(categoryCommodityID);
            commodities.put(categoryCommodityID, categoryCommodity);
        }
        return commodities;
    }


    public Map<Integer, Commodity> getCommoditiesByPriceRange(int startPrice, int endPrice) {
        Map<Integer, Commodity> commodities = new HashMap<>();
        for(Map.Entry<Integer, Commodity> commodityEntry : balootCommodities.entrySet()) {
            if(commodityEntry.getValue().getPrice() <= endPrice && commodityEntry.getValue().getPrice() >= startPrice) {
                commodities.put(commodityEntry.getKey(), commodityEntry.getValue());
            }
        }
        return commodities;
    }


    public void clearFilters() {
        sortIsApplied = false;
        filterAlreadyApplied = false;
        filteredCommoditiesID.clear();
        for(Map.Entry<Integer, Commodity> commodityEntry : balootCommodities.entrySet())
            filteredCommoditiesID.add(commodityEntry.getKey());
    }


    public void filterCommoditiesByName(String searchedName) {
        if(!filterAlreadyApplied) {
            clearFilters();
            filterAlreadyApplied = true;
        }
        sortIsApplied = false;
        for(Map.Entry<Integer, Commodity> commodityEntry : balootCommodities.entrySet()) {
            if( (!(commodityEntry.getValue().getName().contains(searchedName))) && (filteredCommoditiesID.contains(commodityEntry.getKey())) )
                filteredCommoditiesID.remove(commodityEntry.getKey());
        }
    }


    public void filterCommoditiesByCategory(String category) {
        if(!filterAlreadyApplied) {
            clearFilters();
            filterAlreadyApplied = true;
        }
        sortIsApplied = false;
        for(Map.Entry<Integer, Commodity> commodityEntry : balootCommodities.entrySet()) {
            if( (!(commodityEntry.getValue().getCategories().contains(category))) && (filteredCommoditiesID.contains(commodityEntry.getKey())) )
                filteredCommoditiesID.remove(commodityEntry.getKey());
        }
    }


    public Map<Integer, Commodity> sortCommoditiesByPrice() {
        Map<Integer, Commodity> commodities = this.balootCommodities;
        Collections.sort(commodities.values().stream().toList(), (commodity1, commodity2) -> commodity1.getPrice()-(commodity2.getPrice()));
        return commodities;
    }


    public void sortCommoditiesByRating() {
        List<Commodity> commoditiesList = new ArrayList<>(balootCommodities.values());
        commoditiesList = commoditiesList.stream().sorted(Comparator.comparing(Commodity::getRating)).collect(Collectors.toList());
        clearFilters();
        filteredCommoditiesID.clear();
        for(Commodity commodity : commoditiesList)
            filteredCommoditiesID.add(commodity.getId());
        sortIsApplied = true;
    }


    public List<Commodity> getFilteredCommodities() {
        List<Commodity> commoditiesList = new ArrayList<>();
        for(int id : filteredCommoditiesID)
            commoditiesList.add(balootCommodities.get(id));
        return commoditiesList;
    }


    public Map<Integer, Commodity> getRecommendedCommodities(int commodityID) throws Exception {
        Commodity commodity = getBalootCommodity(commodityID);
        Map<Integer, Commodity> commoditiesSharingSameCategories = new HashMap<>();
        for(String category : commodity.getCategories()) {
            Map<Integer, Commodity> commoditiesInSameCategory = getCommoditiesByCategory(category);
            commoditiesSharingSameCategories.putAll(Maps.difference(commoditiesInSameCategory, commoditiesSharingSameCategories).entriesOnlyOnLeft()); //or entry on right ??
        }
        int commoditiesSharingSameCategoriesNum = commoditiesSharingSameCategories.size();

        if(commoditiesSharingSameCategoriesNum >= 5)
            return commoditiesSharingSameCategories.entrySet().stream()
                    .sorted((c1, c2) -> Double.compare(c2.getValue().getRating() , c1.getValue().getRating())) // check ascending or descending !
                    .limit(5).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        else {
            Map<Integer, Commodity> highestRatedCommodities = getNHighestRatedCommodities(5-commoditiesSharingSameCategoriesNum);
            commoditiesSharingSameCategories.putAll(Maps.difference(highestRatedCommodities, commoditiesSharingSameCategories).entriesOnlyOnLeft()); // left or right
            return commoditiesSharingSameCategories;
        }
    }


    public boolean getFilteringStatus() {
        return filterAlreadyApplied;
    }


    public boolean getSortingStatus() {
        return sortIsApplied;
    }

}
