package com.example.smartparking;

import com.example.smartparking.Models.OtpResponse;
import com.example.smartparking.Models.SendOtpRequest;
import com.example.smartparking.Models.VerifyOtpRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OtpApi {

    @POST("/api/send-otp")
    Call<OtpResponse> sendOtp(@Body SendOtpRequest sendOtpRequest);

    @POST("/api/verify-otp")
    Call<OtpResponse> verifyOtp(@Body VerifyOtpRequest verifyOtpRequest);

}