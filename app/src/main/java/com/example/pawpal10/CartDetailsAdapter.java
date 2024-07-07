package com.example.pawpal10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartDetailsAdapter extends RecyclerView.Adapter<CartDetailsAdapter.ViewHolder> {

    Context context ;
    List<CartDetailsModel> cartDetailsModelList;

    public CartDetailsAdapter(FragmentActivity activity, List<CartDetailsModel> cartModelList) {
    }


    @NonNull
    @Override
    public CartDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartDetailsAdapter.ViewHolder holder, int position) {

        holder.name.setText(cartDetailsModelList.get(position).getProduct_name());
        holder.price.setText(String.valueOf(cartDetailsModelList.get(position).getProduct_price()));
        holder.quantity.setText(String.valueOf(cartDetailsModelList.get(position).getQuantity()));


    }

    @Override
    public int getItemCount() {
        return cartDetailsModelList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,price,quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.product_name_value);
            price=itemView.findViewById(R.id.product_price_value);
            quantity=itemView.findViewById(R.id.product_quantity_value);

        }
    }
}
