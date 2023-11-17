package com.example.flavourhive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RestaurantAdapter restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addBtn = findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageRestaurantsActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantAdapter = new RestaurantAdapter(new ArrayList<>());
        recyclerView.setAdapter(restaurantAdapter);

        getRestaurants();

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                restaurantAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                restaurantAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void getRestaurants() {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        RestaurantDao restaurantDao = db.restaurantDao();
        restaurantDao.getAllRestaurants().observe(this, new Observer<List<RestaurantEntity>>() {
            @Override
            public void onChanged(List<RestaurantEntity> restaurantEntities) {
                List<Restaurant> restaurants = convertToRestaurantList(restaurantEntities);
                restaurantAdapter.setRestaurants(restaurants);
            }
        });
    }

    private List<Restaurant> convertToRestaurantList(List<RestaurantEntity> restaurantEntities) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (RestaurantEntity entity : restaurantEntities) {
            restaurants.add(new Restaurant(entity.getName(), entity.getType(), Float.parseFloat(entity.getRating())));
        }
        return restaurants;
    }
}
