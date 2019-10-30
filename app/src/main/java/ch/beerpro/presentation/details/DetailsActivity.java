package ch.beerpro.presentation.details;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.beerpro.GlideApp;
import ch.beerpro.R;
import ch.beerpro.data.repositories.FridgeRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Price;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;
import ch.beerpro.presentation.details.createrating.CreateRatingActivity;
import ch.beerpro.presentation.details.filters.PriceFieldInputFilter;

import static ch.beerpro.presentation.utils.DrawableHelpers.setDrawableTint;

public class DetailsActivity extends AppCompatActivity implements OnRatingLikedListener {

    public static final String ITEM_ID = "item_id";
    private static final String TAG = "DetailsActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    @BindView(R.id.photo)
    ImageView photo;

    @BindView(R.id.avgRating)
    TextView avgRating;

    @BindView(R.id.numRatings)
    TextView numRatings;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.wishlist)
    ToggleButton wishlist;

    @BindView(R.id.manufacturer)
    TextView manufacturer;

    @BindView(R.id.category)
    TextView category;

    @BindView(R.id.addRatingBar)
    RatingBar addRatingBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.averagePrice)
    TextView averagePrice;

    private RatingsRecyclerViewAdapter adapter;

    private DetailsViewModel model;

    private String beerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        toolbar.setTitleTextColor(Color.alpha(0));

        beerId = Objects.requireNonNull(getIntent().getExtras()).getString(ITEM_ID);

        model = ViewModelProviders.of(this).get(DetailsViewModel.class);
        model.setBeerId(beerId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RatingsRecyclerViewAdapter(this, model.getCurrentUser());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        model.getBeer().observe(this, this::updateBeer);
        model.getRatings().observe(this, this::updateRatings);
        model.getWish().observe(this, this::toggleWishlistView);
        model.getPricesForBeer().observe(this, this::updatePrice);

        recyclerView.setAdapter(adapter);
        addRatingBar.setOnRatingBarChangeListener(this::addNewRating);
    }

    private void addNewRating(RatingBar ratingBar, float v, boolean b) {
        Intent intent = new Intent(this, CreateRatingActivity.class);
        intent.putExtra(CreateRatingActivity.ITEM, model.getBeer().getValue());
        intent.putExtra(CreateRatingActivity.RATING, v);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, addRatingBar, "rating");
        startActivity(intent, options.toBundle());
    }

    @OnClick(R.id.share)
    public void onShareListener(View view) {

        Beer beer = model.getBeer().getValue();

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

        shareIntent.setType("text/plain");

        String title = "Gönn dir eine Abkühlung mit Beerpro: " + beer.getName();

        String shareBody = "Probier das Bier: " + beer.getName() + "!\n";

        if (beer.getNumRatings() > 0) {
            if (beer.getNumRatings() > 1) {
                shareBody += beer.getNumRatings() + " Personen haben dieses Bier mit " + Math.round(beer.getAvgRating()) + " Sternen bewertet!" + "\n";
            } else {
                shareBody += "Eine Person hat dieses Bier mit " + Math.round(beer.getAvgRating()) + " Sternen bewertet!" + "\n";
            }

        }
        shareBody += beer.getManufacturer() + "\n";
        shareBody += beer.getCategory() + "\n";
        shareBody += "Ich kann das Bier nur empfehlen!";

        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Teile das Bier mit.."));
    }

    @OnClick(R.id.actionsButton)
    public void showBottomSheetDialog() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.single_bottom_sheet_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();

        Button addToFridge = dialog.findViewById(R.id.addToFridge);
        FridgeRepository fridgeRepository = new FridgeRepository();
        assert addToFridge != null;
        addToFridge.setOnClickListener(viewNew -> fridgeRepository.addUserFridgeItem(model.getCurrentUser().getUid(), beerId));

        View addPrice = view.findViewById(R.id.addPrice);
        addPrice.setOnClickListener(getPriceListener());
    }

    private void updateBeer(Beer item) {
        name.setText(item.getName());
        manufacturer.setText(item.getManufacturer());
        category.setText(item.getCategory());
        name.setText(item.getName());
        GlideApp.with(this).load(item.getPhoto()).apply(new RequestOptions().override(120, 160).centerInside())
                .into(photo);
        ratingBar.setNumStars(5);
        ratingBar.setRating(item.getAvgRating());
        avgRating.setText(getResources().getString(R.string.fmt_avg_rating, item.getAvgRating()));
        numRatings.setText(getResources().getString(R.string.fmt_ratings, item.getNumRatings()));
        toolbar.setTitle(item.getName());
    }

    private void updatePrice(List<Price> pricesForBeer) {
        double total = 0;
        for (Price price : pricesForBeer) {
            total += price.getPrice();
        }
        averagePrice.setText(String.valueOf(total / pricesForBeer.size()));
    }

    private void updateRatings(List<Rating> ratings) {
        adapter.submitList(new ArrayList<>(ratings));
    }

    private View.OnClickListener getPriceListener() {
        return view -> showPriceDialog(view.getContext());
    }

    private void showPriceDialog(Context context) {
        EditText price = new EditText(context);
        price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        price.setHint("Price");
        price.setFilters(new InputFilter[]{new PriceFieldInputFilter()});
        new AlertDialog.Builder(context)
                .setTitle("Add price")
                .setView(price)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> model.savePrice(beerId, Float.parseFloat(price.getText().toString())))
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onRatingLikedListener(Rating rating) {
        model.toggleLike(rating);
    }

    @OnClick(R.id.wishlist)
    public void onWishClickedListener(View view) {
        model.toggleItemInWishlist(Objects.requireNonNull(model.getBeer().getValue()).getId());
        /*
         * We won't get an update from firestore when the wish is removed, so we need to reset the UI state ourselves.
         * */
        if (!wishlist.isChecked()) {
            toggleWishlistView(null);
        }
    }

    private void toggleWishlistView(Wish wish) {
        if (wish != null) {
            int color = getResources().getColor(R.color.colorPrimary);
            setDrawableTint(wishlist, color);
            wishlist.setChecked(true);
        } else {
            int color = getResources().getColor(android.R.color.darker_gray);
            setDrawableTint(wishlist, color);
            wishlist.setChecked(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
