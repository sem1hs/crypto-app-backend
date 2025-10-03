package com.semihsahinoglu.crypto_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "crypto_coin")
public class CryptoCoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String symbol;

    @Column(name = "open_time")
    private LocalDateTime openTime;

    @Column(name = "close_time")
    private LocalDateTime closeTime;

    @Column(name = "open_price")
    private BigDecimal openPrice;

    @Column(name = "high_price")
    private BigDecimal highPrice;

    @Column(name = "low_price")
    private BigDecimal lowPrice;

    @Column(name = "close_price")
    private BigDecimal closePrice;

    @Column
    private BigDecimal volume;

    @Column(name = "quote_volume")
    private BigDecimal quoteVolume;

    @Column(name = "number_of_trades")
    private Integer numberOfTrades;

    @Column(name = "taker_buy_base_asset_volume")
    private BigDecimal takerBuyBaseAssetVolume;

    @Column(name = "taker_buy_quote_asset_volume")
    private BigDecimal takerBuyQuoteAssetVolume;

    @Column(name = "interval")
    private String interval;
}
