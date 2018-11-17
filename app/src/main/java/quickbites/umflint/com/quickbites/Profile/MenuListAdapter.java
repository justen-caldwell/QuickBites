package quickbites.umflint.com.quickbites.Profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import quickbites.umflint.com.quickbites.R;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder> {

    public List<HashMap<String, String>> food_list;

    public MenuListAdapter(List<HashMap<String, String>> menu_data) {
        food_list = menu_data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MenuListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        HashMap<String, String> menu_item = food_list.get(position);

        String testTest = menu_item.get("Name");

        holder.menu_name.setText(menu_item.get("Name"));
        holder.menu_rating.setRating(Float.parseFloat(menu_item.get("Rating")));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return food_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView menu_image;
        public TextView menu_name;
        public RatingBar menu_rating;

        public ViewHolder(View view) {
            super(view);
            menu_name = view.findViewById(R.id.menu_item_name);
            menu_image = view.findViewById(R.id.menu_item_picture);
            menu_rating = view.findViewById(R.id.menu_item_rating);
        }
    }

}
