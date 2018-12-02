package quickbites.umflint.com.quickbites.Utilities;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.List;

import quickbites.umflint.com.quickbites.R;

@SuppressWarnings("unchecked")
public class MenuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int HEADER = 0, ITEM = 1;
    public List<HashMap<String, String>> food_list;

    public MenuListAdapter(List<HashMap<String, String>> menu_data) {
        food_list = menu_data;
    }

    @Override
    public int getItemViewType(int position){
        HashMap<String, String> temp_item = food_list.get(position);
        if(temp_item.size() == 1){
            return HEADER;
        }
        else{
            return ITEM;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType){
            case HEADER:
                View v_header = inflater.inflate(R.layout.menu_card_header, parent, false);
                viewHolder = new ViewHolder_Header(v_header);
                break;
            case ITEM:
                View v_menuItem = inflater.inflate(R.layout.menu_card, parent, false);
                viewHolder = new ViewHolder_MenuItem(v_menuItem);
                break;
            default:
                View v_default = inflater.inflate(R.layout.menu_card, parent, false);
                viewHolder = new ViewHolder_MenuItem(v_default);
                break;
        }
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HashMap<String, String> menu_item = food_list.get(position);

        switch(holder.getItemViewType()){
            case HEADER:
                final ViewHolder_Header v_header = (ViewHolder_Header) holder;
                v_header.header_name.setText(menu_item.get("item_header"));

                break;
            case ITEM:
                final ViewHolder_MenuItem v_item =  (ViewHolder_MenuItem) holder;
                v_item.menu_price.setText(menu_item.get("item_price"));
                v_item.menu_name.setText(menu_item.get("item_name"));

                final DatabaseAccessor databaseAccessor = DatabaseAccessor.getInstance();
                Query rating_query = databaseAccessor.getDatabaseReference().child("ratings_by_item").child(menu_item.get("item_owner")).child(menu_item.get("item_name"));
                databaseAccessor.access(true, rating_query, new DatabaseAccessor.OnGetDataListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()) {
                            double total_rating = 0.0;
                            int num_of_ratings = (int) dataSnapshot.getChildrenCount();
                            for (DataSnapshot rating : dataSnapshot.getChildren()) {
                                total_rating += Double.parseDouble(rating.child("ratingNumber").getValue().toString());
                            }
                            total_rating /= num_of_ratings;
                            v_item.menu_rating.setRating((float) total_rating);
                            v_item.menu_rating.setVisibility(View.VISIBLE);
                        }
                        else{
                            v_item.menu_rating.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return food_list.size();
    }

    public static class ViewHolder_Header extends RecyclerView.ViewHolder {
        public TextView header_name;

        public ViewHolder_Header(View view) {
            super(view);
            header_name = view.findViewById(R.id.menu_item_name);
        }
    }

    public static class ViewHolder_MenuItem extends RecyclerView.ViewHolder {
        public TextView menu_name;
        public RatingBar menu_rating;
        public TextView menu_price;

        public ViewHolder_MenuItem(View view) {
            super(view);
            menu_name = view.findViewById(R.id.menu_item_name);
            menu_rating = view.findViewById(R.id.menu_item_rating);
            menu_price = view.findViewById(R.id.menu_price);

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(view.getContext(), "Clicked: " + menu_name.getText(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }

}
