package com.hussien.goldai;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.app.Activity; // التعديل الذهبي هنا ✅
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity { // وهنا أيضاً ✅

    private TextView priceLabel, signalStatus;
    private List<Double> priceHistory = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper()); // لتجنب أي كراش بالخلفية

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        priceLabel = findViewById(R.id.priceLabel);
        signalStatus = findViewById(R.id.signalStatus);

        // تشغيل محرك التداول 🚀
        startTradingLoop();
    }

    private void startTradingLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // توليد سعر ذهب تقريبي (Live Simulation)
                double livePrice = 4550 + (new Random().nextDouble() * 15);
                priceHistory.add(livePrice);
                if (priceHistory.size() > 20) priceHistory.remove(0);

                priceLabel.setText("XAU/USD: " + String.format("%.2f", livePrice));
                
                // استدعاء محرك الذكاء الاصطناعي الداخلي
                String signal = TradingEngine.analyzeMarket(livePrice, priceHistory);
                signalStatus.setText(signal);

                // تكرار العملية كل ثانيتين
                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }
}
