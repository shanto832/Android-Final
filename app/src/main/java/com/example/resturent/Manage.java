//
//package com.example.resturent;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class Manage extends AppCompatActivity {
//
//    private Button btnAddFood, btnSeeCurrentOrders, btnTransactionHistory, btnCustomerDetails, btnWaiterDetails, btnTakeOrder;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage);
//
//        // Initialize buttons
//        btnAddFood = findViewById(R.id.btnAddFood);
//        btnSeeCurrentOrders = findViewById(R.id.btnSeeCurrentOrders);
//        btnTransactionHistory = findViewById(R.id.btnTransactionHistory);
//        btnCustomerDetails = findViewById(R.id.btnCustomerDetails);
//        btnWaiterDetails = findViewById(R.id.btnWaiterDetails);
//        btnTakeOrder = findViewById(R.id.btnTakeOrder);
//
//        // Set click listener for "Take Order" button
//        btnTakeOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Manage.this, WaiterCredentialActivity.class);
//                startActivity(intent);
//            }
//        });
//        btnAddFood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Manage.this, AddFoodActivity.class);
//                startActivity(intent);
//            }
//        });
//        btnSeeCurrentOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Manage.this, RunningOrdersActivity.class);
//                startActivity(intent);
//            }
//        });
//        btnCustomerDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Manage.this, CookViewActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        // Other button click listeners (you can add the rest as needed)
//        // ...
//    }
//}
//

//package com.example.resturent;
//
//import android.animation.ObjectAnimator;
//import android.animation.AnimatorSet;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.animation.OvershootInterpolator;
//import android.widget.Button;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class Manage extends AppCompatActivity {
//
//    private Button btnAddFood, btnSeeCurrentOrders, btnTransactionHistory, btnCustomerDetails, btnWaiterDetails, btnTakeOrder;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage);
//
//        // Initialize buttons
//        btnAddFood = findViewById(R.id.btnAddFood);
//        btnSeeCurrentOrders = findViewById(R.id.btnSeeCurrentOrders);
//        btnTransactionHistory = findViewById(R.id.btnTransactionHistory);
//        btnCustomerDetails = findViewById(R.id.btnCustomerDetails);
//        btnWaiterDetails = findViewById(R.id.btnWaiterDetails);
//        btnTakeOrder = findViewById(R.id.btnTakeOrder);
//
//        // Apply animation to each button
//        animateButton(btnAddFood, 0);
//        animateButton(btnSeeCurrentOrders, 100);
//        animateButton(btnTransactionHistory, 200);
//        animateButton(btnCustomerDetails, 300);
//        animateButton(btnWaiterDetails, 400);
//        animateButton(btnTakeOrder, 500);
//
//        // Set click listener for "Take Order" button
//        btnTakeOrder.setOnClickListener(v -> {
//            Intent intent = new Intent(Manage.this, WaiterCredentialActivity.class);
//            startActivity(intent);
//        });
//
//        btnAddFood.setOnClickListener(v -> {
//            Intent intent = new Intent(Manage.this, AddFoodActivity.class);
//            startActivity(intent);
//        });
//
//        btnSeeCurrentOrders.setOnClickListener(v -> {
//            Intent intent = new Intent(Manage.this, RunningOrdersActivity.class);
//            startActivity(intent);
//        });
//
//        btnCustomerDetails.setOnClickListener(v -> {
//            Intent intent = new Intent(Manage.this, CookViewActivity.class);
//            startActivity(intent);
//        });
//
//        // Additional button click listeners if needed
//    }
//
//    private void animateButton(View button, long delay) {
//        // Set the initial position of the button below its normal position
//        button.setTranslationY(300);
//
//        // Create an ObjectAnimator for translationY with overshoot effect
//        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "translationY", 0);
//        animator.setInterpolator(new OvershootInterpolator());
//        animator.setStartDelay(delay); // Delay to stagger animations
//        animator.setDuration(600); // Animation duration in milliseconds
//
//        // Start the animation
//        animator.start();
//    }
//}
//package com.example.resturent;
//
//import android.animation.ObjectAnimator;
//import android.animation.ValueAnimator;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.renderscript.Allocation;
//import android.renderscript.RenderScript;
//import android.renderscript.ScriptIntrinsicBlur;
//import android.view.View;
//import android.view.animation.OvershootInterpolator;
//import android.widget.Button;
//import android.widget.ImageView;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class Manage extends AppCompatActivity {
//
//    private Button btnAddFood, btnSeeCurrentOrders, btnTransactionHistory, btnCustomerDetails, btnWaiterDetails, btnTakeOrder;
//    private ImageView backgroundImage;
//    private static final float BLUR_RADIUS = 25f; // Adjust blur intensity
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage);
//
//        // Initialize buttons and background image
//        btnAddFood = findViewById(R.id.btnAddFood);
//        btnSeeCurrentOrders = findViewById(R.id.btnSeeCurrentOrders);
//        btnTransactionHistory = findViewById(R.id.btnTransactionHistory);
//        btnCustomerDetails = findViewById(R.id.btnCustomerDetails);
//        btnWaiterDetails = findViewById(R.id.btnWaiterDetails);
//        btnTakeOrder = findViewById(R.id.btnTakeOrder);
//        backgroundImage = findViewById(R.id.backgroundImage);
//
//        // Apply animations to each button
//        animateButtonFromBottom(btnAddFood, 0);
//        animateButtonFromBottom(btnSeeCurrentOrders, 100);
//        animateButtonFromBottom(btnTransactionHistory, 200);
//        animateButtonFromBottom(btnCustomerDetails, 300);
//        animateButtonFromBottom(btnWaiterDetails, 400);
//        animateButtonFromBottom(btnTakeOrder, 500);
//
//        // Apply blur animation to background
//        applyBlurEffect();
//
//        // Button click listeners
//        btnTakeOrder.setOnClickListener(v -> {
//            Intent intent = new Intent(Manage.this, WaiterCredentialActivity.class);
//            startActivity(intent);
//        });
//
//        btnAddFood.setOnClickListener(v -> {
//            Intent intent = new Intent(Manage.this, AddFoodActivity.class);
//            startActivity(intent);
//        });
//
//        btnSeeCurrentOrders.setOnClickListener(v -> {
//            Intent intent = new Intent(Manage.this, RunningOrdersActivity.class);
//            startActivity(intent);
//        });
//
//        btnCustomerDetails.setOnClickListener(v -> {
//            Intent intent = new Intent(Manage.this, CookViewActivity.class);
//            startActivity(intent);
//        });
//    }
//
//    private void animateButtonFromBottom(View button, long delay) {
//        // Set the initial position of the button at the bottom center
//        button.setTranslationY(300); // Start from below the screen
//        button.setTranslationX(0);   // Center horizontally
//
//        // Create an ObjectAnimator for translationY and translationX with overshoot effect
//        ObjectAnimator translateY = ObjectAnimator.ofFloat(button, "translationY", 0);
//        translateY.setInterpolator(new OvershootInterpolator());
//        translateY.setStartDelay(delay);
//        translateY.setDuration(1200);
//
//        // Start the animation
//        translateY.start();
//    }
//
//    private void applyBlurEffect() {
//        backgroundImage.post(() -> {
//            BitmapDrawable drawable = (BitmapDrawable) backgroundImage.getDrawable();
//            Bitmap bitmap = drawable.getBitmap();
//            Bitmap blurredBitmap = blurBitmap(bitmap, BLUR_RADIUS);
//            backgroundImage.setImageBitmap(blurredBitmap);
//        });
//    }
//
//    private Bitmap blurBitmap(Bitmap bitmap, float radius) {
//        Bitmap outputBitmap = Bitmap.createBitmap(bitmap);
//        RenderScript renderScript = RenderScript.create(this);
//        Allocation input = Allocation.createFromBitmap(renderScript, bitmap);
//        Allocation output = Allocation.createFromBitmap(renderScript, outputBitmap);
//
//        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, input.getElement());
//        script.setRadius(radius);
//        script.setInput(input);
//        script.forEach(output);
//        output.copyTo(outputBitmap);
//
//        renderScript.destroy();
//        return outputBitmap;
//    }
//}
package com.example.resturent;

import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class Manage extends AppCompatActivity {

    private Button assess,addTable, btnAddFood, btnSeeCurrentOrders, btnTransactionHistory, btnCustomerDetails, btnWaiterDetails, btnTakeOrder;
    private ImageView sharpImage, blurredImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        // Initialize buttons
        btnAddFood = findViewById(R.id.btnAddFood);
        btnSeeCurrentOrders = findViewById(R.id.btnSeeCurrentOrders);
        btnTransactionHistory = findViewById(R.id.btnTransactionHistory);
        btnCustomerDetails = findViewById(R.id.btnCustomerDetails);
        btnWaiterDetails = findViewById(R.id.btnWaiterDetails);
        btnTakeOrder = findViewById(R.id.btnTakeOrder);
        addTable = findViewById(R.id.addTable);
        assess = findViewById(R.id.assess);

        // Initialize image views for sharp and blurred images
        sharpImage = findViewById(R.id.sharpImage);
        blurredImage = findViewById(R.id.blurredImage);

        // Initially show sharp image and hide blurred image
        blurredImage.setVisibility(View.GONE);
        sharpImage.setVisibility(View.VISIBLE);

        // Set up fade and slide animations for buttons
        setupAnimations();

        // Set click listener for buttons
        setupButtonListeners();
    }

    // Animate buttons from bottom middle with staggered delays
    private void setupAnimations() {
        animateButton(btnAddFood, 0);
        animateButton(btnSeeCurrentOrders, 100);
        animateButton(btnTransactionHistory, 200);
        animateButton(btnCustomerDetails, 300);
        animateButton(btnWaiterDetails, 400);
        animateButton(btnTakeOrder, 500);
        animateButton(addTable, 600);
        animateButton(assess, 600);

    }

    // Setup button click listeners
    private void setupButtonListeners() {
        btnAddFood.setOnClickListener(v -> {
            Intent intent = new Intent(Manage.this, AddFoodActivity.class);
            startActivity(intent);
        });
        btnWaiterDetails.setOnClickListener(v -> {
            Intent intent = new Intent(Manage.this, WaiterActivity.class);
            startActivity(intent);
        });

        btnSeeCurrentOrders.setOnClickListener(v -> {
            Intent intent = new Intent(Manage.this, ZOrderDetailsActivity.class);
            startActivity(intent);
        });

        btnTransactionHistory.setOnClickListener(v -> {
            Intent intent = new Intent(Manage.this, SeeTransactionsActivity.class);
            startActivity(intent);
        });


        btnCustomerDetails.setOnClickListener(v -> {
            Intent intent = new Intent(Manage.this, CookViewActivity.class);
            startActivity(intent);
        });

        btnTakeOrder.setOnClickListener(v -> {
            Intent intent = new Intent(Manage.this, WaiterCredentialActivity.class);
            startActivity(intent);
        });
        addTable.setOnClickListener(v -> {
            Intent intent = new Intent(Manage.this, ManageTableActivity.class);
            startActivity(intent);
        });
        assess.setOnClickListener(v -> {
            Intent intent = new Intent(Manage.this, FoodStockActivity.class);
            startActivity(intent);
        });
    }

    // Animate buttons from bottom middle
    private void animateButton(View button, long delay) {
        button.setTranslationY(2000); // Start from below the screen
        button.setTranslationX(0);    // Center horizontally

        // Create the ObjectAnimator for slide up effect
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "translationY", 0);
        animator.setDuration(2000);  // Adjust duration as needed
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setStartDelay(delay); // Delay to stagger the animations

        // Start the animation
        animator.start();
    }

    // Trigger the blur effect transition between sharp and blurred images
    private void triggerBlurTransition() {
        // Fade out sharp image
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(sharpImage, "alpha", 1f, 0f);
        fadeOut.setDuration(2000); // Fade out duration

        // Fade in blurred image
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(blurredImage, "alpha", 0f, 1f);
        fadeIn.setDuration(2000); // Fade in duration

        // Make blurred image visible and sharp image invisible
        blurredImage.setVisibility(View.VISIBLE);
        sharpImage.setVisibility(View.GONE);

        // Start both animations simultaneously
        fadeOut.start();
        fadeIn.start();
    }
}
