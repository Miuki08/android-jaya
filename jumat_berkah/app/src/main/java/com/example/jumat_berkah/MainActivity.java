package com.example.jumat_berkah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi Retrofit
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Inisialisasi ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang mendaftarkan akun...");
        progressDialog.setCancelable(false);

        // Inisialisasi komponen UI
        TextInputEditText username = findViewById(R.id.Username);
        TextInputEditText email = findViewById(R.id.Email);
        TextInputEditText password = findViewById(R.id.Password);
        TextInputEditText confPassword = findViewById(R.id.KonfirmasiPassword);
        Button btnRegister = findViewById(R.id.btnRegist);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Tombol login diklik
        btnLogin.setOnClickListener(v -> {
            // Intent untuk pindah ke ActivityLogin
            Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
            startActivity(intent);
            finish();
        });

        // Tombol registrasi diklik
        btnRegister.setOnClickListener(v -> {
            String sUsername = username.getText().toString();
            String sEmail = email.getText().toString();
            String sPassword = password.getText().toString();
            String sConfPassword = confPassword.getText().toString();

            // Validasi input
            if (sUsername.isEmpty() || sEmail.isEmpty() || sPassword.isEmpty() || sConfPassword.isEmpty()) {
                Toast.makeText(MainActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!sPassword.equals(sConfPassword)) {
                Toast.makeText(MainActivity.this, "Password tidak cocok", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tampilkan ProgressDialog
            progressDialog.show();

            // Panggil API registrasi
            Call<ApiResponse> call = apiService.registerUser (sUsername, sEmail, sPassword);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();
                        Log.d("API Response", "Response: " + apiResponse.toString()); // Log respons

                        if (apiResponse.getMessage() != null) {
                            Toast.makeText(MainActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("Navigation", "Navigating to Login Activity"); // Log navigasi
                            Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
                            startActivity(intent);
                            finish();
                        } else if (apiResponse.getError() != null) {
                            Toast.makeText(MainActivity.this, apiResponse.getError(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}