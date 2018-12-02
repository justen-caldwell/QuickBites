package quickbites.umflint.com.quickbites.Utilities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import quickbites.umflint.com.quickbites.R;

@SuppressWarnings("unchecked")
public class RatingListAdapterUser extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int HEADER = 0, ITEM = 1;
    public List<HashMap<String, String>> rating_list;

    public RatingListAdapterUser(List<HashMap<String, String>> rating_data) {
        rating_list = rating_data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View vh = inflater.inflate(R.layout.rating_card, parent, false);
        viewHolder = new ViewHolder(vh);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HashMap<String, String> rating = rating_list.get(position);
        ViewHolder vh = (ViewHolder) holder;
        vh.username.setText(rating.get("restaurantName") + ": " + rating.get("menuItemName"));
        vh.ratingBar.setRating(Float.parseFloat(rating.get("ratingNumber")));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return rating_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.rating_userName);
            ratingBar = view.findViewById(R.id.user_ratingValue);
        }
    }
}
