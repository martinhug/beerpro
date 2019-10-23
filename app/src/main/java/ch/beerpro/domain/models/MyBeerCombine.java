package ch.beerpro.domain.models;

import java.util.HashMap;
import java.util.List;

public class MyBeerCombine {
    private HashMap<String, Beer> beers;
    private List<Wish> wishes;
    private List<Rating> ratings;
    private List<FridgeItem> fridgeItems;

    public MyBeerCombine(HashMap<String, Beer> beers, List<Wish> wishes, List<Rating> ratings, List<FridgeItem> fridgeItems) {
        this.beers = beers;
        this.wishes = wishes;
        this.ratings = ratings;
        this.fridgeItems = fridgeItems;
    }

    public HashMap<String, Beer> getBeers() {
        return beers;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public List<FridgeItem> getFridgeItems() {
        return fridgeItems;
    }
}
