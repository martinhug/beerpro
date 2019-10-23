package ch.beerpro.presentation.profile.myfridge;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.FridgeItem;
import ch.beerpro.presentation.details.DetailsActivity;
import ch.beerpro.presentation.utils.OnFridgeInteractionListener;


public class FridgeActivity extends AppCompatActivity implements OnFridgeInteractionListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    View emptyView;

    private FridgeViewModel model;
    private FridgeRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fridge);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_fridge));

        model = ViewModelProviders.of(this).get(FridgeViewModel.class);
        model.getMyFridgeListWithBeer().observe(this, this::updateFridge);

//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);

        adapter = new FridgeRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void updateFridge(List<Pair<FridgeItem, Beer>> pairs) {
        adapter.submitList(pairs);
        if (pairs.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            emptyView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
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


    public void onMoreClickedListener(ImageView photo, Beer beer) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.ITEM_ID, beer.getId());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, photo, "image");
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onFridgeAddClickedListener(FridgeItem fridgeItem) {
        model.addToFridge(fridgeItem);
    }

    @Override
    public void onFridgeRemoveClickedListener(FridgeItem fridgeItem) {
        model.removeFromFridge(fridgeItem);
    }
}
