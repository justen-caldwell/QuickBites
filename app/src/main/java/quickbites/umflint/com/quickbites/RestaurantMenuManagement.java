package quickbites.umflint.com.quickbites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import quickbites.umflint.com.quickbites.Utilities.DatabaseAccessor;
import quickbites.umflint.com.quickbites.Utilities.MenuListAdapter;

public class RestaurantMenuManagement extends AppCompatActivity {

    private Button addMenuItem, removeMenuItem;
    private TextView restaurantTitle;
    private RecyclerView menu;
    private RecyclerView.LayoutManager layoutManager;
    private MenuListAdapter menuListAdapter;
    private DatabaseAccessor databaseAccessor;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu_management);
        setTitle("Menu Management");

        String userID = auth.getUid();

        databaseAccessor = DatabaseAccessor.getInstance();
        Query menu_query = databaseAccessor.getDatabaseReference().child("menu_items").child(userID);

        addMenuItem = findViewById(R.id.AddMenuItem);
        removeMenuItem = findViewById(R.id.RemoveMenuItem);
        restaurantTitle = findViewById(R.id.RestaurantTitle);
        menu = findViewById(R.id.MenuCardRecycler);
        menu.setAdapter(menuListAdapter);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        menu.setLayoutManager(layoutManager);

        databaseAccessor.access(false, menu_query, new DatabaseAccessor.OnGetDataListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HashMap<String, String>> menuHashMap = new ArrayList<>();
                HashMap<String, String> current_item;
                String current_header;

                for(DataSnapshot menu_header : dataSnapshot.getChildren()){
                    current_header = menu_header.getKey();
                    for(DataSnapshot item : menu_header.getChildren()){
                        current_item = (HashMap<String, String>) item.getValue();
                        menuHashMap.add(current_item);
                    }
                }

                menuListAdapter = new MenuListAdapter(menuHashMap);
                menu.setAdapter(menuListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurantMenuManagement.this, AddMenuItem.class));
                finish();
            }
        });


    }
}
