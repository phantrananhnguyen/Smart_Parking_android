package com.example.smartparking;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smartparking.Models.ApiClient;
import com.example.smartparking.Models.Slot;
import com.example.smartparking.Models.WebSocketManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private ImageView slotA1, slotA2, slotA3, slotA4, slotB1, slotB2;
    private WebSocketManager webSocketManager;
    private AuthApi authApi;
    private List<Slot> slots = new ArrayList<>();
    public HomeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authApi = ApiClient.getClient().create(AuthApi.class);
        webSocketManager = new WebSocketManager();
        webSocketManager.setWebSocketCallback((slot, status) -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> updateSlotImage(slot, status));
            }        });

        webSocketManager.start();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        slotA1 = view.findViewById(R.id.A1);
        slotA2 = view.findViewById(R.id.A2);
        slotA3 = view.findViewById(R.id.A3);
        slotA4 = view.findViewById(R.id.A4);
        slotB1 = view.findViewById(R.id.B1);
        slotB2 = view.findViewById(R.id.B2);

        fetchStatus();
        return view;
    }

    private final Map<String, String> slotStatusMap = new HashMap<>();

    private void updateSlotImage(String slot, String status) {
        // Kiểm tra nếu không đổi trạng thái thì bỏ qua
        String previousStatus = slotStatusMap.get(slot);
        if (status.equals(previousStatus)) {
            Log.d("WebSocket", "Không có thay đổi trạng thái cho " + slot);
            return;
        }
        slotStatusMap.put(slot, status); // Cập nhật trạng thái mới

        Integer resId = null;
        int bgColor;

        Log.d("WebSocket", "Cập nhật " + slot + " sang trạng thái: " + status);

        switch (status.toLowerCase()) {
            case "inuse":
                resId = R.drawable.car;
                bgColor = Color.parseColor("#FFCDD2");
                break;
            case "block":
                resId = R.drawable.padlock;
                bgColor = Color.parseColor("#FBD574");
                break;
            default:
                bgColor = Color.parseColor("#C8E6C9");
                break;
        }

        ImageView slotView = null;
        switch (slot) {
            case "A1": slotView = slotA1; break;
            case "A2": slotView = slotA2; break;
            case "A3": slotView = slotA3; break;
            case "A4": slotView = slotA4; break;
            case "B1": slotView = slotB1; break;
            case "B2": slotView = slotB2; break;
            default:
                Log.w("WebSocket", "Slot không xác định: " + slot);
                return;
        }

        if (slotView != null) {
            if (resId != null) {
                slotView.setImageResource(resId);
            } else {
                slotView.setImageDrawable(null); // Xóa icon nếu free
            }

            slotView.setBackgroundColor(bgColor);
            slotView.invalidate();        // ép redraw
            slotView.requestLayout();     // ép layout lại
        }
    }
    private void fetchStatus(){
        authApi.fetchStatus().enqueue(new Callback<List<Slot>>() {
            @Override
            public void onResponse(Call<List<Slot>> call, Response<List<Slot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    slots.clear();
                    slots.addAll(response.body());

                    for (Slot slot : slots) {
                        updateSlotImage(slot.getSlotId(), slot.getStatus());
                    }
                } else {
                    // Lỗi từ server hoặc body rỗng
                    String message = "Lỗi khi tải dữ liệu: ";
                    if (response.errorBody() != null) {
                        try {
                            message += response.errorBody().string();
                        } catch (IOException e) {
                            message += "Không thể đọc lỗi.";
                        }
                    } else {
                        message += "Phản hồi không hợp lệ hoặc rỗng.";
                    }

                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", message);
                }
            }

            @Override
            public void onFailure(Call<List<Slot>> call, Throwable t) {
                // Lỗi kết nối mạng, timeout, v.v.
                Toast.makeText(getContext(), "Kết nối thất bại: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_FAILURE", "Lỗi khi gọi API", t);
            }

        });
    }




}