package ch.beerpro.presentation.utils;

import ch.beerpro.domain.models.FridgeItem;

public interface OnFridgeInteractionListener extends OnDefaultBeerInteractionListener {
    void onFridgeAddClickedListener(FridgeItem fridgeItem);
    void onFridgeRemoveClickedListener(FridgeItem fridgeItem);
}
