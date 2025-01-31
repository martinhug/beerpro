package ch.beerpro.data.repositories;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.models.MyBeer;
import ch.beerpro.domain.models.MyBeerCombine;
import ch.beerpro.domain.models.MyBeerFromPrice;
import ch.beerpro.domain.models.MyBeerFromPrivateNote;
import ch.beerpro.domain.models.MyBeerFromRating;
import ch.beerpro.domain.models.MyBeerFromWishlist;
import ch.beerpro.domain.models.Price;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;

import static androidx.lifecycle.Transformations.map;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class MyBeersRepository {


    private static List<MyBeer> getMyBeers(MyBeerCombine input, Map<String, ?> privateNotes) {
        List<Wish> wishes = input.getWishes();
        List<Rating> ratings = input.getRatings();
        List<FridgeItem> fridgeItems = input.getFridgeItems();
        List<Price> priceList = input.getPriceList();
        HashMap<String, Beer> beers = input.getBeers();
        HashMap<String, MyBeer> resultHashMap = new HashMap<>();

        Set<String> beersAlreadyOnTheList = new HashSet<>();
        for (Wish wish : wishes) {
            String beerId = wish.getBeerId();
            resultHashMap.put(beerId, new MyBeerFromWishlist(wish, beers.get(beerId)));
            beersAlreadyOnTheList.add(beerId);
        }
        for (Rating rating : ratings) {
            String beerId = rating.getBeerId();
            if (!beersAlreadyOnTheList.contains(beerId)) {
                resultHashMap.put(beerId, new MyBeerFromRating(rating, beers.get(beerId)));
                beersAlreadyOnTheList.add(beerId);
            }
        }
        for (Price price : priceList) {
            String beerId = price.getBeerId();
            if (!beersAlreadyOnTheList.contains(beerId)) {
                resultHashMap.put(beerId, new MyBeerFromPrice(price, beers.get(beerId)));
                beersAlreadyOnTheList.add(beerId);
            }
        }

        for (Map.Entry<String, ?> entry : privateNotes.entrySet()) {
            String beerId = entry.getKey();
            if (!beersAlreadyOnTheList.contains(beerId)) {
                resultHashMap.put(beerId, new MyBeerFromPrivateNote(entry.getValue().toString(), beers.get(beerId)));
                beersAlreadyOnTheList.add(beerId);
            }

        }
        ArrayList<MyBeer> result = new ArrayList<>(resultHashMap.values());
        Collections.sort(result, (r1, r2) -> r2.getDate().compareTo(r1.getDate()));
        return result;
    }


    public LiveData<List<MyBeer>> getMyBeers(LiveData<List<Beer>> allBeers, LiveData<List<Wish>> myWishlist,
                                             LiveData<List<Rating>> myRatings, LiveData<List<FridgeItem>> myFridgeItems,
                                             LiveData<List<Price>> myPrices, Map<String, ?> privateNotes) {

        return map(combineLatest(myWishlist, myRatings, myFridgeItems, myPrices, map(allBeers, Entity::entitiesById)),
                (myBeerCombine) -> MyBeersRepository.getMyBeers(myBeerCombine, privateNotes));
    }


}
