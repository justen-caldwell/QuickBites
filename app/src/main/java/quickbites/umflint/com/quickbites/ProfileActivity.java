package quickbites.umflint.com.quickbites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import quickbites.umflint.com.quickbites.Utilities.DatabaseAccessor;
import quickbites.umflint.com.quickbites.Utilities.RatingListAdapterUser;
import quickbites.umflint.com.quickbites.Utilities.RecyclerTouchListener;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.ProfilePicture)
    CircularImageView profilePicture;
    @BindView(R.id.FirstName)
    TextView firstName;
    @BindView(R.id.RatingsTitle)
    TextView ratingsTitle;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    List<HashMap<String, String>> ratingsList = new ArrayList<>();
    private DatabaseAccessor databaseAccessor;
    FirebaseAuth auth;
    private RatingListAdapterUser ratingListAdapterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String profile_uid = getIntent().getStringExtra("PROFILE_UID");

        ButterKnife.bind(this);
        recyclerView = findViewById(R.id.RatingsRecyclerView);
        recyclerView.setAdapter(ratingListAdapterUser);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        setTitle("Profile");
        String userID = FirebaseAuth.getInstance().getUid();


        databaseAccessor = DatabaseAccessor.getInstance();
        Query query = databaseAccessor.getDatabaseReference().child("ratings_by_user").child(profile_uid);
        Query user_information = databaseAccessor.getDatabaseReference().child("users").child("customers").child(profile_uid);

        databaseAccessor.access(true, user_information, new DatabaseAccessor.OnGetDataListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> user_data = (HashMap<String, String>) dataSnapshot.getValue();
                firstName.setText(user_data.get("firstName") + " " + user_data.get("lastName"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseAccessor.access(true, query, new DatabaseAccessor.OnGetDataListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rating : dataSnapshot.getChildren()) {
                    for (DataSnapshot single_rating : rating.getChildren()) {
                        ratingsList.add((HashMap<String, String>) single_rating.getValue());
                    }
                }
                ratingListAdapterUser = new RatingListAdapterUser(ratingsList);
                recyclerView.setAdapter(ratingListAdapterUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}



