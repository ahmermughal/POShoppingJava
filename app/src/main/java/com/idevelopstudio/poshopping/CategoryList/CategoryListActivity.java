package com.idevelopstudio.poshopping.CategoryList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.idevelopstudio.poshopping.AddProductDetailsActivity;
import com.idevelopstudio.poshopping.Extra.Utils;
import com.idevelopstudio.poshopping.ProductList.ProductListActivity;
import com.idevelopstudio.poshopping.R;

public class CategoryListActivity extends AppCompatActivity implements CategoryAdapter.AdapterItemClickListener {

    private RecyclerView recyclerView;
    private int mode;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));

        // get the mode from last activity
        Intent intent = getIntent();
        mode = intent.getIntExtra(getString(R.string.CATEGORY_LIST_MODE), -1);

        setContentView(R.layout.activity_category_list);
        recyclerView = findViewById(R.id.rv_categories);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, Utils.getCategories(this),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(categoryAdapter);
        lottieAnimationView = findViewById(R.id.lottie_bg);
        lottieAnimationView.playAnimation();
    }

    @Override
    public void onItemClick(String categoryName) {
        if(mode == -1){
            return;
        }

        if(mode == 0){// go to add product activity
            Intent intent = new Intent(this, AddProductDetailsActivity.class);
            intent.putExtra(getString(R.string.CATEGORY_NAME), categoryName);
            startActivity(intent);
        }else if(mode == 1){// go to show list of products
            Intent intent = new Intent(this, ProductListActivity.class);
            intent.putExtra(getString(R.string.CATEGORY_NAME), categoryName);
            startActivity(intent);
        }
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