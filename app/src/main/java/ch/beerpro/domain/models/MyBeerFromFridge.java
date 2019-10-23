package ch.beerpro.domain.models;

import java.util.Date;


public class MyBeerFromFridge implements MyBeer {
    private FridgeItem fridgeItem;
    private Beer beer;

    public MyBeerFromFridge(FridgeItem fridgeItem, Beer beer) {
        this.fridgeItem = fridgeItem;
        this.beer = beer;
    }

    @Override
    public FridgeItem getFridgeiten() {
        return fridgeItem;
    }

    @Override
    public void setFridgeItem(FridgeItem fridgeItem) {

    }

    @Override
    public String getBeerId() {
        return fridgeItem.getBeerId();
    }

    @Override
    public Beer getBeer() {
        return beer;
    }

    @Override
    public Date getDate() {
        return fridgeItem.getCreationDate();
    }
}