package ch.beerpro.presentation.utils;

import android.widget.ImageView;

import ch.beerpro.domain.models.Beer;

public interface OnDefaultBeerInteractionListener {
    void onMoreClickedListener(ImageView photo, Beer beer);
}
