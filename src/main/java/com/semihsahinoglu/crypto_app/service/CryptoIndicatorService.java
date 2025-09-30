package com.semihsahinoglu.crypto_app.service;

import com.semihsahinoglu.crypto_app.entity.CryptoCoin;
import com.semihsahinoglu.crypto_app.util.CryptoIndicatorHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CryptoIndicatorService {

    public Map<String, Object> calculateIndicators(List<CryptoCoin> coins, int period) {
        Map<String, Object> result = new HashMap<>();


        result.put("MA", CryptoIndicatorHelper.calculateMA(coins, period));


        result.put("EMA", CryptoIndicatorHelper.calculateEMA(coins, period));


        result.put("WMA", CryptoIndicatorHelper.calculateWMA(coins, period));


        result.put("VWAP", CryptoIndicatorHelper.calculateVWAP(coins));

        List<CryptoIndicatorHelper.BollingerBand> boll = CryptoIndicatorHelper.calculateBollingerBands(coins, period);
        result.put("BOLL", boll);

        return result;
    }
}
