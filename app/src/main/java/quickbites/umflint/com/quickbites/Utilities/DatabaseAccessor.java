package quickbites.umflint.com.quickbites.Utilities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DatabaseAccessor {
    private static final DatabaseAccessor main_instance = new DatabaseAccessor();
    private DatabaseReference databaseReference;

    private DatabaseAccessor() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    public static DatabaseAccessor getInstance() {
        return main_instance;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void access(final boolean continuousUpdating, final Query locationQuery,
                       final DatabaseAccessor.OnGetDataListener listener) {
        locationQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (!continuousUpdating) {
                    locationQuery.removeEventListener(this);
                }
                listener.onSuccessfulAdd(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (!continuousUpdating) {
                    locationQuery.removeEventListener(this);
                }
                listener.onSuccessfulChange(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (!continuousUpdating) {
                    locationQuery.removeEventListener(this);
                }
                listener.onSuccessfulRemoval(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (!continuousUpdating) {
                    locationQuery.removeEventListener(this);
                }
                listener.onSuccessfulMove(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (!continuousUpdating) {
                    locationQuery.removeEventListener(this);
                }
                listener.onFailure(databaseError);
            }
        });
    }

    public interface OnGetDataListener {
        void onSuccessfulAdd(DataSnapshot dataSnapshot);

        void onSuccessfulChange(DataSnapshot dataSnapshot);

        void onSuccessfulRemoval(DataSnapshot dataSnapshot);

        void onSuccessfulMove(DataSnapshot dataSnapshot);

        void onFailure(DatabaseError databaseError);
    }
}