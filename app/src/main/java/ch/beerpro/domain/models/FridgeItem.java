package ch.beerpro.domain.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class FridgeItem implements Entity {
    public static final String COLLECTION = "fridge";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_BEER_ID = "beerId";
    public static final String FIELD_AMOUNT = "amount";
    public static final String FIELD_CREATION_DATE = "creationDate";

    @Exclude
    private String id;

    private String userId;

    private String beerId;

    private Integer amount;

    private Date creationDate;


    public FridgeItem(String id, String userId, Integer amount, Date creationDate) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.creationDate = creationDate;
    }


    public static String generateId(String userId, String beerId) {
        return String.format("%s_%s", userId, beerId);
    }



    public static HashMap<String, FridgeItem> entitiesByBeerId(List<FridgeItem> entries) {
        HashMap<String, FridgeItem> byId = new HashMap<>();
        for (FridgeItem entry : entries) {
            byId.put(entry.getId(), entry);
        }
        return byId;
    }

    public Integer getAmount() {
        return amount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getBeerId() {
        return beerId;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
