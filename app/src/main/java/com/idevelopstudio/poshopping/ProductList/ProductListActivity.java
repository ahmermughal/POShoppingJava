package com.idevelopstudio.poshopping.ProductList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.idevelopstudio.poshopping.AddProductDetailsActivity;
import com.idevelopstudio.poshopping.Database.AppDatabase;
import com.idevelopstudio.poshopping.Database.Product;
import com.idevelopstudio.poshopping.Extra.AppExecutors;
import com.idevelopstudio.poshopping.R;
import com.idevelopstudio.poshopping.tools.SwipeToDeleteCallbackProductAdapter;

import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductAdapter.AdapterClickListner, ProductAdapter.AdapterDeleteListener {

    private static final String TAG = ProductListActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    AppDatabase db;
    ProductAdapter productAdapter;
    private LottieAnimationView lottieAnimationView;
    private TextView categoryTitleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));

        setContentView(R.layout.activity_product_list);

            Intent intent = getIntent();
            db = AppDatabase.getInstance(getApplicationContext());
            String category = intent.getStringExtra(getString(R.string.CATEGORY_NAME));
            recyclerView = findViewById(R.id.rv_product);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            productAdapter = new ProductAdapter(this, this, this);
            recyclerView.setAdapter(productAdapter);
            categoryTitleTextView = findViewById(R.id.tv_category_title);
            categoryTitleTextView.setText(category);
            lottieAnimationView = findViewById(R.id.lottie_bg);
            lottieAnimationView.playAnimation();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallbackProductAdapter(productAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Product> products = db.productDao().getProductsByCategory(category);
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

    @Override
    public void onAdapterClick(Product product) {


        Intent intent = new Intent(this, AddProductDetailsActivity.class);
        intent.putExtra(getString(R.string.PRODUCT_ID), product.getId());
        startActivity(intent);
    }

    @Override
    public void onAdapterDelete(Product product) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.productDao().deleteProduct(product);
            }
        });
    }
}
