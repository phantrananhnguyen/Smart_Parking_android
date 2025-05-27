package com.example.smartparking;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.smartparking.Models.ApiClient;
import com.example.smartparking.Models.MonthTicket;
import com.example.smartparking.Models.OtpResponse;
import com.example.smartparking.Models.SendOtpRequest;
import com.example.smartparking.Models.UserRequest;
import com.example.smartparking.Models.UserSession;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class user_extend_fragment extends Fragment {
    private CheckBox checkbox1, checkbox2, checkbox3;
    private Button sendbtn;
    private ImageButton backbtn;
    private EditText car_plate, email;
    private Spinner carbrands;
    AuthApi authApi;
    String email_user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_user_extend, container, false);
        checkbox1 = view.findViewById(R.id.checkbox1);
        checkbox2 = view.findViewById(R.id.checkbox2);
        checkbox3 = view.findViewById(R.id.checkbox3);
        sendbtn = view.findViewById(R.id.sendbtn);
        backbtn = view.findViewById(R.id.back);
        car_plate = view.findViewById(R.id.edt_carplate);
        email = view.findViewById(R.id.edt_email);
        carbrands = view.findViewById(R.id.car_spin);
        checkbox1.setChecked(true);
        authApi = ApiClient.getClient().create(AuthApi.class);
        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
        @Override
            public void onCheckedChanged(CompoundButton buttonview, boolean b ){
            if (b)
            {
                checkbox2.setChecked(false);
                checkbox3.setChecked(false);
            }
        }
        });
        checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonview, boolean b ){
                if (b)
                {
                    checkbox1.setChecked(false);
                    checkbox3.setChecked(false);
                }
            }
        });
        checkbox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonview, boolean b ){
                if (b)
                {
                    checkbox1.setChecked(false);
                    checkbox2.setChecked(false);
                }
            }
        });
        backbtn.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(R.id.action_ticketFragment2_to_ticketFragment1);
        });
        sendbtn.setOnClickListener(v -> {
            if (isInputValid()) {
                handleSubmit();
            }
            else {
                showErrorDialog();
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.car_brands, android.R.layout.simple_spinner_dropdown_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carbrands.setAdapter(adapter);
        return view;
    }
    private void handleSubmit(){
        String carplate = car_plate.getText().toString();
        String email = this.email.getText().toString();
        UserSession.getInstance().setEmail(email);
        String carbrand = carbrands.getSelectedItem().toString();
        String owner = UserSession.getInstance().getName();
        int amount = getMonthAmount(checkbox1, checkbox2, checkbox3);
        MonthTicket monthTicket = new MonthTicket(carplate, carbrand,owner,"", amount);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("monthTicket", monthTicket);
        authApi.purchase(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle("Attention!!!")
                            .setMessage("Make sure your information is correct? We will not be responsible for any incorrect information. ")
                            .setPositiveButton("OK", (dialogInterface, which) -> {
                                showSuccessDialog();
                                dialogInterface.dismiss();
                            })
                            .setNegativeButton("Cancel", (dialogInterface, which) -> {
                                dialogInterface.dismiss();
                            })
                            .create();

                    dialog.show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }
    private void showSuccessDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.provide_mail_code, null);
        builder.setView(dialogView);

        EditText code_enter = dialogView.findViewById(R.id.edt_otp);
        Button submit = dialogView.findViewById(R.id.confirm);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        String email = UserSession.getInstance().getEmail();
        String OTP = code_enter.getText().toString().trim();
        SendOtpRequest otpRequest = new SendOtpRequest(email,OTP);
        submit.setOnClickListener(v -> {
            if (!OTP.isEmpty()) {
                SendOTP(otpRequest);
            }
            else {
                Toast.makeText(getContext(), "Please enter the code", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        dialog.show();
    }
    private void SendOTP(SendOtpRequest otpRequest){
        authApi.sendOTP(otpRequest).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<OtpResponse> call, Throwable throwable) {
                Toast.makeText(getContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private int getMonthAmount(CheckBox checkbox1, CheckBox checkbox2, CheckBox checkbox3) {
        int amount = 0;
        if (checkbox1.isChecked()) {
            amount = 1;
        } else if (checkbox2.isChecked()) {
            amount = 6;
        } else if (checkbox3.isChecked()) {
            amount = 12;
        }
        return amount;
    }
    private void openFeedbackDialog(int gravity){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.provide_mail_code);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.show();

    }

    private void showErrorDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("ERROR")
                .setMessage("Vui lòng nhập đủ thông tin!")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    private void text() {
        new AlertDialog.Builder(requireContext())
                .setTitle("hehe")
                .setMessage("Vui lòng nhập đủ thông tin!")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    private boolean isInputValid() {
        if (car_plate == null) {
            throw new IllegalStateException("editText1 is null");
        }
        if (email == null) {
            throw new IllegalStateException("editText2 is null");
        }
        if (checkbox1 == null || checkbox2 == null || checkbox3 == null) {
            throw new IllegalStateException("One of the CheckBoxes is null");
        }
        boolean EditText1Filled = !car_plate.getText().toString().trim().isEmpty();
        boolean EditText2Filled = !email.getText().toString().trim().isEmpty();
        boolean AnyCheckBoxChecked =checkbox1.isChecked() || checkbox2.isChecked() || checkbox3.isChecked();
        return EditText1Filled && EditText2Filled && AnyCheckBoxChecked;
    }
}