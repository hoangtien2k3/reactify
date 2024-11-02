package com.reactify.cache.demo;

import com.reactify.cache.LocalCache;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class CachedDemo {

    @LocalCache(durationInMinute = 10, maxRecord = 100, autoCache = true)
    public Mono<List<String>> getCachedDataList() {
        return Mono.fromCallable(this::fetchDataFromDatabase);
    }

    public Mono<List<String>> getListData() {
        return Mono.fromSupplier(this::fetchDataFromDatabase);
    }

    private List<String> fetchDataFromDatabase() {
        try {
            //blocking
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<String> data = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            data.add("Data " + i);
        }
        return data;
    }

}