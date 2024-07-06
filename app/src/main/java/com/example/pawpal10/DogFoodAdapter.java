package com.example.pawpal10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DogFoodAdapter extends RecyclerView.Adapter<DogFoodAdapter.ViewHolder> {

    private Context context;
    private List<DogFoodItem> dogFoodItems;

    public DogFoodAdapter(Context context, List<DogFoodItem> dogFoodItems) {
        this.context = context;
        this.dogFoodItems = dogFoodItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dog_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DogFoodItem item = dogFoodItems.get(position);
        holder.productImage.setImageResource(item.getImageResource());
        holder.productName.setText(item.getName());
        holder.productPrice.setText(item.getPrice());
        holder.productRating.setRating(item.getRating());
        holder.reviewCount.setText(context.getString(R.string.review_count, item.getReviewCount()));
        holder.addToCartButton.setOnClickListener(view -> {
            // Handle add to cart logic
        });
    }

    @Override
    public int getItemCount() {
        return dogFoodItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        RatingBar productRating;
        TextView reviewCount;
        Button addToCartButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productRating = itemView.findViewById(R.id.product_rating);
            reviewCount = itemView.findViewById(R.id.review_count);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}

