package com.msh.restdemo.config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;

@Configuration
@EnableCaching
public class CachingConfig {

	public CacheManager cacheManager(Ticker ticker) {
		CaffeineCache currencyServiceCache = buildCache("currencyService", ticker, 30);
		CaffeineCache currencyServiceLongCache = buildCache("currencyServiceLong", ticker, 360);
		CaffeineCache productServiceCache = buildCache("productService", ticker, 30);
		SimpleCacheManager manager = new SimpleCacheManager();
		manager.setCaches(Arrays.asList(currencyServiceCache, currencyServiceLongCache, productServiceCache));
		return manager;
	}

	private CaffeineCache buildCache(String name, Ticker ticker, int minutesToExpire) {
		return new CaffeineCache(name, Caffeine.newBuilder().expireAfterWrite(minutesToExpire, TimeUnit.MINUTES)
				.maximumSize(100).ticker(ticker).build());
	}

	@Bean
	public Ticker ticker() {
		return Ticker.systemTicker();
	}
}