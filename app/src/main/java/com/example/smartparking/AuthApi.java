package com.example.smartparking;

import com.example.smartparking.Models.LoginResponse;
import com.example.smartparking.Models.OtpResponse;
import com.example.smartparking.Models.SendOtpRequest;
import com.example.smartparking.Models.UserRequest;
import com.example.smartparking.Models.Userlogin;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/api/user/SignUp")
    Call<LoginResponse> signUp(@Body UserRequest userRequest);

    @POST("/api/user/SignIn")
    Call<LoginResponse> signIn(@Body Userlogin userlogin);

    @POST("/api/user/updateName")
    Call<ResponseBody> updateName(@Body UserRequest userRequest);

    @POST("/api/user/purchase_ticket")
    Call<ResponseBody> purchase(@Body Map<String, Object> requestBody);

    @POST("/api/otp/verify-otp")
    Call<OtpResponse> sendOTP(@Body SendOtpRequest sendOtpRequest);
}

