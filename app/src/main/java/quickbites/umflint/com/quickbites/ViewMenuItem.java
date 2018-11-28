package quickbites.umflint.com.quickbites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.HashMap;

import quickbites.umflint.com.quickbites.Utilities.DatabaseAccessor;

public class ViewMenuItem extends AppCompatActivity {

    private TextView itemName, descriptionText, priceLabel, priceAmount, descriptionLabel;
    private Button deleteButton, rateButton;

    private DatabaseAccessor databaseAccessor;

    FirebaseAuth auth = FirebaseAuth.getInstance();


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

        rateButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);

        final String userID = auth.getCurrentUser().getUid();
        databaseAccessor = DatabaseAccessor.getInstance();

        final Query item_query = databaseAccessor.getDatabaseReference().child("menu_items").child(menu_itemOwner).child(menu_headerName).child(menu_itemName);
        final Query owner_query = databaseAccessor.getDatabaseReference().child("users").child("restaurants").child(menu_itemOwner).child("restaurantName");

        if(menu_itemOwner.equals(userID)){
            deleteButton.setVisibility(View.VISIBLE);
        }
        else{
            rateButton.setVisibility(View.VISIBLE);
        }

        databaseAccessor.access(true, owner_query, new DatabaseAccessor.OnGetDataListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username_snapshot = dataSnapshot.getValue().toString();
                String[] snapshot_array = username_snapshot.split("\\=");
                username_snapshot = snapshot_array[0];
                setTitle(username_snapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseAccessor.access(true, item_query, new DatabaseAccessor.OnGetDataListener() {
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



        Toast.makeText(getApplicationContext(),menu_headerName + ": " + menu_itemName + ", by " + menu_itemOwner, Toast.LENGTH_SHORT).show();
    }
}
