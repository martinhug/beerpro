package ch.beerpro.presentation;

import android.app.Application;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Map;

import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.FridgeRepository;
import ch.beerpro.data.repositories.LikesRepository;
import ch.beerpro.data.repositories.MyBeersRepository;
import ch.beerpro.data.repositories.PriceRepository;
import ch.beerpro.data.repositories.RatingsRepository;
import ch.beerpro.data.repositories.WishlistRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.models.MyBeer;
import ch.beerpro.domain.models.Price;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;

import static android.content.Context.MODE_PRIVATE;
import static ch.beerpro.presentation.details.DetailsActivity.NOTE_ID;

/**
 * This is the viewmodel for the {@link MainActivity}, which is also used by the three pages/fragments contained in it.
 */
public class MainViewModel extends AndroidViewModel implements CurrentUser {

    private static final String TAG = "MainViewModel";

    private final BeersRepository beersRepository;
    private final LikesRepository likesRepository;
    private final RatingsRepository ratingsRepository;
    private final WishlistRepository wishlistRepository;
    private final FridgeRepository fridgeRepository;
    private final PriceRepository priceRepository;

    private final LiveData<List<Wish>> myWishlist;
    private final LiveData<List<Rating>> myRatings;
    private final LiveData<List<MyBeer>> myBeers;
    private final LiveData<List<FridgeItem>> myFridge;
    private final LiveData<List<Price>> myPrice;

    public MainViewModel(Application application) {
        super(application);
        /*
         * TODO We should really be injecting these!
         */
        beersRepository = new BeersRepository();
        likesRepository = new LikesRepository();
        wishlistRepository = new WishlistRepository();
        ratingsRepository = new RatingsRepository();
        fridgeRepository = new FridgeRepository();
        priceRepository = new PriceRepository();
        MyBeersRepository myBeersRepository = new MyBeersRepository();

        LiveData<List<Beer>> allBeers = beersRepository.getAllBeers();

        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        myWishlist = wishlistRepository.getMyWishlist(currentUserId);
        myRatings = ratingsRepository.getMyRatings(currentUserId);
        myFridge = fridgeRepository.getMyFridgeItems(currentUserId);
        myPrice = priceRepository.getMyPriceList(currentUserId);
        Map<String, ?> privateNotes = application.getSharedPreferences(NOTE_ID, MODE_PRIVATE).getAll();

        myBeers = myBeersRepository.getMyBeers(allBeers, myWishlist, myRatings, myFridge, myPrice, privateNotes);


        /*
         * Set the current user id, which is used as input for the getMyWishlist and getMyRatings calls above.
         * Settings the id does not yet cause any computation or data fetching, only when an observer is subscribed
         * to the LiveData will the data be fetched and computations depending on it will be executed. LiveData works
         * similar to Java 8 streams or Rx observables in that regard, but have a less rich API for combining such
         * streams of data.
         * */
        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<MyBeer>> getMyBeers() {
        return myBeers;
    }

    public LiveData<List<Rating>> getMyRatings() {
        return myRatings;
    }

    public LiveData<List<Wish>> getMyWishlist() {
        return myWishlist;
    }

    public LiveData<List<FridgeItem>> getMyFridgeItems() {
        return myFridge;
    }

    public PriceRepository getPriceRepository() {
        return priceRepository;
    }


    public LiveData<List<String>> getBeerCategories() {
        return beersRepository.getBeerCategories();
    }

    public LiveData<List<String>> getBeerManufacturers() {
        return beersRepository.getBeerManufacturers();
    }

    public void toggleLike(Rating rating) {
        likesRepository.toggleLike(rating);
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), itemId);
    }

    public LiveData<List<Pair<Rating, Wish>>> getAllRatingsWithWishes() {
        return ratingsRepository.getAllRatingsWithWishes(myWishlist);
    }
}