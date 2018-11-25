package quickbites.umflint.com.quickbites;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import quickbites.umflint.com.quickbites.Utilities.DatabaseAccessor;


public class UserSignUp extends AppCompatActivity {

    private TextInputLayout textInputFirstName, textInputLastName, textInputPhone;
    private TextView accountTypeText, accountInformationText, selectedTypeText;
    private EditText inputFirstName, inputLastName, inputPhone;
    private Button customerButton, restaurantButton, submitButton;
    private LinearLayout inputLinearLayout;

    private DatabaseAccessor databaseAccessor;

    private boolean selectedProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        databaseAccessor = DatabaseAccessor.getInstance();

        inputLinearLayout = findViewById(R.id.InputLinearLayout);
        accountTypeText = findViewById(R.id.AccountTypeTextView);
        accountInformationText = findViewById(R.id.AccountInformationTextView);
        selectedTypeText = findViewById(R.id.SelectedTypeTextView);
        restaurantButton = findViewById(R.id.RestaurantButton);
        customerButton = findViewById(R.id.CustomerButton);
        submitButton = findViewById(R.id.SubmitButton);
        inputFirstName = findViewById(R.id.InputFirstName);
        inputLastName = findViewById(R.id.InputLastName);
        inputPhone = findViewById(R.id.InputPhone);
        textInputFirstName = findViewById(R.id.TextInputFirstName);
        textInputLastName = findViewById(R.id.TextInputLastName);
        textInputPhone = findViewById(R.id.TextInputPhone);

        inputLinearLayout.setVisibility(View.GONE);
        selectedTypeText.setVisibility(View.INVISIBLE);
        textInputPhone.setHint("Phone Number");

        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedProfile = true;
                selectedTypeText.setText("Account Type: Restaurant");
                textInputFirstName.setHint("Restaurant Name");
                textInputLastName.setHint("Address");

                if (textInputPhone.getVisibility() == View.GONE) {
                    textInputPhone.setVisibility(View.VISIBLE);
                }

                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fui_slide_in_right);
                selectedTypeText.setVisibility(View.VISIBLE);
                inputLinearLayout.startAnimation(slideIn);
                inputLinearLayout.setVisibility(View.VISIBLE);

            }
        });

        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedProfile = false;
                textInputFirstName.setHint("First Name");
                textInputLastName.setHint("Last Name");
                selectedTypeText.setText("Account Type: Customer");
                textInputPhone.setVisibility(View.GONE);

                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fui_slide_in_right);
                selectedTypeText.setVisibility(View.VISIBLE);
                inputLinearLayout.startAnimation(slideIn);
                inputLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the profile being created is a restaurant
                if (selectedProfile) {
                    //
                } else {

                }
            }
        });

    }
}
