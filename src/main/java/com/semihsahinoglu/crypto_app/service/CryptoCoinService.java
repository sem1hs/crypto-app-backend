package com.semihsahinoglu.crypto_app.service;

import com.semihsahinoglu.crypto_app.client.BinanceClient;
import com.semihsahinoglu.crypto_app.entity.CryptoCoin;
import com.semihsahinoglu.crypto_app.repository.ICryptoCoinRepository;
import com.semihsahinoglu.crypto_app.util.CryptoCoinHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoCoinService {

    private final BinanceClient binanceClient;
    private final ICryptoCoinRepository cryptoCoinRepository;

    @Transactional
    public List<CryptoCoin> fetchAndSaveCoin(String symbol, String interval, long startTime, long endTime) {
        List<List<Object>> cryptoCoinKline = binanceClient.getCrypto(symbol, interval, startTime, endTime).block();
        List<CryptoCoin> coins = new ArrayList<>();

        if (cryptoCoinKline == null) return null;

        for (List<Object> kline : cryptoCoinKline) {
            LocalDateTime openTime = CryptoCoinHelper.toLocalDateTime(kline.get(0));
            boolean exists = cryptoCoinRepository.existsBySymbolAndOpenTime(symbol, openTime);
            if (exists) continue;

            CryptoCoin entity = CryptoCoin.builder()
                    .symbol(symbol)
                    .openTime(openTime)
                    .openPrice(CryptoCoinHelper.toBigDecimal(kline.get(1)))
                    .highPrice(CryptoCoinHelper.toBigDecimal(kline.get(2)))
                    .lowPrice(CryptoCoinHelper.toBigDecimal(kline.get(3)))
                    .closePrice(CryptoCoinHelper.toBigDecimal(kline.get(4)))
                    .volume(CryptoCoinHelper.toBigDecimal(kline.get(5)))
                    .closeTime(CryptoCoinHelper.toLocalDateTime(kline.get(6)))
                    .quoteVolume(CryptoCoinHelper.toBigDecimal(kline.get(7)))
                    .numberOfTrades(((Number) kline.get(8)).intValue())
                    .takerBuyBaseAssetVolume(CryptoCoinHelper.toBigDecimal(kline.get(9)))
                    .takerBuyQuoteAssetVolume(CryptoCoinHelper.toBigDecimal(kline.get(10)))
                    .interval(interval)
                    .build();

            coins.add(entity);
            cryptoCoinRepository.save(entity);
        }
        return coins;
    }

    public Page<CryptoCoin> getCryptoCoinsBySymbol(String symbol, String interval, Pageable pageable) {
        return cryptoCoinRepository.findAllBySymbolAndInterval(symbol, interval, pageable);
    }

    public Page<CryptoCoin> getAllCryptoCoins(Pageable pageable) {
        return cryptoCoinRepository.findAll(pageable);
    }

    @Transactional
    public void deleteCryptoCoinBySymbol(String symbol) {
        this.cryptoCoinRepository.deleteAllBySymbol(symbol);
    }
}
