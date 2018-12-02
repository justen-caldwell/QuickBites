package quickbites.umflint.com.quickbites;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import quickbites.umflint.com.quickbites.Utilities.PrivateController;

public class RatingFragment extends Fragment {
    FragmentActivity listener;
    PrivateController privateController;
    //RatingFragment ratingFragment;


    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }

    public static RatingFragment newInstance() {
        return new RatingFragment();

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View RootView = inflater.inflate(R.layout.rating_fragment, parent, false);
        Button submitButton = RootView.findViewById(R.id.Fragment_SubmitButton);
        Button cancelButton = RootView.findViewById(R.id.Fragment_CancelButton);
        RatingBar ratingBar = RootView.findViewById(R.id.Fragment_RatingBar);
        //privateController = (ViewMenuItem) getActivity();
        final RatingFragment ratingFragment = this;
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
                privateController.closeFragment(ratingFragment);
            }
        });

        return RootView;
    }

    public interface InteractFragment {
        void closeFragment(Fragment fragment);
    }

}