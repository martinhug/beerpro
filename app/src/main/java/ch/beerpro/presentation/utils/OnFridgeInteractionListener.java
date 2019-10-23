package ch.beerpro.presentation.utils;

import android.widget.ImageView;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;


public interface OnFridgeInteractionListener {
    void onFridgeAddClickedListener(FridgeItem fridgeItem);
    void onFridgeRemoveClickedListener(FridgeItem fridgeItem);
}