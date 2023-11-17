package com.example.flavourhive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> implements Filterable {

    private List<Restaurant> restaurantList;
    private List<Restaurant> restaurantListFull; // For filtering

    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        restaurantListFull = new ArrayList<>(restaurantList); // Initialize the full list
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant currentRestaurant = restaurantList.get(position);
        holder.restaurantName.setText(currentRestaurant.getName());
        holder.restaurantType.setText(currentRestaurant.getType());
        holder.restaurantRating.setRating(currentRestaurant.getRating());
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    @Override
    public Filter getFilter() {
        return restaurantFilter;
    }

    private Filter restaurantFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Restaurant> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(restaurantListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Restaurant item : restaurantListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            restaurantList.clear();
            restaurantList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantName;
        TextView restaurantType;
        RatingBar restaurantRating;

        public ViewHolder(View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.tvRestaurantName);
            restaurantType = itemView.findViewById(R.id.tvRestaurantType);
            restaurantRating = itemView.findViewById(R.id.ratingBar);
        }
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        restaurantList.clear();
        restaurantList.addAll(restaurants);
        restaurantListFull.clear();
        restaurantListFull.addAll(restaurants);
        notifyDataSetChanged();
    }
}

