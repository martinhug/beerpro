package ch.beerpro.presentation.profile.myfridge;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import butterknife.ButterKnife;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.presentation.utils.FridgeBeerViewHolder;
import ch.beerpro.presentation.utils.EntityPairDiffItemCallback;
import ch.beerpro.presentation.utils.OnFridgeInteractionListener;

public class FridgeRecyclerViewAdapter extends ListAdapter<Pair<FridgeItem, Beer>, FridgeRecyclerViewAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<Pair<FridgeItem, Beer>> DIFF_CALLBACK = new EntityPairDiffItemCallback<>();
    private final OnFridgeInteractionListener listener;

    public FridgeRecyclerViewAdapter(OnFridgeInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_my_fridge_listentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<FridgeItem, Beer> item = getItem(position);
        holder.bind(item.first, item.second, listener);
    }

    class ViewHolder extends FridgeBeerViewHolder {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(FridgeItem fridgeItem, Beer beer, OnFridgeInteractionListener listener) {
            super.bind(fridgeItem, beer, listener);
        }
    }
}
