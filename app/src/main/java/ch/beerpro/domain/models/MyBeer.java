package ch.beerpro.domain.models;

import java.util.Date;

public interface MyBeer {
    FridgeItem getFridgeiten();

    void setFridgeItem(FridgeItem fridgeItem);

    String getBeerId();

    Beer getBeer();

    Date getDate();
}
