package com.idevelopstudio.poshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.idevelopstudio.poshopping.CategoryList.CategoryListActivity;
import com.idevelopstudio.poshopping.Checkout.CheckoutActivity;
import com.idevelopstudio.poshopping.Database.AppDatabase;
import com.idevelopstudio.poshopping.Database.Product;
import com.idevelopstudio.poshopping.Extra.AppExecutors;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 200;

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button addProductButton;
    private Button myProductsButton;
    private Button checkOutButton;
    private View mainLayout;
    private AppDatabase db;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = AppDatabase.getInstance(getApplicationContext());
        addProductButton = findViewById(R.id.btn_add_product);
        myProductsButton = findViewById(R.id.btn_my_products);
        checkOutButton = findViewById(R.id.btn_checkout);
        mainLayout = findViewById(R.id.main_layout);
        lottieAnimationView = findViewById(R.id.lottie_bg);

        lottieAnimationView.playAnimation();

        addProductButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
            intent.putExtra(getString(R.string.CATEGORY_LIST_MODE), 0);
            startActivity(intent);
        });

        myProductsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CategoryListActivity.class);
            intent.putExtra(getString(R.string.CATEGORY_LIST_MODE), 1);
            startActivity(intent);
        });
        checkOutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
            startActivity(intent);
        });
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Product> products = db.productDao().getAllProducts();
                if(products != null){
                    for(Product product : products){
                        Log.d(TAG, "Id"+ product.getId());
                        Log.d(TAG, "Name"+ product.getName());
                        Log.d(TAG, "Cateogry: " + product.getCategory());
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        lottieAnimationView.pauseAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lottieAnimationView.playAnimation();
    }
}
