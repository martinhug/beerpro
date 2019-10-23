package ch.beerpro.data.repositories;

import android.util.Pair;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class FridgeRepository {
    private static LiveData<List<FridgeItem>> getFridgeItemsByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(FridgeItem.COLLECTION).whereEqualTo(FridgeItem.FIELD_USER_ID, userId),
                FridgeItem.class);
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

    public Task<Void> addFridgeItem(String userId, String itemId) {
        return addFridgeItem(userId, itemId, 1);
    }

    public Task<Void> addFridgeItem(String userId, String itemId, int amount) {
        return updateAmount(userId, itemId, amount);
    }

    public Task<Void> removeFridgeItem(String userId, String itemId) {
        return removeFridgeItem(userId, itemId, 1);
    }

    public Task<Void> removeFridgeItem(String userId, String itemId, int amount) {
        return updateAmount(userId, itemId, -amount);
    }

    private Task<Void> updateAmount(String userId, String itemId, int amount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String fridgeItemId = FridgeItem.generateId(userId, itemId);
        DocumentReference fridgeItemQuery = db.collection(FridgeItem.COLLECTION).document(fridgeItemId);

        return fridgeItemQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                long currentAmount = task.getResult().getLong(FridgeItem.FIELD_AMOUNT);
                if (currentAmount + amount == 0) {
                    return fridgeItemQuery.delete();
                }
                fridgeItemQuery.update(FridgeItem.FIELD_AMOUNT, currentAmount + amount);
                return fridgeItemQuery.update(FridgeItem.FIELD_CREATION_DATE, new Date());
            } else if (task.isSuccessful()) {
                return fridgeItemQuery.set(new FridgeItem(userId, itemId, amount, new Date()));
            } else {
                throw task.getException();
            }
        });
    }
}
