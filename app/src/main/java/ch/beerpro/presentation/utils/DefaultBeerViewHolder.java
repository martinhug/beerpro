package ch.beerpro.presentation.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import ch.beerpro.GlideApp;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;

public class DefaultBeerViewHolder<T extends OnDefaultBeerInteractionListener> extends RecyclerView.ViewHolder {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.manufacturer)
    TextView manufacturer;
    @BindView(R.id.category)
    TextView category;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.numRatings)
    TextView numRatings;

    DefaultBeerViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Beer beer, T listener) {
        name.setText(beer.getName());
        manufacturer.setText(beer.getManufacturer());
        category.setText(beer.getCategory());
        ratingBar.setNumStars(5);
        ratingBar.setRating(beer.getAvgRating());
        numRatings.setText(itemView.getResources().getString(R.string.fmt_num_ratings, beer.getNumRatings()));
        GlideApp.with(itemView).load(beer.getPhoto()).apply(new RequestOptions().override(240, 240).centerInside())
                .into(photo);
        itemView.setOnClickListener(v -> listener.onMoreClickedListener(photo, beer));
    }
}
