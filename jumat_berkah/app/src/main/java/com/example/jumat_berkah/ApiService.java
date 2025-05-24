package com.example.jumat_berkah;

import retrofit2.Call;
import java.util.List;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    // Endpoint untuk registrasi
    @FormUrlEncoded
    @POST("register.php")
    Call<ApiResponse> registerUser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    // Endpoint untuk login
    @FormUrlEncoded
    @POST("login.php")
    Call<ApiResponse> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    // Ambil semua data bunga
    @GET("get_flowers.php")
    Call<List<Flower>> getFlowers();

    // Pesan bunga
    @FormUrlEncoded
    @POST("order_flower.php")
    Call<ApiResponse> orderFlower(
            @Field("user_id") int userId,
            @Field("flower_id") int flowerId,
            @Field("quantity") int quantity
    );

    // Ambil daftar pesanan user (menggunakan wrapper OrderResponse)
    @GET("get_orders.php")
    Call<OrderResponse> getUserOrders(@Query("user_id") int userId);

    // Hapus pesanan
    @FormUrlEncoded
    @POST("delete_order.php")
    Call<ApiResponse> deleteOrder(@Field("order_id") int orderId);

    // Update jumlah pesanan
    @FormUrlEncoded
    @POST("update_order.php")
    Call<ApiResponse> updateOrder(
            @Field("order_id") int orderId,
            @Field("quantity") int quantity
    );
}
