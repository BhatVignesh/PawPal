package com.example.pawpal10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartDetailsAdapter extends RecyclerView.Adapter<CartDetailsAdapter.ViewHolder> {

    private Context context;
    private List<CartDetailsModel> cartDetailsModelList;
    private OnItemClickListener listener;

    public CartDetailsAdapter(Context context, List<CartDetailsModel> cartModelList) {
        this.context = context;
        this.cartDetailsModelList = cartModelList;
    }
    public interface OnItemClickListener {
        void onAdd1ToCartClick(CartDetailsModel item);
        void onDelete1FromCartClick(CartDetailsModel item);
    }
    public void setOnItemClickListener(CartDetailsAdapter.OnItemClickListener listener) {
        this.listener = listener;
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
        holder.price.setText(String.valueOf(model.getTotal_price()));
        holder.quantity.setText(String.valueOf(model.getQuantity()));
        holder.size.setText(String.valueOf(model.getSize()));
        Glide.with(context)
                .load(model.getImageUrl())
                .into(holder.item_image);
        holder.add1.setOnClickListener(view -> {
            if (listener != null) {
                listener.onAdd1ToCartClick(model);
            }
        });
        holder.delete1.setOnClickListener(view -> {
            if (listener != null) {
                listener.onDelete1FromCartClick(model);
            }
        });

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
        TextView name, price, quantity,size;
        ImageView item_image;
        Button add1,delete1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name_value);
            price = itemView.findViewById(R.id.product_price_value);
            quantity = itemView.findViewById(R.id.product_count);
            item_image=itemView.findViewById(R.id.item_image);
            size=itemView.findViewById(R.id.size_view);
            add1=itemView.findViewById(R.id.incrementButton);
            delete1=itemView.findViewById(R.id.decrementButton);
        }
    }
}

