package quickbites.umflint.com.quickbites.Profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import quickbites.umflint.com.quickbites.R;
import quickbites.umflint.com.quickbites.Utilities.DatabaseAccessor;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.ProfilePicture)
    CircularImageView profilePicture;
    @BindView(R.id.FirstName)
    TextView firstName;
    @BindView(R.id.LastName)
    TextView lastName;
    @BindView(R.id.NumOfRatings)
    TextView numOfRatings;
    @BindView(R.id.RatingsNumber)
    TextView ratingsNumber;
    @BindView(R.id.FavoritesTitle)
    TextView favoritesTitle;
    @BindView(R.id.FavoritesRecyclerView)
    RecyclerView favoritesRecycler;
    @BindView(R.id.RatingsTitle)
    TextView ratingsTitle;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MenuListAdapter menuListAdapter;
    private DatabaseAccessor databaseAccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        recyclerView = findViewById(R.id.RatingsRecyclerView);
        recyclerView.setAdapter(menuListAdapter);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        setTitle("Profile");

        databaseAccessor = DatabaseAccessor.getInstance();

        Query query = databaseAccessor.getDatabaseReference()
                .child("MenuItem");

        databaseAccessor.access(false, query, new DatabaseAccessor.OnGetDataListener() {
            @Override
            public void onSuccessfulAdd(DataSnapshot dataSnapshot) {

                List<HashMap<String, String>> menuHashMap = new ArrayList<>();

                HashMap<String, String> current_item;

                for (DataSnapshot menu_item : dataSnapshot.getChildren()) {
                    current_item = (HashMap<String, String>) menu_item.getValue();
                    menuHashMap.add(current_item);
                }

                menuListAdapter = new MenuListAdapter(menuHashMap);
                recyclerView.setAdapter(menuListAdapter);
            }

            @Override
            public void onSuccessfulChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onSuccessfulRemoval(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onSuccessfulMove(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onFailure(DatabaseError databaseError) {

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
