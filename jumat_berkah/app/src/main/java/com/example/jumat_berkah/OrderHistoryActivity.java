package com.example.jumat_berkah;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ApiService apiService;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.recyclerOrders);
        progressBar = findViewById(R.id.progressOrders);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Ambil user ID dari SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadOrders(userId);
    }

    private void loadOrders(int userId) {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getUserOrders(userId).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body().getOrders();
                    if (orders == null || orders.isEmpty()) {
                        Toast.makeText(OrderHistoryActivity.this, "Belum ada pesanan", Toast.LENGTH_SHORT).show();
                    }

                    adapter = new OrderAdapter(orders, OrderHistoryActivity.this, new OrderAdapter.OnOrderActionListener() {
                        @Override
                        public void onEdit(Order order) {
                            showEditDialog(order);
                        }

                        @Override
                        public void onDelete(Order order) {
                            deleteOrder(order.getId());
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "Gagal memuat data pesanan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OrderHistoryActivity.this, "Kesalahan jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog(Order order) {
        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(order.getQuantity()));

        new AlertDialog.Builder(this)
                .setTitle("Edit Jumlah Pesanan")
                .setView(input)
                .setPositiveButton("Simpan", (dialog, which) -> {
                    try {
                        int newQty = Integer.parseInt(input.getText().toString());
                        apiService.updateOrder(order.getId(), newQty).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                Toast.makeText(OrderHistoryActivity.this, "Pesanan diperbarui", Toast.LENGTH_SHORT).show();
                                recreate();
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toast.makeText(OrderHistoryActivity.this, "Gagal memperbarui pesanan", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (NumberFormatException e) {
                        Toast.makeText(OrderHistoryActivity.this, "Jumlah tidak valid", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void deleteOrder(int orderId) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Pesanan")
                .setMessage("Yakin ingin menghapus pesanan ini?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    apiService.deleteOrder(orderId).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            Toast.makeText(OrderHistoryActivity.this, "Pesanan dihapus", Toast.LENGTH_SHORT).show();
                            recreate();
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Toast.makeText(OrderHistoryActivity.this, "Gagal menghapus pesanan", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Batal", null)
                .show();
    }
}
