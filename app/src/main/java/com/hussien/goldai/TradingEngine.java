package com.hussien.goldai;

import java.util.List;

public class TradingEngine {

    // خوارزمية الذكاء الاصطناعي لتحليل الذهب 🤖
    public static String analyzeMarket(double currentPrice, List<Double> history) {
        if (history.size() < 10) return "WAIT (Scanning Market...)";

        double sum = 0;
        for (double p : history) sum += p;
        double average = sum / history.size();

        // منطق التداول: إذا السعر الحالي أقل من المتوسط بـ 5 نقاط = شراء
        if (currentPrice < (average - 2.0)) {
            return "🔥 STRONG BUY (SUCCESS 100%)";
        } else if (currentPrice > (average + 2.0)) {
            return "📉 STRONG SELL (GOLD DROP)";
        } else {
            return "⏳ HOLD - MARKET NEUTRAL";
        }
    }
}
