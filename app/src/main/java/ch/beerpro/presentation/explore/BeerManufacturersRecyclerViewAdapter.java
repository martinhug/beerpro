package ch.beerpro.presentation.explore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.R;
import ch.beerpro.presentation.MainActivity;
import ch.beerpro.presentation.utils.BackgroundImageProvider;
import ch.beerpro.presentation.utils.StringDiffItemCallback;


/**
 * This class is really similar to {@link BeerCategoriesRecyclerViewAdapter} see the documentation there.
 */
public class BeerManufacturersRecyclerViewAdapter
        extends ListAdapter<String, BeerManufacturersRecyclerViewAdapter.ViewHolder> {

    private final BeerManufacturersFragment.OnItemSelectedListener listener;
    private Context mContext;

    BeerManufacturersRecyclerViewAdapter(BeerManufacturersFragment.OnItemSelectedListener listener) {
        super(new StringDiffItemCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_explore_beer_manufacturers_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bind(getItem(position), position, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content)
        TextView content;

        @BindView(R.id.imageView)
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(String item, int position, BeerManufacturersFragment.OnItemSelectedListener listener) {
            content.setText(item);
            Context resources = itemView.getContext();
            imageView.setImageDrawable(BackgroundImageProvider.getBackgroundImage(resources, position + 10));
            if (listener != null) {
                itemView.setOnClickListener(v -> {
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).onBeerManufacturerSelected(item);
                    }
                });
            }
        }
    }
}
