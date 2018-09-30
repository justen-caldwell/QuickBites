package quickbites.umflint.com.quickbites.Utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {

    private static final Firebase ourInstance = new Firebase();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String userToken;

    private Firebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    public static Firebase getInstance() {
        return ourInstance;
    }
}
