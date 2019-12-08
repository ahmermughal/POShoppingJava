package com.idevelopstudio.poshopping.ProductList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.idevelopstudio.poshopping.Database.AppDatabase;
import com.idevelopstudio.poshopping.Database.Product;
import com.idevelopstudio.poshopping.Extra.AppExecutors;
import com.idevelopstudio.poshopping.R;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private static final String TAG = ProductListActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    AppDatabase db;
    ProductAdapter productAdapter;
    private LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

            Intent intent = getIntent();
            db = AppDatabase.getInstance(getApplicationContext());
            String category = intent.getStringExtra(getString(R.string.CATEGORY_NAME));
            recyclerView = findViewById(R.id.rv_product);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            productAdapter = new ProductAdapter(this);
            recyclerView.setAdapter(productAdapter);
            lottieAnimationView = findViewById(R.id.lottie_bg);
            lottieAnimationView.playAnimation();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Product> products = db.productDao().getAllProducts();
                if(products != null){
                    for(Product product : products){
                        Log.d(TAG, "Id"+ product.getId());
                        Log.d(TAG, "Name"+ product.getName());
                    }
                }else{
                    Log.d(TAG, "run: EMPTY");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        productAdapter.setProducts(products);
                    }
                });
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
