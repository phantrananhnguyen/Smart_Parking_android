package com.example.smartparking.Models;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.*;

public class WebSocketManager {

    private static final String TAG = "WebSocketManager";
    private static final String SERVER_URL = "wss://0c5b-42-119-205-123.ngrok-free.app"; // Thay bằng IP thật khi chạy trên thiết bị

    private WebSocket webSocket;
    private OkHttpClient client;
    private WebSocketCallback callback;

    public interface WebSocketCallback {
        void onSlotUpdate(String slot, String status);
    }

    public boolean isConnected() {
        return webSocket != null;
    }

    public void setWebSocketCallback(WebSocketCallback callback) {
        this.callback = callback;
    }

    public void start() {
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(SERVER_URL)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d(TAG, "✅ WebSocket connected");

                // Gửi sự kiện đăng ký là dashboard
                JSONObject registerMsg = new JSONObject();
                try {
                    registerMsg.put("event", "register");
                    registerMsg.put("type", "dashboard");
                    webSocket.send(registerMsg.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "❌ JSON error", e);
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d(TAG, "📩 Message received: " + text);
                try {
                    JSONObject json = new JSONObject(text);
                    String event = json.optString("event");

                    if ("slot_update".equals(event)) {
                        String slot = json.optString("slot");
                        String status = json.optString("status");

                        if (callback != null) {
                            callback.onSlotUpdate(slot, status);
                        }
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "❌ JSON parse error", e);
                }
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
