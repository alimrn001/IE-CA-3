package com.baloot.IE_CA_3.Baloot.Commodity;

import com.baloot.IE_CA_3.Baloot.Exceptions.CommodityNotExistsException;
import com.baloot.IE_CA_3.Baloot.Exceptions.CommodityWithSameIDException;
import com.baloot.IE_CA_3.Baloot.Exceptions.ProviderNotExistsException;

import java.util.*;

public class CommoditiesManager {

    private final Map<Integer, Commodity> balootCommodities = new HashMap<>();

    private final Map<String, Category> balootCategorySections = new HashMap<>();

    private ArrayList<Integer> filteredCommoditiesID = new ArrayList<>(); // in case we want to apply multiple filters (for future use)

    private boolean filterAlreadyApplied = false;


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

    public Map<Integer, Commodity> sortCommoditiesByRating() {
        Map<Integer, Commodity> commodities = this.balootCommodities;
        Collections.sort(commodities.values().stream().toList(), (commodity1, commodity2) -> Double.compare(commodity1.getRating(), commodity2.getRating()));
        return commodities;
    }

    public Map<Integer, Commodity> getFilteredCommodities() {
        Map<Integer, Commodity> commodities = new HashMap<>();
        for(Map.Entry<Integer, Commodity> commodityEntry : balootCommodities.entrySet()) {
            if(this.filteredCommoditiesID.contains(commodityEntry.getKey())) {
                commodities.put(commodityEntry.getKey(), commodityEntry.getValue());
            }
        }
        return commodities;
    }

}
