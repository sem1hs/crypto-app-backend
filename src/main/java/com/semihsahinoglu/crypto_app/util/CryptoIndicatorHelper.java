package com.semihsahinoglu.crypto_app.util;

import com.semihsahinoglu.crypto_app.entity.CryptoCoin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CryptoIndicatorHelper {

    public static List<BigDecimal> calculateMA(List<CryptoCoin> coins, int period) {
        List<BigDecimal> maList = new ArrayList<>();
        for (int i = 0; i < coins.size(); i++) {
            if (i + 1 < period) {
                maList.add(null);
                continue;
            }
            BigDecimal sum = BigDecimal.ZERO;
            for (int j = i + 1 - period; j <= i; j++) {
                sum = sum.add(coins.get(j).getClosePrice());
            }
            maList.add(sum.divide(BigDecimal.valueOf(period), 8, RoundingMode.HALF_UP));
        }
        return maList;
    }

    public static List<BigDecimal> calculateEMA(List<CryptoCoin> coins, int period) {
        List<BigDecimal> emaList = new ArrayList<>();
        BigDecimal multiplier = BigDecimal.valueOf(2.0 / (period + 1));
        for (int i = 0; i < coins.size(); i++) {
            BigDecimal close = coins.get(i).getClosePrice();
            if (i == 0) {
                emaList.add(close);
            } else {
                BigDecimal prevEma = emaList.get(i - 1);
                BigDecimal ema = close.subtract(prevEma).multiply(multiplier).add(prevEma);
                emaList.add(ema);
            }
        }
        return emaList;
    }

    public static List<BigDecimal> calculateWMA(List<CryptoCoin> coins, int period) {
        List<BigDecimal> wmaList = new ArrayList<>();
        int weightSum = period * (period + 1) / 2;
        for (int i = 0; i < coins.size(); i++) {
            if (i + 1 < period) {
                wmaList.add(null);
                continue;
            }
            BigDecimal sum = BigDecimal.ZERO;
            for (int j = 0; j < period; j++) {
                int weight = j + 1;
                sum = sum.add(coins.get(i - period + 1 + j).getClosePrice().multiply(BigDecimal.valueOf(weight)));
            }
            wmaList.add(sum.divide(BigDecimal.valueOf(weightSum), 8, RoundingMode.HALF_UP));
        }
        return wmaList;
    }

    public static List<BigDecimal> calculateVWAP(List<CryptoCoin> coins) {
        List<BigDecimal> vwapList = new ArrayList<>();
        BigDecimal cumulativePV = BigDecimal.ZERO;
        BigDecimal cumulativeVolume = BigDecimal.ZERO;
        for (CryptoCoin coin : coins) {
            BigDecimal typicalPrice = coin.getClosePrice();
            BigDecimal volume = coin.getVolume();
            cumulativePV = cumulativePV.add(typicalPrice.multiply(volume));
            cumulativeVolume = cumulativeVolume.add(volume);
            vwapList.add(cumulativePV.divide(cumulativeVolume, 8, RoundingMode.HALF_UP));
        }
        return vwapList;
    }

    public static List<BollingerBand> calculateBollingerBands(List<CryptoCoin> coins, int period) {
        List<BollingerBand> bands = new ArrayList<>();
        for (int i = 0; i < coins.size(); i++) {

            if (i + 1 < period) {
                bands.add(null);
                continue;
            }
            BigDecimal sum = BigDecimal.ZERO;
            for (int j = i + 1 - period; j <= i; j++) {
                sum = sum.add(coins.get(j).getClosePrice());
            }
            BigDecimal ma = sum.divide(BigDecimal.valueOf(period), 8, RoundingMode.HALF_UP);

            BigDecimal variance = BigDecimal.ZERO;
            for (int j = i + 1 - period; j <= i; j++) {
                BigDecimal diff = coins.get(j).getClosePrice().subtract(ma);
                variance = variance.add(diff.multiply(diff));
            }
            BigDecimal std = BigDecimal.valueOf(Math.sqrt(variance.divide(BigDecimal.valueOf(period), 8, RoundingMode.HALF_UP).doubleValue()));
            bands.add(new BollingerBand(ma.add(std.multiply(BigDecimal.valueOf(2))),
                    ma.subtract(std.multiply(BigDecimal.valueOf(2))),
                    ma));
        }
        return bands;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BollingerBand {
        private BigDecimal upper;
        private BigDecimal lower;
        private BigDecimal middle;

    }
}
