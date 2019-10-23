package ch.beerpro.presentation.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.beerpro.R;

public class SingleBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private DetailsViewModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_bottom_sheet_dialog, container, false);
        ButterKnife.bind(this, view);
        model = ViewModelProviders.of(getActivity()).get(DetailsViewModel.class);
        return view;
    }

    @OnClick(R.id.addToFridge)
    public void onAddToFridgeClickedListener(View view){
        model.addToFridge(model.getBeer().getValue().getId());
        Toast toast = Toast.makeText(getContext(), "Das Bier wurde zum Kühlschrank hinzugefügt.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
