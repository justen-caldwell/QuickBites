package quickbites.umflint.com.quickbites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import quickbites.umflint.com.quickbites.Utilities.DatabaseAccessor;

public class ViewMenuItem extends AppCompatActivity {

    private TextView titleText, itemName, descriptionText, priceLabel, priceAmount, descriptionLabel;

    private DatabaseAccessor databaseAccessor;

    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu_item);

        String menu_itemName = getIntent().getStringExtra("ITEM_NAME");
        String menu_headerName = getIntent().getStringExtra("ITEM_HEADER");
        String menu_itemOwner = getIntent().getStringExtra("ITEM_OWNER");

        setTitle(menu_itemName);

        final String userID = auth.getCurrentUser().getUid();
        databaseAccessor = DatabaseAccessor.getInstance();

        Query item_query = databaseAccessor.getDatabaseReference().child(menu_itemOwner).child(menu_headerName).child(menu_itemName);

        databaseAccessor.access(false, item_query, new DatabaseAccessor.OnGetDataListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot tempData = dataSnapshot.getChildren();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        titleText = findViewById(R.id.TitleText);
        itemName = findViewById(R.id.ItemName);
        descriptionText = findViewById(R.id.DescriptionText);
        priceLabel = findViewById(R.id.PriceLabel);
        priceAmount = findViewById(R.id.PriceAmount);
        descriptionLabel = findViewById(R.id.DescriptionLabel);




        Toast.makeText(getApplicationContext(),menu_headerName + ": " + menu_itemName + ", by " + menu_itemOwner, Toast.LENGTH_SHORT).show();
    }
}
