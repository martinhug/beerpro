package ch.beerpro.data.repositories;

import android.util.Pair;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.utils.FirestoreQueryLiveData;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class FridgeRepository {


    private static LiveData<List<FridgeItem>> getFridgeItemsByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(FridgeItem.COLLECTION)
                .orderBy(FridgeItem.FIELD_AMOUNT, Query.Direction.DESCENDING).whereEqualTo(FridgeItem.FIELD_USER_ID, userId),
                FridgeItem.class);
    }

    private static LiveData<FridgeItem> getUserFridgeFor(Pair<String, Beer> input) {
        String userId = input.first;
        Beer beer = input.second;
        DocumentReference document = FirebaseFirestore.getInstance().collection(FridgeItem.COLLECTION)
                .document(FridgeItem.generateId(userId, beer.getId()));
        return new FirestoreQueryLiveData<>(document, FridgeItem.class);
    }

    public Task<Void> addUserFridgeItem(String userId, String itemId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String fridgeItemId = FridgeItem.generateId(userId, itemId);

        DocumentReference fridgeItemEntryQuery = db.collection(FridgeItem.COLLECTION).document(fridgeItemId);

        return fridgeItemEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && Objects.requireNonNull(task.getResult()).exists()) {

                DocumentSnapshot doc = task.getResult();
                String amount = doc.getString("amount");
                assert amount != null;
                int amountInt = Integer.parseInt(amount);
                amountInt++;

                return fridgeItemEntryQuery.set(new FridgeItem(userId, itemId, Integer.toString(amountInt)));
            } else if (task.isSuccessful()) {
                return fridgeItemEntryQuery.set(new FridgeItem(userId, itemId, "1"));
            } else {
                throw Objects.requireNonNull(task.getException());
            }
        });
    }

    public Task<Void> removeUserFridgeItem(String userId, String itemId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String fridgeItemId = FridgeItem.generateId(userId, itemId);

        DocumentReference fridgeItemEntryQuery = db.collection(FridgeItem.COLLECTION).document(fridgeItemId);

        return fridgeItemEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && Objects.requireNonNull(task.getResult()).exists()) {

                DocumentSnapshot doc = task.getResult();
                String amount = doc.getString("amount");
                assert amount != null;
                int amountInt = Integer.parseInt(amount);
                amountInt--;
                if (amountInt == 0) {
                    return fridgeItemEntryQuery.delete();
                } else {
                    return fridgeItemEntryQuery.set(new FridgeItem(userId, itemId, Integer.toString(amountInt)));
                }
            } else {
                throw Objects.requireNonNull(task.getException());
            }
        });
    }

    public LiveData<List<Pair<FridgeItem, Beer>>> getMyFridgeWithBeers(LiveData<String> currentUserId,
                                                                       LiveData<List<Beer>> allBeers) {
        return map(combineLatest(getMyFridgeItems(currentUserId), map(allBeers, Entity::entitiesById)), input -> {
            List<FridgeItem> fridgeItems = input.first;
            HashMap<String, Beer> beersById = input.second;

            ArrayList<Pair<FridgeItem, Beer>> result = new ArrayList<>();
            for (FridgeItem fridgeItem : fridgeItems) {
                Beer beer = beersById.get(fridgeItem.getBeerId());
                result.add(Pair.create(fridgeItem, beer));
            }
            return result;
        });
    }

    public LiveData<List<FridgeItem>> getMyFridgeItems(LiveData<String> currentUserId) {
        return switchMap(currentUserId, FridgeRepository::getFridgeItemsByUser);
    }


    public LiveData<FridgeItem> getMyFridgeItemForBeer(LiveData<String> currentUserId, LiveData<Beer> beer) {


        return switchMap(combineLatest(currentUserId, beer), FridgeRepository::getUserFridgeFor);
    }


}
