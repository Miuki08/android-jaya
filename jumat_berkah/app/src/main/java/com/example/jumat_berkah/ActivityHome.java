package com.example.jumat_berkah;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHome extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FlowerAdapter flowerAdapter;
    private Button btnOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerViewFlowers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnOrderHistory = findViewById(R.id.btnOrderHistory); // Tombol untuk membuka riwayat
        btnOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityHome.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        fetchFlowers();
    }

    private void fetchFlowers() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Flower>> call = apiService.getFlowers();

        call.enqueue(new Callback<List<Flower>>() {
            @Override
            public void onResponse(Call<List<Flower>> call, Response<List<Flower>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Flower> flowerList = response.body();
                    flowerAdapter = new FlowerAdapter(ActivityHome.this, flowerList);
                    recyclerView.setAdapter(flowerAdapter);
                } else {
                    Toast.makeText(ActivityHome.this, "Gagal memuat data bunga", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Flower>> call, Throwable t) {
                Toast.makeText(ActivityHome.this, "Kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }
}
