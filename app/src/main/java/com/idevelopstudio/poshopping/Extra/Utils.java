package com.idevelopstudio.poshopping.Extra;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.idevelopstudio.poshopping.CategoryList.CategoryModel;
import com.idevelopstudio.poshopping.R;

import java.util.ArrayList;
import java.util.List;

public class Utils {




    public static List<CategoryModel> getCategories(Context context){
        List<CategoryModel> categories = new ArrayList<>();
        categories.add(new CategoryModel("Beverages", context.getDrawable(R.drawable.sparkling)));
        categories.add(new CategoryModel("Bread/Bakery", context.getDrawable(R.drawable.bread1)));
        categories.add(new CategoryModel("Snacks", context.getDrawable(R.drawable.snack)));
        categories.add(new CategoryModel("Dairy", context.getDrawable(R.drawable.milk)));
        categories.add(new CategoryModel("Dry/Baking Goods", context.getDrawable(R.drawable.flour)));
        categories.add(new CategoryModel("Frozen Foods", context.getDrawable(R.drawable.icecream)));
        categories.add(new CategoryModel("Meat", context.getDrawable(R.drawable.meat)));
        categories.add(new CategoryModel("Vegetables", context.getDrawable(R.drawable.tomato)));
        categories.add(new CategoryModel("Fruits", context.getDrawable(R.drawable.apple)));
        categories.add(new CategoryModel("Canned Goods", context.getDrawable(R.drawable.cannedfood)));
        categories.add(new CategoryModel("Candy", context.getDrawable(R.drawable.candy)));
        categories.add(new CategoryModel("Fish", context.getDrawable(R.drawable.fish)));
        categories.add(new CategoryModel("Seasoning", context.getDrawable(R.drawable.seasoning)));
        return categories;
    }



    public static Drawable getIconForCategory(Context context, String title){

        Drawable drawable;

        switch (title){
            case "Beverages":
                drawable = context.getResources().getDrawable(R.drawable.sparkling);
                return drawable;
            case "Bread/Bakery":
                drawable = context.getResources().getDrawable(R.drawable.bread1);
                return drawable;
            case "Snacks":
                drawable = context.getResources().getDrawable(R.drawable.snack);
                return drawable;
            case "Dairy":
                drawable = context.getResources().getDrawable(R.drawable.milk);
                return drawable;
            case "Dry/Baking Goods":
                drawable = context.getResources().getDrawable(R.drawable.flour);
                return drawable;
            case "Frozen Foods":
                drawable = context.getResources().getDrawable(R.drawable.icecream);
                return drawable;
            case "Meat":
                drawable = context.getResources().getDrawable(R.drawable.meat);
                return drawable;
            case "Vegetables":
                drawable = context.getResources().getDrawable(R.drawable.tomato);
                return drawable;
            case "Fruits":
                drawable = context.getResources().getDrawable(R.drawable.apple);
                return drawable;
            case "Canned Goods":
                drawable = context.getResources().getDrawable(R.drawable.cannedfood);
                return drawable;
            case "Candy":
                drawable = context.getResources().getDrawable(R.drawable.candy);
                return drawable;
            case "Fish":
                drawable = context.getResources().getDrawable(R.drawable.fish);
                return drawable;
            case "Seasoning":
                drawable = context.getResources().getDrawable(R.drawable.seasoning);
                return drawable;
            default:
                drawable = null;
                return drawable;
        }

    }


}
