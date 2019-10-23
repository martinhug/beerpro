package ch.beerpro.presentation.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ch.beerpro.data.repositories.FridgeRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;

public class BasicFridgeViewModel extends ViewModel {
    protected final MutableLiveData<String> currentUserId = new MutableLiveData<>();
    protected final FridgeRepository fridgeRepository;

    public BasicFridgeViewModel() {
        fridgeRepository = new FridgeRepository();
    }

    public void addToFridge(FridgeItem fridgeItem) {
        fridgeRepository.addFridgeItem(currentUserId.getValue(), fridgeItem.getBeerId());
    }

    public void removeFromFridge(FridgeItem fridgeItem) {
        fridgeRepository.removeFridgeItem(currentUserId.getValue(), fridgeItem.getBeerId());
    }

    public void addToFridge(Beer item) {
        fridgeRepository.addFridgeItem(currentUserId.getValue(), item.getId());
    }
}
