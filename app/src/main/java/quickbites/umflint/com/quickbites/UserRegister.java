package quickbites.umflint.com.quickbites;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class UserRegister extends AppCompatActivity {

    private TextInputLayout textInputFirstName, textInputLastName, textInputAddress, textInputState, textInputZip, textInputPhone;
    private TextView accountTypeText, accountInformationText, selectedTypeText, selectProfilePicture;
    private EditText inputFirstName, inputLastName, inputPhone, inputAddress, inputState, inputZip;
    private Button customerButton, restaurantButton, submitButton;
    private LinearLayout inputLinearLayout, buttonContainer;
    private CircularImageView profilePicture;
    private static final int SELECT_PICTURE = 0;

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




        // Text prompts
        selectProfilePicture = findViewById(R.id.ProfilePicSelectionText);
        accountTypeText = findViewById(R.id.AccountTypeTextView);
        accountInformationText = findViewById(R.id.AccountInformationTextView);
        selectedTypeText = findViewById(R.id.SelectedTypeTextView);

        // Buttons
        restaurantButton = findViewById(R.id.RestaurantButton);
        customerButton = findViewById(R.id.CustomerButton);
        submitButton = findViewById(R.id.SubmitButton);

        // Text inputs
        textInputFirstName = findViewById(R.id.TextInputFirstName);
        textInputLastName = findViewById(R.id.TextInputLastName);
        textInputAddress = findViewById(R.id.TextInputAddress);
        textInputState = findViewById(R.id.TextInputState);
        textInputZip = findViewById(R.id.TextInputZip);
        textInputPhone = findViewById(R.id.TextInputPhone);

        // Edit texts
        inputFirstName = findViewById(R.id.InputFirstName);
        inputLastName = findViewById(R.id.InputLastName);
        inputAddress = findViewById(R.id.InputAddress);
        inputState = findViewById(R.id.InputState);
        inputZip = findViewById(R.id.InputZip);
        inputPhone = findViewById(R.id.InputPhone);

        // Containers
        buttonContainer = findViewById(R.id.ButtonContainer);
        inputLinearLayout = findViewById(R.id.InputLinearLayout);

        // Initial hides for onCreate format
        inputLinearLayout.setVisibility(View.GONE);
        selectedTypeText.setVisibility(View.GONE);
        accountInformationText.setVisibility(View.GONE);
        profilePicture.setVisibility(View.GONE);
        selectProfilePicture.setVisibility(View.GONE);

        //When the restaurant button is clicked.
        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedProfile = true;
                selectedTypeText.setText("Account Type: Restaurant");
                textInputFirstName.setHint("Restaurant Name");
                selectProfilePicture.setText("Choose Restaurant Picture");

                // Make text fields not relevant to restaurant gone
                // Make the top selection buttons go away, will return upon back button clicked
                textInputLastName.setVisibility(View.GONE);
                accountTypeText.setVisibility(View.GONE);
                buttonContainer.setVisibility(View.GONE);

                // Make text fields only relevant to restaurants visible
                textInputAddress.setVisibility(View.VISIBLE);
                textInputState.setVisibility(View.VISIBLE);
                textInputZip.setVisibility(View.VISIBLE);
                textInputPhone.setVisibility(View.VISIBLE);

                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fui_slide_in_right);
                selectedTypeText.setVisibility(View.VISIBLE);
                accountInformationText.setVisibility(View.VISIBLE);
                inputLinearLayout.startAnimation(slideIn);
                profilePicture.startAnimation(slideIn);
                selectProfilePicture.startAnimation(slideIn);
                inputLinearLayout.setVisibility(View.VISIBLE);
                profilePicture.setVisibility(View.VISIBLE);
                selectProfilePicture.setVisibility(View.VISIBLE);

            }
        });

        // When the customer button is clicked.
        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedProfile = false;
                textInputFirstName.setHint("First Name");
                selectedTypeText.setText("Account Type: Customer");
                selectProfilePicture.setText("Choose Profile Picture");

                textInputAddress.setVisibility(View.GONE);
                textInputState.setVisibility(View.GONE);
                textInputZip.setVisibility(View.GONE);
                textInputPhone.setVisibility(View.GONE);
                accountTypeText.setVisibility(View.GONE);
                buttonContainer.setVisibility(View.GONE);

                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fui_slide_in_right);
                selectedTypeText.setVisibility(View.VISIBLE);
                accountInformationText.setVisibility(View.VISIBLE);
                textInputLastName.setVisibility(View.VISIBLE);
                inputLinearLayout.startAnimation(slideIn);
                profilePicture.startAnimation(slideIn);
                selectProfilePicture.startAnimation(slideIn);
                inputLinearLayout.setVisibility(View.VISIBLE);
                profilePicture.setVisibility(View.VISIBLE);
                selectProfilePicture.setVisibility(View.VISIBLE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the profile being created is a restaurant
                if (selectedProfile == true) {
                    // Data to be submitted
                    String restaurantName = inputFirstName.getText().toString().trim();
                    String address = inputAddress.getText().toString().trim();
                    String state = inputState.getText().toString().trim();
                    String zipCode = inputZip.getText().toString().trim();
                    String phoneNum = inputPhone.getText().toString().trim();

                    if (TextUtils.isEmpty(restaurantName)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(address)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(state)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(zipCode)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(phoneNum)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String fullAddress = address + ", " + state + " " + zipCode;




                    Toast.makeText(getApplicationContext(), "Restaurant Account Created", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profilePicture.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(UserRegister.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(UserRegister.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed(){
        if (accountTypeText.getVisibility() == View.GONE){
            selectedTypeText.setVisibility(View.GONE);
            accountInformationText.setVisibility(View.GONE);
            profilePicture.setVisibility(View.GONE);
            selectProfilePicture.setVisibility(View.GONE);

            if(inputLinearLayout.getVisibility() == View.VISIBLE) {
                inputLinearLayout.setVisibility(View.GONE);
            }

            accountTypeText.setVisibility(View.VISIBLE);
            buttonContainer.setVisibility(View.VISIBLE);
        }
    }

    public void selectImage(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

}