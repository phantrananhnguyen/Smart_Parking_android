package com.example.smartparking.Models;

import com.example.smartparking.Models.UserSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Tạo OkHttpClient với Interceptor
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request originalRequest = chain.request();

                            // Lấy token từ UserSession
                            String token = UserSession.getInstance().getToken();

                            // Nếu không có token thì gửi request bình thường
                            if (token == null || token.isEmpty()) {
                                return chain.proceed(originalRequest);
                            }

                            // Thêm Authorization header
                            Request newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + token)
                                    .build();

                            return chain.proceed(newRequest);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000") // Đổi nếu cần
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
