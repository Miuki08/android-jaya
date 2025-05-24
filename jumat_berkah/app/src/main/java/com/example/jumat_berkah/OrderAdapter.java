package com.example.jumat_berkah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> orderList;
    private Context context;
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onEdit(Order order);
        void onDelete(Order order);
    }

    public OrderAdapter(List<Order> orderList, Context context, OnOrderActionListener listener) {
        this.orderList = orderList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvName.setText(order.getFlowerName());
        holder.tvQty.setText("Jumlah: " + order.getQuantity());
        holder.tvTotal.setText("Total: Rp " + order.getTotalPrice());

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(order));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvTotal;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvOrderFlower);
            tvQty = itemView.findViewById(R.id.tvOrderQty);
            tvTotal = itemView.findViewById(R.id.tvOrderTotal);
            btnEdit = itemView.findViewById(R.id.btnEditOrder);
            btnDelete = itemView.findViewById(R.id.btnDeleteOrder);
        }
    }
}
