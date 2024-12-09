package com.example.resturent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.firestore.Query;

public class PendingActivity extends AppCompatActivity {

    private ListView listView;
    private String waiterId;
    private String waiterName;
    private FirebaseFirestore db;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve waiter ID and Name from Intent
        Intent intent = getIntent();
        waiterId = intent.getStringExtra("waiterId");
        waiterName = intent.getStringExtra("waiterName");

        // Initialize ListView and Adapter
        listView = findViewById(R.id.listViewPendingOrders);
        orderAdapter = new OrderAdapter(this, R.layout.list_item_order);
        listView.setAdapter(orderAdapter);

        // Fetch and display orders from Firestore where "isViewed" is false
        db.collection("OrderWithTable")
                .whereEqualTo("isViewed", false)  // Only fetch orders that are not viewed
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable com.google.firebase.firestore.FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(PendingActivity.this, "Error loading orders", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (value != null) {
                            orderAdapter.clear(); // Clear previous orders before adding new ones
                            // Add the orders from Firestore to the adapter
                            for (DocumentSnapshot document : value) {
                                OrderWithTable order = document.toObject(OrderWithTable.class);
                                if (order != null) {
                                    order.setId(document.getId()); // Manually set the ID from Firestore
                                    orderAdapter.add(order);
                                }
                            }
                        }
                    }
                });

        // Set item click listener to navigate to TableWaiterActivity and handle transaction finalization
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderWithTable selectedOrder = orderAdapter.getItem(position);

                if (selectedOrder != null) {
                    // Use the correct method name to access customer phone number and user name
                    String userPhoneNumber = selectedOrder.getCustomerPhoneNumber();
                    String userName = selectedOrder.getUserName();

                    // Pass selected order details to TableWaiterActivity
                    Intent intent = new Intent(PendingActivity.this, TableWaiterActivity.class);
                    intent.putExtra("waiterId", waiterId);
                    intent.putExtra("waiterName", waiterName);
                    intent.putExtra("foodName", selectedOrder.getFoodName());
                    intent.putExtra("tableNumber", selectedOrder.getTableNumber());
                    intent.putExtra("quantity", selectedOrder.getQuantity());
                    intent.putExtra("customerName", userName);
                    intent.putExtra("customerPhoneNumber", userPhoneNumber);
                    startActivity(intent);

                    // Fetch the price of the food from FoodItems collection and create transaction
                    fetchFoodPriceAndCreateTransaction(selectedOrder);

                    // Mark the order as viewed
                    markOrderAsViewed(selectedOrder);
                }
            }
        });
    }

    private void fetchFoodPriceAndCreateTransaction(OrderWithTable selectedOrder) {
        // Retrieve food name and quantity from the selected order
        String foodName = selectedOrder.getFoodName();
        int quantity = selectedOrder.getQuantity();
        String customerName = selectedOrder.getUserName();
        String customerPhoneNumber = selectedOrder.getCustomerPhoneNumber();

        // Fetch unit price from FoodItems collection
        db.collection("FoodItems")
                .whereEqualTo("foodName", foodName)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Assume that the food item collection contains a single document for each foodName
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                        // Retrieve the food price from the document
                        Object priceObject = documentSnapshot.get("foodPrice");

                        if (priceObject instanceof String) {
                            // If the price is a String, try to parse it as a double
                            try {
                                double unitPrice = Double.parseDouble((String) priceObject);
                                // Calculate the total price
                                double totalPrice = unitPrice * quantity;

                                // Create a new TransactionFinalDetails object and add it to Firestore
                                createTransactionFinalDetails(selectedOrder, unitPrice, totalPrice);
                            } catch (NumberFormatException e) {
                                // Handle invalid food price format
                                Toast.makeText(PendingActivity.this, "Invalid food price for " + foodName, Toast.LENGTH_SHORT).show();
                            }
                        } else if (priceObject instanceof Number) {
                            // If the price is already a number, use it directly
                            double unitPrice = ((Number) priceObject).doubleValue();
                            // Calculate the total price
                            double totalPrice = unitPrice * quantity;

                            // Create a new TransactionFinalDetails object and add it to Firestore
                            createTransactionFinalDetails(selectedOrder, unitPrice, totalPrice);
                        } else {
                            // Handle case where food price is invalid or missing
                            Toast.makeText(PendingActivity.this, "Invalid or missing food price for " + foodName, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PendingActivity.this, "Food item not found in FoodItems collection", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PendingActivity.this, "Error fetching food price", Toast.LENGTH_SHORT).show();
                });
    }

    private void createTransactionFinalDetails(OrderWithTable selectedOrder, double unitPrice, double totalPrice) {
        // Create a new TransactionFinalDetails object with the necessary fields
        TransactionFinalDetails transactionDetails = new TransactionFinalDetails(
                selectedOrder.getFoodName(), unitPrice, selectedOrder.getQuantity(),
                totalPrice, selectedOrder.getUserName(), selectedOrder.getCustomerPhoneNumber());

        // Add the new transaction details to the TransactionFinalDetails collection in Firestore
        db.collection("TransactionFinalDetails")
                .add(transactionDetails)
                .addOnSuccessListener(documentReference -> {
                    // Show a Toast on successful entry
                    Toast.makeText(PendingActivity.this, "Transaction finalized successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Show a Toast on error
                    Toast.makeText(PendingActivity.this, "Error finalizing transaction", Toast.LENGTH_SHORT).show();
                });
    }

    private void markOrderAsViewed(OrderWithTable selectedOrder) {
        // Ensure the order has a valid ID
        if (selectedOrder != null && selectedOrder.getId() != null) {
            // Update the order document to mark it as viewed
            WriteBatch batch = db.batch();
            batch.update(db.collection("OrderWithTable").document(selectedOrder.getId()), "isViewed", true);

            batch.commit().addOnSuccessListener(aVoid -> {
                Toast.makeText(PendingActivity.this, "Order marked as viewed", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(PendingActivity.this, "Error marking order as viewed", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Handle the case when the order ID is null
            Toast.makeText(PendingActivity.this, "Invalid order ID", Toast.LENGTH_SHORT).show();
        }
    }
}
