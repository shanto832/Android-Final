package com.example.resturent;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodItemDetailActivity extends AppCompatActivity {

    private TextView orderDetailsTextView, timerTextView;
    private Button confirmOrderButton;
    private int selectedTableNumber;
    private ArrayList<String> foodNames;
    private ArrayList<Integer> quantities;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 600000; // 10 minutes in milliseconds

    // Flag to stop the timer if the order is marked ready
    private boolean orderReady = false;
    private DocumentReference orderReference;

    // New variables to store customer information
    private String userName;
    private String userPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_detail);

        orderDetailsTextView = findViewById(R.id.orderDetailsTextView);
        confirmOrderButton = findViewById(R.id.confirmOrderButton);
        timerTextView = findViewById(R.id.timerTextView);

        // Get data from the previous activity
        selectedTableNumber = getIntent().getIntExtra("selectedTableNumber", -1);
        foodNames = getIntent().getStringArrayListExtra("foodNames");
        quantities = getIntent().getIntegerArrayListExtra("quantities");

        // Get customer details from the Intent
        userName = getIntent().getStringExtra("userName");
        userPhoneNumber = getIntent().getStringExtra("userPhoneNumber");

        // Display the order details (table number, food items, and quantities)
        StringBuilder orderDetails = new StringBuilder();
        orderDetails.append("Table Number: ").append(selectedTableNumber).append("\n");
        for (int i = 0; i < foodNames.size(); i++) {
            orderDetails.append(foodNames.get(i)).append(" - Quantity: ").append(quantities.get(i)).append("\n");
        }
        orderDetailsTextView.setText(orderDetails.toString());

        confirmOrderButton.setOnClickListener(v -> {
            // Disable the button to prevent multiple clicks
            confirmOrderButton.setEnabled(false);
            confirmOrder();
        });
    }

    // Method to confirm order and save it to Firestore
    private void confirmOrder() {
        if (selectedTableNumber != -1 && foodNames != null && !foodNames.isEmpty() && quantities != null && !quantities.isEmpty()) {
            // Batch to perform multiple updates atomically
            WriteBatch batch = db.batch();

            for (int i = 0; i < foodNames.size(); i++) {
                String foodName = foodNames.get(i);
                int quantityOrdered = quantities.get(i);

                db.collection("FoodItems")
                        .whereEqualTo("foodName", foodName)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String foodId = document.getId();
                                String currentQuantityStr = document.getString("foodQuantity");

                                if (currentQuantityStr != null) {
                                    int currentQuantity = Integer.parseInt(currentQuantityStr);

                                    if (currentQuantity >= quantityOrdered) {
                                        // Calculate the new quantity
                                        int updatedQuantity = currentQuantity - quantityOrdered;

                                        // Update the food item's quantity
                                        DocumentReference foodRef = db.collection("FoodItems").document(foodId);
                                        batch.update(foodRef, "foodQuantity", String.valueOf(updatedQuantity));

                                        // Save order details to "OrderWithTable" collection
                                        Map<String, Object> orderData = new HashMap<>();
                                        orderData.put("tableNumber", selectedTableNumber);
                                        orderData.put("foodName", foodName);
                                        orderData.put("quantity", quantityOrdered);
                                        orderData.put("userName", userName); // Store customer name
                                        orderData.put("userPhoneNumber", userPhoneNumber); // Store customer phone number
                                        orderData.put("status", "in-progress"); // Order initially in-progress
                                        orderData.put("isViewed", false); // New field added

                                        // Add order data
                                        orderReference = db.collection("OrderWithTable").document();
                                        batch.set(orderReference, orderData);
                                    } else {
                                        Toast.makeText(this, foodName + ": Not enough stock. Available: " + currentQuantity, Toast.LENGTH_LONG).show();
                                        confirmOrderButton.setEnabled(true);
                                        return;
                                    }
                                }
                            }

                            // Commit the batch update
                            batch.commit().addOnSuccessListener(aVoid -> {
                                Toast.makeText(FoodItemDetailActivity.this, "Order confirmed!", Toast.LENGTH_SHORT).show();
                                startTimer(); // Start countdown timer after order confirmation
                            }).addOnFailureListener(e -> {
                                Toast.makeText(FoodItemDetailActivity.this, "Failed to confirm order. Try again.", Toast.LENGTH_SHORT).show();
                                confirmOrderButton.setEnabled(true);
                            });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Error checking stock for " + foodName, Toast.LENGTH_SHORT).show();
                            confirmOrderButton.setEnabled(true);
                        });
            }
        } else {
            Toast.makeText(this, "Please check the order details.", Toast.LENGTH_SHORT).show();
            confirmOrderButton.setEnabled(true);
        }
    }

    // Method to start the countdown timer
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (orderReady) {
                    cancel(); // Stop timer if the order is marked as ready
                    timerTextView.setText("00:00");
                    Toast.makeText(FoodItemDetailActivity.this, "Your order is ready!", Toast.LENGTH_SHORT).show();
                    return;
                }
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                if (!orderReady) {
                    timerTextView.setText("00:00"); // Timer reaches 00:00
                }
            }
        }.start();

        // Listen for changes to order status in Firestore
        if (orderReference != null) {
            orderReference.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    return; // Return if there's an error
                }
                if (snapshot != null && snapshot.exists()) {
                    String status = snapshot.getString("status");
                    if ("ready".equals(status)) {
                        orderReady = true; // Mark order as ready to stop timer
                    }
                }
            });
        }
    }

    // Method to update the timer display
    private void updateTimerDisplay() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Stop the timer when the activity is destroyed
        }
    }
}
