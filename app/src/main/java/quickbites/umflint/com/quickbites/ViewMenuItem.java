package quickbites.umflint.com.quickbites;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import quickbites.umflint.com.quickbites.Utilities.DatabaseAccessor;
import quickbites.umflint.com.quickbites.Utilities.RatingListAdapter;
import quickbites.umflint.com.quickbites.Utilities.RecyclerTouchListener;

public class ViewMenuItem extends AppCompatActivity {

    List<HashMap<String, String>> ratingsList = new ArrayList<>();

    private TextView itemName, descriptionText, priceLabel, priceAmount, descriptionLabel;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseAccessor databaseAccessor;
    private Button deleteButton, rateButton, cancelButton, submitButton;
    private ConstraintLayout ratingLayout;
    private RatingBar ratingBar;
    private String user_fullname;
    private String restaurant_name;
    private RecyclerView ratingRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private RatingListAdapter ratingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu_item);

        final String menu_itemName = getIntent().getStringExtra("ITEM_NAME");
        final String menu_headerName = getIntent().getStringExtra("ITEM_HEADER");
        final String menu_itemOwner = getIntent().getStringExtra("ITEM_OWNER");

        itemName = findViewById(R.id.ItemName);
        descriptionText = findViewById(R.id.DescriptionText);
        priceLabel = findViewById(R.id.PriceLabel);
        priceAmount = findViewById(R.id.PriceAmount);
        descriptionLabel = findViewById(R.id.DescriptionLabel);

        rateButton = findViewById(R.id.RateButton);
        deleteButton = findViewById(R.id.DeleteButton);
        cancelButton = findViewById(R.id.CancelButton);
        submitButton = findViewById(R.id.SubmitButton);

        ratingRecycler = findViewById(R.id.RatingsRecycler);
        ratingRecycler.setAdapter(ratingListAdapter);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        ratingRecycler.setLayoutManager(layoutManager);
        ratingBar = findViewById(R.id.RatingBar);

        ratingLayout = findViewById(R.id.RatingLayout);
        ratingLayout.setVisibility(View.GONE);

        rateButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);


        //ratingFrame = findViewById(R.id.your_placeholder);
        //ratingFrame.setVisibility(View.GONE);

        final String userID = auth.getCurrentUser().getUid();
        databaseAccessor = DatabaseAccessor.getInstance();

        final Query item_query = databaseAccessor.getDatabaseReference().child("menu_items").child(menu_itemOwner).child(menu_headerName).child(menu_itemName);
        final Query owner_query = databaseAccessor.getDatabaseReference().child("users").child("restaurants").child(menu_itemOwner).child("restaurantName");
        final Query rating_query = databaseAccessor.getDatabaseReference().child("ratings_by_item").child(menu_itemOwner).child(menu_itemName);

        if(menu_itemOwner.equals(userID)){
            deleteButton.setVisibility(View.VISIBLE);
        }
        else{
            rateButton.setVisibility(View.VISIBLE);
            Query username_query = databaseAccessor.getDatabaseReference().child("users").child("customers").child(userID);
            databaseAccessor.access(false, username_query, new DatabaseAccessor.OnGetDataListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user_fullname = (dataSnapshot.child("firstName").getValue()) + " " + (dataSnapshot.child("lastName").getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        databaseAccessor.access(false, owner_query, new DatabaseAccessor.OnGetDataListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                restaurant_name = dataSnapshot.getValue().toString();
                String[] snapshot_array = restaurant_name.split("\\=");
                restaurant_name = snapshot_array[0];
                setTitle(restaurant_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseAccessor.access(false, item_query, new DatabaseAccessor.OnGetDataListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> item_information = (HashMap<String, String>) dataSnapshot.getValue();
                itemName.setText(item_information.get("item_name"));
                descriptionText.setText("\t\t\t" + item_information.get("item_description"));
                priceAmount.setText(item_information.get("item_price"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseAccessor.access(true, rating_query, new DatabaseAccessor.OnGetDataListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot rating : dataSnapshot.getChildren()) {
                    ratingsList.add((HashMap<String, String>) rating.getValue());
                }

                ratingListAdapter = new RatingListAdapter(ratingsList);
                ratingRecycler.setAdapter(ratingListAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ratingRecycler.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), ratingRecycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String profile_to_open = ratingsList.get(position).get("customerUID");
                Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                intent.putExtra("PROFILE_UID", profile_to_open);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseAccessor.getDatabaseReference().child("menu_items").child(menu_itemOwner).child(menu_headerName).child(menu_itemName).removeValue();
                Intent intent = new Intent(getBaseContext(), ViewMenu.class);
                intent.putExtra("ITEM_OWNER", userID);
                startActivity(intent);
                finish();
            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingLayout.setVisibility(View.VISIBLE);
                rateButton.setVisibility(View.GONE);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingLayout.setVisibility(View.GONE);
                rateButton.setVisibility(View.VISIBLE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating_number = String.valueOf(ratingBar.getRating());
                Rating rating = new Rating(userID, menu_itemOwner, rating_number, menu_itemName, user_fullname, restaurant_name);
                databaseAccessor.getDatabaseReference().child("ratings_by_user").child(userID).child(menu_itemOwner).child(menu_itemName).setValue(rating);
                databaseAccessor.getDatabaseReference().child("ratings_by_item").child(menu_itemOwner).child(menu_itemName).child(userID).setValue(rating);
                ratingLayout.setVisibility(View.GONE);
                rateButton.setVisibility(View.VISIBLE);
            }
        });



        Toast.makeText(getApplicationContext(),menu_headerName + ": " + menu_itemName + ", by " + menu_itemOwner, Toast.LENGTH_SHORT).show();
    }

    public static class Rating {
        public String customerUID;
        public String restaurantUID;
        public String ratingNumber;
        public String menuItemName;
        public String userName;
        public String restaurantName;

        public Rating() {
        }

        public Rating(String customerUID_in, String restaurantUID_in, String ratingNumber_in, String menuItemName_in, String userName_in, String restaurantName_in) {
            customerUID = customerUID_in;
            restaurantUID = restaurantUID_in;
            ratingNumber = ratingNumber_in;
            menuItemName = menuItemName_in;
            userName = userName_in;
            restaurantName = restaurantName_in;
        }
    }

}

