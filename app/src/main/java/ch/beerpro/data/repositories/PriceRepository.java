package ch.beerpro.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import ch.beerpro.domain.models.Price;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;

import static androidx.lifecycle.Transformations.switchMap;

public class PriceRepository {
    private static LiveData<List<Price>> getPriceListByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(Price.COLLECTION)
                .orderBy(Price.FIELD_CREATION_DATE, Query.Direction.DESCENDING).whereEqualTo(Price.FIELD_USER_ID, userId), Price.class);
    }

    private static LiveData<List<Price>> getPricesByBeer(String beerId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(Price.COLLECTION)
                .orderBy(Price.FIELD_CREATION_DATE, Query.Direction.DESCENDING)
                .whereEqualTo(Price.FIELD_BEER_ID, beerId), Price.class);
    }

    public LiveData<List<Price>> getMyPriceList(MutableLiveData<String> currentUserId) {
        return switchMap(currentUserId, PriceRepository::getPriceListByUser);
    }

    public LiveData<List<Price>> getPriceListForBeer(LiveData<String> beerId) {
        return switchMap(beerId, PriceRepository::getPricesByBeer);
    }
}
