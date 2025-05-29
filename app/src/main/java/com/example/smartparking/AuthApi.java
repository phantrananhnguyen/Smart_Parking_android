package com.example.smartparking;

import com.example.smartparking.Models.LoginResponse;
import com.example.smartparking.Models.MonthTicket;
import com.example.smartparking.Models.OtpResponse;
import com.example.smartparking.Models.QRCodeRequest;
import com.example.smartparking.Models.QRCodeResponse;
import com.example.smartparking.Models.SendOtpRequest;
import com.example.smartparking.Models.Slot;
import com.example.smartparking.Models.UserRequest;
import com.example.smartparking.Models.Userlogin;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {

    @POST("/api/user/SignUp")
    Call<LoginResponse> signUp(@Body UserRequest userRequest);

    @POST("/api/user/SignIn")
    Call<LoginResponse> signIn(@Body Userlogin userlogin);

    @POST("/api/user/updateName")
    Call<ResponseBody> updateName(@Body UserRequest userRequest);

    @POST("/api/otp/month_ticket")
    Call<ResponseBody> purchase(@Body MonthTicket monthTicket);

    @POST("/api/otp/verify-otp")
    Call<OtpResponse> sendOTP(@Body SendOtpRequest sendOtpRequest);

    @GET("/api/otp/history")
    Call<List<MonthTicket>> fetchHistory(@Query("email") String email);

    @GET("/api/otp/status")
    Call<List<Slot>> fetchStatus();
    @POST("api/qr/get-qr")
    Call<QRCodeResponse> getQRCode(@Body QRCodeRequest request);

}

