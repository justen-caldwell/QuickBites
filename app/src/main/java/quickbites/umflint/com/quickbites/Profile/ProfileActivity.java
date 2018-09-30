package quickbites.umflint.com.quickbites.Profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import quickbites.umflint.com.quickbites.R;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.ProfilePicture)
    CircularImageView profilePicture;
    @BindView(R.id.ProfileName)
    TextView profileName;
    @BindView(R.id.NumOfRatings)
    TextView numOfRatings;
    @BindView(R.id.RatingsNumber)
    TextView ratingsNumber;
    @BindView(R.id.FavoritesTitle)
    TextView favoritesTitle;
    @BindView(R.id.FavoritesRecyclerView)
    RecyclerView favoritesRecycler;
    @BindView(R.id.RatingsTitle)
    TextView ratingsTitle;
    @BindView(R.id.RatingsRecyclerView)
    RecyclerView ratingsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
    }
}
