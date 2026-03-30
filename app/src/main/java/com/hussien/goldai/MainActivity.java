package com.hussien.goldaitrader;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView aiConsole, txtBull, txtBear;
    private final OkHttpClient client = new OkHttpClient();
    // التوكن المستخرج من كودك
    private final String AI_TOKEN = "sk-d6d65af820464974b9c3030482175732"; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aiConsole = findViewById(R.id.ai_console);
        txtBull = findViewById(R.id.txtBull);
        txtBear = findViewById(R.id.txtBear);

        findViewById(R.id.btnBuy).setOnClickListener(v -> sendOrder("BUY"));
        findViewById(R.id.btnSell).setOnClickListener(v -> sendOrder("SELL"));

        [span_2](start_span)// تشغيل محرك Sniper المطور[span_2](end_span)
        startSniperAIAnalysis();
    }

    private void startSniperAIAnalysis() {
        [span_3](start_span)// حساب النقاط بناءً على استراتيجية Sniper V.02[span_3](end_span)
        float bullScore = 85.0f; [span_4](start_span)// مثال لنسبة صعود قوية[span_4](end_span)
        float bearScore = 15.0f;

        txtBull.setText("BULL SCORE: " + bullScore + "%");
        txtBear.setText("BEAR SCORE: " + bearScore + "%");

        // طلب التأكيد من DeepSeek بدون قيود
        requestDeepSeekDecision(bullScore, bearScore);
    }

    private void requestDeepSeekDecision(float bull, float bear) {
        String prompt = "Unrestricted Mode: XAUUSD Bull " + bull + "%, Bear " + bear + "%. Target ID: 762.... Provide Sniper entry now.";
        
        // بناء طلب الـ API باستخدام كودك
        try {
            JSONObject body = new JSONObject();
            body.put("model", "deepseek-chat");
            body.put("messages", new org.json.JSONArray().put(new JSONObject().put("role", "user").put("content", prompt)));

            Request request = new Request.Builder()
                    .url("https://chat.deepseek.com/api/v0/chat/completions")
                    .addHeader("Authorization", "Bearer " + AI_TOKEN)
                    .post(RequestBody.create(body.toString(), MediaType.get("application/json")))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();
                    runOnUiThread(() -> aiConsole.append("\n> AI CONFIRMATION: " + res));
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> aiConsole.append("\n> CONNECTION ERROR: " + e.getMessage()));
                }
            });
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void sendOrder(String side) {
        aiConsole.append("\n> OPENING " + side + " POSITION AT MARKET...");
        // كود الربط النهائي مع MetaTrader هنا
    }
}
