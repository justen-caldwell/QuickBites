package quickbites.umflint.com.quickbites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import quickbites.umflint.com.quickbites.Utilities.DatabaseAccessor;
import quickbites.umflint.com.quickbites.Utilities.MenuListAdapter;
import quickbites.umflint.com.quickbites.Utilities.RecyclerTouchListener;

public class ViewMenu extends AppCompatActivity {

    private Button addMenuItem;
    private RecyclerView menu;
    private RecyclerView.LayoutManager layoutManager;
    private MenuListAdapter menuListAdapter;
    private DatabaseAccessor databaseAccessor;
    String userID;
    String menu_itemOwner;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu);

        menu_itemOwner = getIntent().getStringExtra("ITEM_OWNER");

        userID = auth.getUid();

        databaseAccessor = DatabaseAccessor.getInstance();

        final Query owner_query = databaseAccessor.getDatabaseReference().child("users").child("restaurants").child(menu_itemOwner).child("restaurantName");
        final Query menu_query = databaseAccessor.getDatabaseReference().child("menu_items").child(menu_itemOwner);

        addMenuItem = findViewById(R.id.AddMenuItem);
        menu = findViewById(R.id.MenuCardRecycler);
        menu.setAdapter(menuListAdapter);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        menu.setLayoutManager(layoutManager);


        addMenuItem.setVisibility(View.GONE);
        if(menu_itemOwner.equals(userID)){
            addMenuItem.setVisibility(View.VISIBLE);
        }


        databaseAccessor.access(true, owner_query, new DatabaseAccessor.OnGetDataListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username_snapshot = dataSnapshot.getValue().toString();
                String[] snapshot_array = username_snapshot.split("\\=");
                username_snapshot = snapshot_array[0];
                username_snapshot = username_snapshot + "'s Menu";
                setTitle(username_snapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final List<HashMap<String, String>> menuHashMap = new ArrayList<>();
        databaseAccessor.access(true, menu_query, new DatabaseAccessor.OnGetDataListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> current_item;
                HashMap<String, String> current_itemHeader;
                String current_header;

                for(DataSnapshot menu_header : dataSnapshot.getChildren()){
                    current_header = menu_header.getKey();
                    current_itemHeader = new HashMap<>();
                    current_itemHeader.put("item_header", current_header);
                    menuHashMap.add(current_itemHeader);
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
                startActivity(new Intent(ViewMenu.this, AddMenuItem.class));
                finish();
            }
        });

        menu.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), menu, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                String item_to_open = menuHashMap.get(position).get("item_name");;
                String header_to_open = menuHashMap.get(position).get("menu_category");
                //Toast.makeText(getApplicationContext(), "Deleting: " + menuHashMap.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), ViewMenuItem.class);
                intent.putExtra("ITEM_NAME", item_to_open);
                intent.putExtra("ITEM_HEADER", header_to_open);
                intent.putExtra("ITEM_OWNER", menu_itemOwner);
                startActivity(intent);
            }
        }));

    }

    @Override
    public void onBackPressed(){
        if(userID.equals(menu_itemOwner)){

        }
        else{
            super.onBackPressed();
        }
    }
}
