package ch.beerpro.domain.utils;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.models.MyBeerCombine;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;

import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.List;

public class LiveDataExtensions {

    public static <A, B> LiveData<Pair<A, B>> zip(LiveData<A> as, LiveData<B> bs) {
        return new MediatorLiveData<Pair<A, B>>() {

            A lastA = null;
            B lastB = null;

            {
                {
                    addSource(as, (A a) -> {
                        lastA = a;
                        update();
                    });
                    addSource(bs, (B b) -> {
                        lastB = b;
                        update();
                    });
                }
            }

            private void update() {
                this.setValue(new Pair<>(lastA, lastB));
            }
        };
    }

    public static <A, B> LiveData<Pair<A, B>> combineLatest(LiveData<A> as, LiveData<B> bs) {
        return new MediatorLiveData<Pair<A, B>>() {

            A lastA = null;
            B lastB = null;

            {
                {
                    addSource(as, (A a) -> {
                        lastA = a;
                        update();
                    });
                    addSource(bs, (B b) -> {
                        lastB = b;
                        update();
                    });
                }
            }

            private void update() {
                if (lastA != null && lastB != null) {
                    this.setValue(new Pair<>(lastA, lastB));
                }
            }
        };
    }

    public static <A, B, C> LiveData<Triple<A, B, C>> combineLatest(LiveData<A> as, LiveData<B> bs, LiveData<C> cs) {
        return new MediatorLiveData<Triple<A, B, C>>() {

            A lastA = null;
            B lastB = null;
            C lastC = null;

            {
                {
                    addSource(as, (A a) -> {
                        lastA = a;
                        update();
                    });
                    addSource(bs, (B b) -> {
                        lastB = b;
                        update();
                    });
                    addSource(cs, (C c) -> {
                        lastC = c;
                        update();
                    });
                }
            }

            private void update() {
                if (lastA != null && lastB != null && lastC != null) {
                    this.setValue(Triple.of(lastA, lastB, lastC));
                }
            }
        };
    }

    public static LiveData<MyBeerCombine> combineLatest(
            LiveData<List<Wish>> wishes,
            LiveData<List<Rating>> ratings,
            LiveData<List<FridgeItem>> fridgeItems,
            LiveData<HashMap<String, Beer>> beers) {
        return new MediatorLiveData<MyBeerCombine>() {
            List<Wish> lastWishes = null;
            List<Rating> lastRatings = null;
            List<FridgeItem> lastFridgeItems = null;
            HashMap<String, Beer> lastBeers = null;

            {
                {
                    addSource(wishes, (List<Wish> wishList) -> {
                        lastWishes = wishList;
                        update();
                    });
                    addSource(ratings, (List<Rating> ratingsList) -> {
                        lastRatings = ratingsList;
                        update();
                    });
                    addSource(fridgeItems, (List<FridgeItem> fridgeItemList) -> {
                        lastFridgeItems = fridgeItemList;
                        update();
                    });
                    addSource(beers, (HashMap<String, Beer> beerHashMap) -> {
                        lastBeers = beerHashMap;
                        update();
                    });
                }
            }

            private void update() {
                if (lastWishes != null && lastRatings != null && lastFridgeItems != null && lastBeers != null) {
                    this.setValue(new MyBeerCombine(lastBeers, lastWishes, lastRatings, lastFridgeItems));
                }
            }
        };
    }
}
