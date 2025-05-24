package com.example.jumat_berkah;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.33.21/jumat_berkah/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Buat instance Gson dengan setLenient(true)
            Gson gson = new GsonBuilder()
                    .setLenient() // Aktifkan mode lenient
                    .create();

            // Tambahkan logging interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            // Buat instance Retrofit dengan Gson yang sudah dikonfigurasi
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)) // Gunakan Gson yang sudah dikonfigurasi
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}
