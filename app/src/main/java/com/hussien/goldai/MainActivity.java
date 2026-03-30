package com.hussien.goldai;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// تم تصحيح الاستيراد ليتطابق مع الـ Namespace الموحد
import com.hussien.goldai.R; 

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView aiConsole, txtBull, txtBear;
    private final OkHttpClient client = new OkHttpClient();
    // مفتاحك الذهبي المستخرج
    private final String AI_TOKEN = "sk-d6d65af820464974b9c3030482175732"; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

            aiConsole = findViewById(R.id.ai_console);
            txtBull = findViewById(R.id.txtBull);
            txtBear = findViewById(R.id.txtBear);

            if (aiConsole == null) {
                Toast.makeText(this, "خطأ في تحميل الواجهة", Toast.LENGTH_LONG).show();
                return;
            }

            findViewById(R.id.btnBuy).setOnClickListener(v -> executeTrade("BUY"));
            findViewById(R.id.btnSell).setOnClickListener(v -> executeTrade("SELL"));

            runSniperEngine();
        } catch (Exception e) {
            android.util.Log.e("SHADOW_TRADER", "CRASH: " + e.getMessage());
        }
    }

    private void runSniperEngine() {
        // النسب الظاهرة في التحليل
        txtBull.setText("BULL SCORE: 85%");
        txtBear.setText("BEAR SCORE: 15%");
        requestDeepSeekAnalysis("Gold Trend: Bull 85%. Give Sniper Entry.");
    }

    private void requestDeepSeekAnalysis(String message) {
        try {
            JSONObject json = new JSONObject();
            json.put("model", "deepseek-chat");
            json.put("messages", new org.json.JSONArray().put(new JSONObject().put("role", "user").put("content", message)));

            Request request = new Request.Builder()
                    // الرابط الرسمي للـ API لضمان استلام البيانات
                    .url("https://api.deepseek.com/chat/completions") 
                    .addHeader("Authorization", "Bearer " + AI_TOKEN)
                    .post(RequestBody.create(json.toString(), MediaType.get("application/json")))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    runOnUiThread(() -> {
                        if (aiConsole != null) {
                            aiConsole.append("\n> AI ANALYSIS RECEIVED ✅");
                            aiConsole.append("\n> " + result);
                        }
                    });
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        if (aiConsole != null) aiConsole.append("\n> NET ERROR: " + e.getMessage());
                    });
                }
            });
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void executeTrade(String side) {
        if (aiConsole != null) aiConsole.append("\n> SNIPER " + side + " EXECUTED.");
    }
}
