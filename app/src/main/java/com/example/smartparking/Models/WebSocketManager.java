package com.example.smartparking.Models;

import android.util.Log;
import okhttp3.*;

public class WebSocketManager {

    private static final String TAG = "WebSocketManager";
    private static final String SERVER_URL = "ws://10.0.2.2:3000"; // Đổi IP nếu cần

    private WebSocket webSocket;
    private OkHttpClient client;

    public void start() {
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(SERVER_URL)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d(TAG, "✅ WebSocket connected");
                webSocket.send("Hello from Android!");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d(TAG, "📩 Message received: " + text);

                // TODO: Xử lý dữ liệu ở đây (ví dụ: cập nhật UI, gửi vào ViewModel, ...)
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
                Log.d(TAG, "🔌 WebSocket closing: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e(TAG, "❌ WebSocket error", t);
            }
        });
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "Client closed");
        }
        if (client != null) {
            client.dispatcher().executorService().shutdown();
        }
    }
}
