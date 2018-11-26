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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserRegister extends AppCompatActivity {

    private TextInputLayout textInputFirstName, textInputLastName, textInputPhone;
    private TextView accountTypeText, accountInformationText, selectedTypeText;
    private EditText inputFirstName, inputLastName, inputPhone;
    private Button customerButton, restaurantButton, submitButton;
    private LinearLayout inputLinearLayout, buttonContainer;


    private boolean selectedProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        setTitle("Create Profile");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String userID = user.getUid();
        }

        buttonContainer = findViewById(R.id.ButtonContainer);
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
        accountInformationText.setVisibility(View.INVISIBLE);
        textInputPhone.setHint("Phone Number");

        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedProfile = true;
                selectedTypeText.setText("Account Type: Restaurant");
                textInputFirstName.setHint("Restaurant Name");
                textInputLastName.setHint("Address");

                accountTypeText.setVisibility(View.GONE);
                buttonContainer.setVisibility(View.GONE);


                if (textInputPhone.getVisibility() == View.GONE) {
                    textInputPhone.setVisibility(View.VISIBLE);
                }

                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fui_slide_in_right);
                selectedTypeText.setVisibility(View.VISIBLE);
                accountInformationText.setVisibility(View.VISIBLE);
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
                accountTypeText.setVisibility(View.GONE);
                buttonContainer.setVisibility(View.GONE);

                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fui_slide_in_right);
                selectedTypeText.setVisibility(View.VISIBLE);
                accountInformationText.setVisibility(View.VISIBLE);
                inputLinearLayout.startAnimation(slideIn);
                inputLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the profile being created is a restaurant
                if (selectedProfile == true) {
                    Toast.makeText(getApplicationContext(), "Select Account Type", Toast.LENGTH_SHORT).show();
                }
                else if (selectedProfile == false){
                    Toast.makeText(getApplicationContext(), "Select Account Type", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Select Account Type", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed(){
        if (accountTypeText.getVisibility() == View.GONE){
            selectedTypeText.setVisibility(View.GONE);
            accountInformationText.setVisibility(View.GONE);

            if(inputLinearLayout.getVisibility() == View.VISIBLE) {
                inputLinearLayout.setVisibility(View.GONE);
            }

            accountTypeText.setVisibility(View.VISIBLE);
            buttonContainer.setVisibility(View.VISIBLE);
        }
    }
}