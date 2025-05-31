package com.example.smartparking;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartparking.Models.ApiClient;
import com.example.smartparking.Models.ProfileResponse;
import com.example.smartparking.Models.SendOtpRequest;
import com.example.smartparking.Models.UserSession;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
private AuthApi authApi;
TextView name, tvemail, joinTime, numofTicket, plate, status;
LinearLayout logout;
String email;
private static final DateTimeFormatter outputFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("vi", "VN"));
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authApi = ApiClient.getClient().create(AuthApi.class);
        email = UserSession.getInstance().getEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.name);
        tvemail = view.findViewById(R.id.email);
        joinTime = view.findViewById(R.id.joinTime);
        numofTicket = view.findViewById(R.id.numofTicket);
        plate = view.findViewById(R.id.plate);
        status = view.findViewById(R.id.status);
        logout = view.findViewById(R.id.logout);
        fetchProfile();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });


        return view;
    }

    private void fetchProfile(){
        authApi.fetchProfile(email).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    ProfileResponse profileResponse = response.body();
                    String formattedJoin = formatDateString(profileResponse.getJoin());

                    name.setText(UserSession.getInstance().getName());
                    tvemail.setText(email);
                    joinTime.setText(formattedJoin);
                    numofTicket.setText(String.valueOf(profileResponse.getNumberOfTickets()));
                    plate.setText(profileResponse.getPlate());
                    status.setText(profileResponse.getLatestTicketStatus());
                    if (profileResponse.getLatestTicketStatus().equals("Active")){
                        status.setBackgroundResource(R.drawable.status_active);
                        status.setTextColor(Color.WHITE);
                    } else {
                        status.setBackgroundResource(R.drawable.status_expired);
                        status.setTextColor(Color.WHITE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable throwable) {

            }
        });
    }
    private void logoutUser() {
        // Xoá dữ liệu người dùng
        UserSession.getInstance().clearSession();

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xoá stack
        startActivity(intent);
    }

    private String formatDateString(String inputDateStr) {
        try {
            OffsetDateTime dateTime = OffsetDateTime.parse(inputDateStr);
            return dateTime.format(outputFormatter);
        } catch (DateTimeParseException e) {
            return inputDateStr;
        }
    }
    private void showLogoutDialog() {
        Dialog dialogView = new Dialog(requireContext());
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView.setContentView(R.layout.logout);

        Window window = dialogView.getWindow();
        if(window !=null){
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER);
        }
        Button ok = dialogView.findViewById(R.id.ok_lo);
        Button cancel = dialogView.findViewById(R.id.cancel_lo);
        dialogView.show();

        ok.setOnClickListener(v -> {
            logoutUser();
            dialogView.dismiss();
        });
        cancel.setOnClickListener(v -> {
            dialogView.dismiss();
        });
    }
}