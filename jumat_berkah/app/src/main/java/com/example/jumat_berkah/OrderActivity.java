package com.example.jumat_berkah;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private ImageView ivFlower;
    private TextView tvName, tvPrice;
    private EditText etQuantity;
    private Button btnOrder;

    private int flowerId;
    private String flowerName;
    private double flowerPrice;
    private String flowerImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Inisialisasi komponen UI
        ivFlower = findViewById(R.id.ivFlower);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        etQuantity = findViewById(R.id.etQuantity);
        btnOrder = findViewById(R.id.btnOrder);

        // Ambil data dari Intent
        flowerId = getIntent().getIntExtra("id", -1);
        flowerName = getIntent().getStringExtra("name");
        flowerPrice = getIntent().getDoubleExtra("price", 0.0);
        flowerImageUrl = getIntent().getStringExtra("image_url");

        // Tampilkan data ke UI
        tvName.setText(flowerName);
        tvPrice.setText("Rp " + flowerPrice);
        Glide.with(this).load(flowerImageUrl).into(ivFlower);

        // Tombol "Pesan Sekarang"
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qtyStr = etQuantity.getText().toString().trim();
                if (qtyStr.isEmpty()) {
                    Toast.makeText(OrderActivity.this, "Masukkan jumlah pesanan!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity = Integer.parseInt(qtyStr);

                // Kirim pesanan ke server
                sendOrderToServer(flowerId, quantity);
            }
        });
    }

    private void sendOrderToServer(int flowerId, int quantity) {
        // Ambil user_id dari SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ApiResponse> call = apiService.orderFlower(userId, flowerId, quantity);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse res = response.body();
                    if (res.getMessage() != null) {
                        Toast.makeText(OrderActivity.this, res.getMessage(), Toast.LENGTH_LONG).show();
                        finish(); // Tutup halaman setelah pesan berhasil
                    } else if (res.getError() != null) {
                        Toast.makeText(OrderActivity.this, "Gagal pesan: " + res.getError(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(OrderActivity.this, "Gagal: response tidak valid", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
