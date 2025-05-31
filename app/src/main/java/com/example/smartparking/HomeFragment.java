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
import com.example.smartparking.Models.ProfileResponse;
import com.example.smartparking.Models.Slot;
import com.example.smartparking.Models.UserSession;
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
    private final List<Slot> slots = new ArrayList<>();
    private final Map<String, String> slotStatusMap = new HashMap<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authApi = ApiClient.getClient().create(AuthApi.class);
        webSocketManager = new WebSocketManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ các ô đỗ xe
        slotA1 = view.findViewById(R.id.A1);
        slotA2 = view.findViewById(R.id.A2);
        slotA3 = view.findViewById(R.id.A3);
        slotA4 = view.findViewById(R.id.A4);
        slotB1 = view.findViewById(R.id.B1);
        slotB2 = view.findViewById(R.id.B2);

        // Thiết lập callback của WebSocket (sau khi view đã sẵn sàng)
        webSocketManager.setWebSocketCallback((slot, status) -> {
            Log.d("WebSocket", "Nhận từ socket: " + slot + " -> " + status);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> updateSlotImage(slot, status));
            }
        });

        // Bắt đầu WebSocket
        webSocketManager.start();

        // Lấy trạng thái lần đầu từ server
        fetchStatus();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Làm mới trạng thái để đảm bảo WebSocket có thể cập nhật lại UI
        slotStatusMap.clear();

        if (!webSocketManager.isConnected()) {
            webSocketManager.start();
        }
    }

    private void fetchStatus() {
        String email = UserSession.getInstance().getEmail();
        authApi.fetchStatus().enqueue(new Callback<List<Slot>>() {
            @Override
            public void onResponse(Call<List<Slot>> call, Response<List<Slot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    slots.clear();
                    slots.addAll(response.body());

                    for (Slot slot : slots) {
                        updateSlotImage(slot.getSlotId(), slot.getStatus());
                        Log.d("API_RESPONSE", slot.getSlotId() + " -> " + slot.getStatus());
                    }
                } else {
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
                Toast.makeText(getContext(), "Kết nối thất bại: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_FAILURE", "Lỗi khi gọi API", t);
            }
        });
        authApi.fetchProfile(email).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileResponse profileResponse = response.body();
                    UserSession.getInstance().setTicketStatus(profileResponse.getLatestTicketStatus());
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable throwable) {

            }
        });
    }

    private void updateSlotImage(String slot, String status) {
        String previousStatus = slotStatusMap.get(slot);
        if (status.equals(previousStatus)) {
            Log.d("WebSocket", "Không thay đổi trạng thái cho " + slot);
            return;
        }

        slotStatusMap.put(slot, status); // Cập nhật trạng thái

        Integer resId = null;
        int bgColor;

        switch (status.toLowerCase()) {
            case "inuse":
                resId = R.drawable.car;
                bgColor = Color.parseColor("#FFCDD2"); // Đỏ nhạt
                break;
            case "block":
                resId = R.drawable.padlock;
                bgColor = Color.parseColor("#FBD574"); // Vàng
                break;
            default:
                bgColor = Color.parseColor("#C8E6C9"); // Xanh lá nhạt (free)
                break;
        }

        ImageView slotView = getSlotViewById(slot);
        if (slotView != null) {
            if (resId != null) {
                slotView.setImageResource(resId);
            } else {
                slotView.setImageDrawable(null);
            }
            slotView.setBackgroundColor(bgColor);
            slotView.invalidate();
            slotView.requestLayout();
        } else {
            Log.w("WebSocket", "Slot không xác định: " + slot);
        }
    }

    private ImageView getSlotViewById(String slotId) {
        switch (slotId) {
            case "A1": return slotA1;
            case "A2": return slotA2;
            case "A3": return slotA3;
            case "A4": return slotA4;
            case "B1": return slotB1;
            case "B2": return slotB2;
            default: return null;
        }
    }
}
