package com.example.jumat_berkah;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.ViewHolder> {

    private List<Flower> flowerList;
    private Context context;

    public FlowerAdapter(Context context, List<Flower> flowerList) {
        this.context = context;
        this.flowerList = flowerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flowe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Flower flower = flowerList.get(position);
        holder.name.setText(flower.getName());
        holder.price.setText("Rp " + flower.getPrice());

        // Load gambar dengan Glide
        Glide.with(context).load(flower.getImage_url()).into(holder.image);

        // Tambahkan listener ketika item diklik
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderActivity.class);
            intent.putExtra("id", flower.getId());
            intent.putExtra("name", flower.getName());
            intent.putExtra("price", flower.getPrice());
            intent.putExtra("image_url", flower.getImage_url());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return flowerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            price = itemView.findViewById(R.id.tvPrice);
            image = itemView.findViewById(R.id.ivFlower);
        }
    }
}
