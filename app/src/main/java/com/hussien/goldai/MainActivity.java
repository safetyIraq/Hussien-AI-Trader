package com.hussien.goldai;

import android.os.*;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private TextView priceLabel, signalStatus;
    private List<Double> priceHistory = new ArrayList<>();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        priceLabel = findViewById(R.id.priceLabel);
        signalStatus = findViewById(R.id.signalStatus);

        // محاكاة سحب البيانات كل 2 ثانية (Real-time Simulation)
        startTradingLoop();
    }

    private void startTradingLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                double livePrice = 4550 + (new Random().nextDouble() * 15);
                priceHistory.add(livePrice);
                if (priceHistory.size() > 20) priceHistory.remove(0);

                priceLabel.setText("XAU/USD: " + String.format("%.2f", livePrice));
                
                // استدعاء محرك الذكاء الاصطناعي الداخلي
                String signal = TradingEngine.analyzeMarket(livePrice, priceHistory);
                signalStatus.setText(signal);

                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }
}
