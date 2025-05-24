package com.example.jumat_berkah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {

    private ApiService apiService;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi Retrofit
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Inisialisasi ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang memproses login...");
        progressDialog.setCancelable(false);

        // Inisialisasi komponen UI
        TextInputEditText username = findViewById(R.id.Username);
        TextInputEditText password = findViewById(R.id.Password);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegist);

        // Tombol login diklik
        btnLogin.setOnClickListener(v -> {
            String sUsername = username.getText().toString();
            String sPassword = password.getText().toString();

            // Validasi input
            if (sUsername.isEmpty() || sPassword.isEmpty()) {
                Toast.makeText(ActivityLogin.this, "Username dan password harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tampilkan ProgressDialog
            progressDialog.show();

            // Panggil API login
            Call<ApiResponse> call = apiService.loginUser(sUsername, sPassword);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();

                        if (apiResponse.getMessage() != null) {
                            // Simpan user_id ke SharedPreferences jika tersedia
                            if (apiResponse.getUser() != null) {
                                SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("user_id", apiResponse.getUser().getId_users());
                                editor.apply();
                            }

                            Toast.makeText(ActivityLogin.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();

                            // Arahkan ke halaman Home
                            Intent intent = new Intent(ActivityLogin.this, ActivityHome.class);
                            startActivity(intent);
                            finish();
                        } else if (apiResponse.getError() != null) {
                            Toast.makeText(ActivityLogin.this, apiResponse.getError(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ActivityLogin.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ActivityLogin.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Tombol register diklik
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
