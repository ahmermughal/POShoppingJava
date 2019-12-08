package com.idevelopstudio.poshopping.ProductList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idevelopstudio.poshopping.CategoryList.CategoryAdapter;
import com.idevelopstudio.poshopping.Database.Product;
import com.idevelopstudio.poshopping.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.AdapterViewHolder> {

    private Context context;
    private List<Product> products;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public void setProducts(List<Product> products){
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_product, parent, false);
        return new ProductAdapter.AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Product product = products.get(position);
        holder.nameTextView.setText(product.getName());
        holder.stockTextView.setText(product.getStock() + " Left");
        holder.priceTextView.setText(product.getPrice() + " PKR");
    }

    @Override
    public int getItemCount() {
        return products!= null ? products.size() : 0;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView stockTextView;
        TextView priceTextView;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tv_name);
            stockTextView = itemView.findViewById(R.id.tv_stock);
            priceTextView = itemView.findViewById(R.id.tv_price);
        }
    }
}
