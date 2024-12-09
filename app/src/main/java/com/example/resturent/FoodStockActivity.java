package com.example.resturent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodStockActivity extends AppCompatActivity {

    private EditText quantityInput;
    private ListView listView;
    private Button filterButton, addFoodButton, homeButton;

    private FirebaseFirestore db;
    private List<HashMap<String, String>> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_stock);

        quantityInput = findViewById(R.id.quantityInput);
        listView = findViewById(R.id.listView);
        filterButton = findViewById(R.id.filterButton);
        addFoodButton = findViewById(R.id.addFoodButton);
        homeButton = findViewById(R.id.homeButton);

        db = FirebaseFirestore.getInstance();
        foodList = new ArrayList<>();

        // Button Actions
        filterButton.setOnClickListener(v -> {
            loadFoodItems();
        });

        addFoodButton.setOnClickListener(v -> {
            // Add food button functionality (not implemented yet)
            Intent intent = new Intent(FoodStockActivity.this, UpdateFoodActivity.class);
            startActivity(intent);
        });

        homeButton.setOnClickListener(v -> {
            // Navigate to home activity
            Intent intent = new Intent(FoodStockActivity.this, Manage.class);
            startActivity(intent);
        });
    }

    private void loadFoodItems() {
        String input = quantityInput.getText().toString().trim();

        // Validate input
        if (input.isEmpty()) {
            Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        int inputQuantity = Integer.parseInt(input);

        // Query Firestore collection
        db.collection("FoodItems")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            foodList.clear();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                String foodName = document.getString("foodName");
                                String foodQuantityStr = document.getString("foodQuantity");

                                if (foodQuantityStr != null) {
                                    int foodQuantity = Integer.parseInt(foodQuantityStr);
                                    if (foodQuantity < inputQuantity) {
                                        // Add the food item to the list
                                        HashMap<String, String> food = new HashMap<>();
                                        food.put("foodName", foodName);
                                        foodList.add(food);
                                    }
                                }
                            }

                            // Use a SimpleAdapter to populate the ListView with only food names
                            SimpleAdapter adapter = new SimpleAdapter(
                                    FoodStockActivity.this,
                                    foodList,
                                    android.R.layout.simple_list_item_1, // Only food names
                                    new String[]{"foodName"}, // Only map the food name
                                    new int[]{android.R.id.text1}
                            );
                            listView.setAdapter(adapter);
                        }
                    } else {
                        Toast.makeText(FoodStockActivity.this, "Failed to load food items", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
