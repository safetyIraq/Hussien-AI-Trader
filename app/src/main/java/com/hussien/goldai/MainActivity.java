package com.hussien.goldai;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONObject;

public class MainActivity extends Activity {

    private TextView priceLabel, signalStatus;
    private List<Double> priceHistory = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        priceLabel = findViewById(R.id.priceLabel);
        signalStatus = findViewById(R.id.signalStatus);

        // تشغيل محرك التداول الحقيقي 🚀
        startLiveTradingLoop();
    }

    // دالة فحص العطلة الأسبوعية (السوق مغلق السبت والأحد)
    private boolean isMarketOpen() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        // في توقيت جرينتش، السوق يغلق نهاية الجمعة ويفتح بداية الإثنين
        return (day != Calendar.SATURDAY && day != Calendar.SUNDAY);
    }

    private void startLiveTradingLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isMarketOpen()) {
                    priceLabel.setText("السوق مغلق (عطلة)");
                    signalStatus.setText("💤 البوت في وضع السبات");
                    signalStatus.setTextColor(0xFFBBBBBB);
                    
                    // يفحص كل ساعة أيام العطلة حتى ما يصرف بطارية
                    handler.postDelayed(this, 3600000); 
                    return;
                }

                // سحب السعر الحقيقي بـ Thread منفصل حتى ما يكرش التطبيق
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final double livePrice = fetchRealGoldPrice();
                        
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (livePrice > 0) {
                                    priceHistory.add(livePrice);
                                    if (priceHistory.size() > 20) priceHistory.remove(0);

                                    priceLabel.setText("XAU/USD: " + String.format("%.2f", livePrice));
                                    
                                    // استدعاء محرك الذكاء الاصطناعي
                                    String signal = TradingEngine.analyzeMarket(livePrice, priceHistory);
                                    signalStatus.setText(signal);
                                } else {
                                    signalStatus.setText("⚠️ جاري الاتصال بالسيرفر...");
                                }
                            }
                        });
                    }
                }).start();

                // تحديث السعر كل 3 ثواني وقت عمل السوق
                handler.postDelayed(this, 3000);
            }
        }, 1000); // يبدأ بعد ثانية من فتح التطبيق
    }

    // دالة سحب السعر الحقيقي من API مجاني (Yahoo Finance API)
    private double fetchRealGoldPrice() {
        try {
            URL url = new URL("https://query1.finance.yahoo.com/v7/finance/quote?symbols=GC=F");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // استخراج السعر من ملف الـ JSON
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject quoteResponse = jsonObject.getJSONObject("quoteResponse");
            return quoteResponse.getJSONArray("result").getJSONObject(0).getDouble("regularMarketPrice");

        } catch (Exception e) {
            e.printStackTrace();
            return -1; // في حال فشل الاتصال
        }
    }
}
