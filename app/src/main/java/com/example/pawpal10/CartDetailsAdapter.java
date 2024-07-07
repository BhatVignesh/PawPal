package com.example.pawpal10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartDetailsAdapter extends RecyclerView.Adapter<CartDetailsAdapter.ViewHolder> {

    private Context context;
    private List<CartDetailsModel> cartDetailsModelList;

    public CartDetailsAdapter(Context context, List<CartDetailsModel> cartModelList) {
        this.context = context;
        this.cartDetailsModelList = cartModelList;
    }

    @NonNull
    @Override
    public CartDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartDetailsAdapter.ViewHolder holder, int position) {
        CartDetailsModel model = cartDetailsModelList.get(position);
        holder.name.setText(model.getProduct_name());
        holder.price.setText(String.valueOf(model.getProduct_price()));
        holder.quantity.setText(String.valueOf(model.getQuantity()));
    }

    @Override
    public int getItemCount() {
        if (cartDetailsModelList != null) {
            return cartDetailsModelList.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name_value);
            price = itemView.findViewById(R.id.product_price_value);
            quantity = itemView.findViewById(R.id.product_quantity_value);
        }
    }
}

