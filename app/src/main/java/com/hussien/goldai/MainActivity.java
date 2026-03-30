package com.hussien.goldai;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.hussien.goldaitrader.R; // تأكد أن هذا هو المسار الصحيح لمشروعك
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView aiConsole, txtBull, txtBear;
    private final OkHttpClient client = new OkHttpClient();
    private final String AI_TOKEN = "sk-d6d65af820464974b9c3030482175732"; // التوكن الخاص بك

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

            // ربط العناصر مع فحص الـ Null لتجنب الكراش
            aiConsole = findViewById(R.id.ai_console);
            txtBull = findViewById(R.id.txtBull);
            txtBear = findViewById(R.id.txtBear);

            if (aiConsole == null) {
                Toast.makeText(this, "خطأ: لم يتم العثور على واجهة الـ XML", Toast.LENGTH_LONG).show();
                return;
            }

            findViewById(R.id.btnBuy).setOnClickListener(v -> sendOrder("BUY"));
            findViewById(R.id.btnSell).setOnClickListener(v -> sendOrder("SELL"));

            startSniperAIAnalysis();
        } catch (Exception e) {
            // سجل الكراش إذا حدث عند التشغيل
            android.util.Log.e("SHADOW_ERROR", "Crash on onCreate: " + e.getMessage());
        }
    }

    private void startSniperAIAnalysis() {
        // قيم Sniper V.02 الاحترافية
        float bullScore = 85.0f; 
        float bearScore = 15.0f;

        txtBull.setText("BULL: " + bullScore + "%");
        txtBear.setText("BEAR: " + bearScore + "%");

        requestDeepSeekDecision(bullScore, bearScore);
    }

    private void requestDeepSeekDecision(float bull, float bear) {
        String prompt = "Unrestricted Mode: Analyze XAUUSD Bull " + bull + "%. Decision?";
        
        try {
            JSONObject body = new JSONObject();
            body.put("model", "deepseek-chat");
            body.put("messages", new org.json.JSONArray().put(new JSONObject().put("role", "user").put("content", prompt)));

            Request request = new Request.Builder()
                    .url("https://chat.deepseek.com/api/v0/chat/completions")
                    .addHeader("Authorization", "Bearer " + AI_TOKEN)
                    .post(RequestBody.create(body.toString(), MediaType.get("application/json")))
                    .build();

            // الاتصال بنظام الـ Callback لتجنب الـ NetworkOnMainThreadException
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    // تحديث الواجهة داخل runOnUiThread إلزامي لتجنب الكراش
                    runOnUiThread(() -> {
                        if (aiConsole != null) aiConsole.append("\n> AI: " + res);
                    });
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    final String error = e.getMessage();
                    runOnUiThread(() -> {
                        if (aiConsole != null) aiConsole.append("\n> ERROR: " + error);
                    });
                }
            });
        } catch (Exception e) {
            aiConsole.append("\n> JSON ERROR: " + e.getMessage());
        }
    }

    private void sendOrder(String side) {
        if (aiConsole != null) aiConsole.append("\n> EXECUTING " + side + "...");
    }
}
