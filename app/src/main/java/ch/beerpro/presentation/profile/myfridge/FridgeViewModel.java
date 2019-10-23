package ch.beerpro.presentation.profile.myfridge;

import android.util.Pair;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.presentation.profile.BasicFridgeViewModel;

public class FridgeViewModel extends BasicFridgeViewModel implements CurrentUser {
    private final BeersRepository beersRepository;

    public FridgeViewModel() {
        super();
        beersRepository = new BeersRepository();
        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<Pair<FridgeItem, Beer>>> getMyFridgeListWithBeer() {
        LiveData<List<Beer>> allBeers = beersRepository.getAllBeers();
        return fridgeRepository.getMyFridgeWithBeers(currentUserId, allBeers);
    }

}
