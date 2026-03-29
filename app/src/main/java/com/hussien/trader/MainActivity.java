package com.hussien.trader;

import android.os.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView priceTxt, aiStatus;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        priceTxt = findViewById(R.id.priceTxt);
        aiStatus = findViewById(R.id.aiStatus);

        // تشغيل البوت تلقائياً عند الفتح 🚀
        startTradingEngine();
    }

    private void startTradingEngine() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // محاكاة سحب السعر من API (الذكاء الاصطناعي)
                        final double newPrice = 4550 + (new Random().nextDouble() * 20);
                        
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                priceTxt.setText(String.format("%.2f", newPrice));
                                updateAISignals(newPrice);
                            }
                        });
                        Thread.sleep(2000); // تحديث كل ثانيتين
                    } catch (Exception e) {}
                }
            }
        }).start();
    }

    private void updateAISignals(double price) {
        // خوارزمية حسين للذهب
        if (price > 4565) {
            aiStatus.setText("🔥 SIGNAL: SELL NOW!");
            aiStatus.setTextColor(0xFFC62828);
        } else if (price < 4555) {
            aiStatus.setText("🚀 SIGNAL: BUY NOW!");
            aiStatus.setTextColor(0xFF2E7D32);
        } else {
            aiStatus.setText("⏳ AI: MONITORING MARKET...");
            aiStatus.setTextColor(0xFF00FF00);
        }
    }
}
