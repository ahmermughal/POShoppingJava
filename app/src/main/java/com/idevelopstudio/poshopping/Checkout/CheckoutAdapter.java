package com.idevelopstudio.poshopping.Checkout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idevelopstudio.poshopping.Database.Product;
import com.idevelopstudio.poshopping.Extra.Utils;
import com.idevelopstudio.poshopping.R;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.AdapterViewHolder> {

    private Context context;
    private List<Product> products;
    private AdapterDeleteListener adapterDeleteListener;

    public interface AdapterDeleteListener{
        void onAdapterDelete(Product product);
    }

    public CheckoutAdapter(Context context, AdapterDeleteListener adapterDeleteListener) {
        this.context = context;
        this.adapterDeleteListener = adapterDeleteListener;
    }

    public void setProducts(List<Product> products){
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_checkout_product, parent, false);
        return new CheckoutAdapter.AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Product product = products.get(position);
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(product.getPrice() + " PKR");
        Drawable drawable = Utils.getIconForCategory(context, product.getCategory());

        if(drawable != null) holder.iconImageView.setImageDrawable(drawable);

    }

    public void deleteItem(int position) {
        Product product = products.get(position);
        products.remove(position);
        notifyItemRemoved(position);
        adapterDeleteListener.onAdapterDelete(product);
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return products!= null ? products.size() : 0;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView iconImageView;
        TextView priceTextView;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tv_name);
            iconImageView = itemView.findViewById(R.id.iv_category_icon);
            priceTextView = itemView.findViewById(R.id.tv_price);
        }
    }
}
