package quickbites.umflint.com.quickbites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import quickbites.umflint.com.quickbites.Utilities.DatabaseAccessor;

public class UserSignIn extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseAccessor databaseAccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);
        setTitle("Sign In");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth != null) {
            auth.signOut();
        }

        btnSignIn = findViewById(R.id.sign_in_button);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        btnResetPassword = findViewById(R.id.btn_reset_password);


        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(UserSignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(UserSignIn.this, "loginUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(UserSignIn.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            FirebaseUser user = auth.getCurrentUser();
                            final String userID = user.getUid();
                            databaseAccessor = DatabaseAccessor.getInstance();
                            Query check_signup_status = databaseAccessor.getDatabaseReference().child("users");

                            databaseAccessor.access(false, check_signup_status, new DatabaseAccessor.OnGetDataListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    HashMap<String, Object> current_user;

                                    for (DataSnapshot this_user : dataSnapshot.getChildren()){
                                        String tempKey = this_user.getKey();
                                        current_user = (HashMap<String, Object>)this_user.getValue();
                                        if(tempKey.equals("customers")) {
                                            Iterator it = current_user.entrySet().iterator();
                                            while (it.hasNext()){
                                                Map.Entry current_entry = (Map.Entry)it.next();
                                                if(current_entry.getKey().equals(userID)){
                                                    startActivity(new Intent(UserSignIn.this, MainActivity.class));
                                                    finish();
                                                }
                                                it.remove();
                                            }
                                        }
                                        if(tempKey.equals("restaurants")) {
                                            Iterator it = current_user.entrySet().iterator();
                                            while (it.hasNext()){
                                                Map.Entry current_entry = (Map.Entry)it.next();
                                                if(current_entry.getKey().equals(userID)){
                                                    Intent intent = new Intent(getBaseContext(), ViewMenu.class);
                                                    intent.putExtra("ITEM_OWNER", userID);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                it.remove();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                                startActivity(new Intent(UserSignIn.this, UserRegister.class));
                                finish();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(UserSignIn.this, SignupActivity.class));
        finish();
    }
}