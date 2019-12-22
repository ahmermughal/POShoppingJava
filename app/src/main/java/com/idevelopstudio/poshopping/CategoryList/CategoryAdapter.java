package com.idevelopstudio.poshopping.CategoryList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idevelopstudio.poshopping.R;

import org.w3c.dom.Text;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.AdapterViewHolder> {

    private static final String TAG = CategoryAdapter.class.getSimpleName();

    Context context;

    List<CategoryModel> categories;

    public interface AdapterItemClickListener{
        void onItemClick(String categoryName);
    }

    private AdapterItemClickListener adapterItemClickListener;

    public CategoryAdapter(Context context, List<CategoryModel> categories, AdapterItemClickListener adapterItemClickListener) {
        this.context = context;
        this.categories = categories;
        this.adapterItemClickListener = adapterItemClickListener;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_category, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {

        if(position == 0){
            holder.arrowImageView.setVisibility(View.INVISIBLE);
            holder.divider.setVisibility(View.INVISIBLE);
        }else{
            CategoryModel category = categories.get(position-1);
            holder.categoryName.setText(category.getCategoryName());
            holder.categoryIcon.setImageDrawable(category.getCategoryIcon());
        }

    }

    @Override
    public int getItemCount() {
        return categories!= null ? categories.size()+1 : 0;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName;
        private ImageView categoryIcon;
        private ImageView arrowImageView;
        private View divider;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            arrowImageView = itemView.findViewById(R.id.iv_arrow);
            categoryName = itemView.findViewById(R.id.tv_category_name);
            categoryIcon = itemView.findViewById(R.id.iv_category_icon);
            divider = itemView.findViewById(R.id.divider);
                itemView.setOnClickListener(v -> {
                    if(getAdapterPosition() > 0) {
                        adapterItemClickListener.onItemClick(categories.get(getAdapterPosition() - 1).getCategoryName());
                    }
                });
        }


    }
}
