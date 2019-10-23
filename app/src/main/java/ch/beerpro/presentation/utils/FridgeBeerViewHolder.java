package ch.beerpro.presentation.utils;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;

public class FridgeBeerViewHolder extends DefaultBeerViewHolder{
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.addToFridge)
    Button addToFridge;
    @BindView(R.id.removeFromFridge)
    Button removeFromFridge;

    public FridgeBeerViewHolder( View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(FridgeItem fridgeItem, Beer beer, OnFridgeInteractionListener listener) {
        super.bind(beer, listener);
        String suffix;
        if (fridgeItem.getAmount() == 1) {
            suffix = "Bier";
        } else {
            suffix = "Biere";
        }
        amount.setText(String.format(Locale.GERMAN, "%d %s", fridgeItem.getAmount(), suffix));

        addToFridge.setOnClickListener(v -> listener.onFridgeAddClickedListener(fridgeItem));
        removeFromFridge.setOnClickListener(v -> listener.onFridgeRemoveClickedListener(fridgeItem));
    }
}
