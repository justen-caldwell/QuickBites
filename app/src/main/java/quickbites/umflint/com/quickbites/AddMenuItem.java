package quickbites.umflint.com.quickbites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMenuItem extends AppCompatActivity {

    public static class MenuItem{
        public String item_name;
        public String item_price;
        public String item_description;
        public String menu_category;
        public String item_owner;

        public MenuItem(){}

        public MenuItem(String item_nameIn, String item_priceIn, String item_descriptionIn, String menu_categoryIn, String item_ownerIn){
            item_name = item_nameIn;
            item_price = item_priceIn;
            item_description = item_descriptionIn;
            menu_category = menu_categoryIn;
            item_owner = item_ownerIn;
        }
    }

    private Button submitButton;
    private EditText name, price, description, category;
    private TextView title;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    DatabaseReference reference = databaseReference.getRef().child("menu_items");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);
        setTitle("Add A Menu Item");

        final String userID = auth.getUid();


        submitButton = findViewById(R.id.SubmitButton);
        name = findViewById(R.id.InputMenuName);
        price = findViewById(R.id.InputPrice);
        description = findViewById(R.id.InputDescription);
        category = findViewById(R.id.InputCategory);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String menuName = name.getText().toString().trim();
                String menuPrice = price.getText().toString().trim();
                String menuDescription = description.getText().toString().trim();
                String menuCategory = category.getText().toString().trim();

                if (TextUtils.isEmpty(menuName)) {
                    Toast.makeText(getApplicationContext(), "Enter A Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(menuPrice)) {
                    Toast.makeText(getApplicationContext(), "Enter A Price", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(menuDescription)) {
                    Toast.makeText(getApplicationContext(), "Enter A Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(menuDescription)) {
                    Toast.makeText(getApplicationContext(), "Enter A Category", Toast.LENGTH_SHORT).show();
                    return;
                }

                MenuItem menuItem = new MenuItem(menuName, menuPrice, menuDescription, menuCategory, userID);
                reference.child(userID).child(menuCategory).child(menuName).setValue(menuItem);
                startActivity(new Intent(AddMenuItem.this, ViewMenu.class));
                finish();

            }
        });
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(AddMenuItem.this, ViewMenu.class));
        finish();
    }
}
