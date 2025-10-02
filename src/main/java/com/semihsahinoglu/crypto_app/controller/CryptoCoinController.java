package com.semihsahinoglu.crypto_app.controller;

import com.semihsahinoglu.crypto_app.dto.CryptoCoinWithIndicatorsDTO;
import com.semihsahinoglu.crypto_app.entity.CryptoCoin;
import com.semihsahinoglu.crypto_app.service.CryptoCoinService;
import com.semihsahinoglu.crypto_app.service.CryptoIndicatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crypto")
public class CryptoCoinController {

    private final CryptoCoinService cryptoCoinService;
    private final CryptoIndicatorService cryptoIndicatorService;

    @PostMapping("/fetch")
    public ResponseEntity<List<CryptoCoin>> fetchCryptoData(@RequestParam String symbol,
                                                            @RequestParam String interval,
                                                            @RequestParam(required = false) String startDateTime,
                                                            @RequestParam(required = false) String endDateTime,
                                                            Pageable pageable) {
        ZoneId zone = ZoneId.systemDefault();

        LocalDateTime start = LocalDateTime.parse(startDateTime.trim());
        LocalDateTime end = LocalDateTime.parse(endDateTime.trim());

        long startTime = start.atZone(zone).toInstant().toEpochMilli();
        long endTime = end.atZone(zone).toInstant().toEpochMilli();

        List<CryptoCoin> coins = cryptoCoinService.fetchAndSaveCoin(symbol, interval, startTime, endTime);
        return ResponseEntity.ok().body(coins);
    }

    @GetMapping("/get")
    public ResponseEntity<Page<CryptoCoin>> getAllCryptoCoins(@PageableDefault(size = 30) Pageable pageable) {
        Page<CryptoCoin> coins = cryptoCoinService.getAllCryptoCoins(pageable);

        return ResponseEntity.ok().body(coins);
    }

    @GetMapping("/get/{symbol}")
    public ResponseEntity<Page<CryptoCoin>> getCryptoCoin(@PathVariable String symbol, @PageableDefault(size = 30, sort = "openTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CryptoCoin> coins = cryptoCoinService.getCryptoCoinsBySymbol(symbol, pageable);

        return ResponseEntity.ok().body(coins);
    }

    @GetMapping("/get/{symbol}/with-indicators")
    public ResponseEntity<CryptoCoinWithIndicatorsDTO> getCryptoCoinWithIndicators(@PathVariable String symbol,
                                                                                   @RequestParam(required = false, defaultValue = "5") int period,
                                                                                   @PageableDefault(size = 30, sort = "openTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CryptoCoin> coins = cryptoCoinService.getCryptoCoinsBySymbol(symbol, pageable);
        Map<String, Object> indicators = cryptoIndicatorService.calculateIndicators(coins.getContent(), period);

        CryptoCoinWithIndicatorsDTO cryptoCoinWithIndicatorsDTO = new CryptoCoinWithIndicatorsDTO(coins.getContent(), indicators);

        return ResponseEntity.ok().body(cryptoCoinWithIndicatorsDTO);
    }

    @GetMapping("/get/indicators")
    public ResponseEntity<Map<String, Object>> getIndicators(
            @RequestParam String symbol,
            @RequestParam(required = false, defaultValue = "5") int period,
            @PageableDefault(size = 30, sort = "openTime", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<CryptoCoin> coins = cryptoCoinService.getCryptoCoinsBySymbol(symbol, pageable);
        Map<String, Object> indicators = cryptoIndicatorService.calculateIndicators(coins.getContent(), period);

        return ResponseEntity.ok().body(indicators);
    }

    @DeleteMapping("/delete/{symbol}")
    public ResponseEntity<?> deleteCrypto(@PathVariable String symbol) {
        this.cryptoCoinService.deleteCryptoCoinBySymbol(symbol);

        return ResponseEntity.ok().body(symbol + " verileri silindi...");
    }
}
