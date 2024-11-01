package com.reactify.cache.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class CacheController {

    private final CachedDemo cachedDemo;

    @Autowired
    public CacheController(CachedDemo cachedDemo) {
        this.cachedDemo = cachedDemo;
    }

    @GetMapping("/cache/data")
    public Mono<List<String>> getDataCacheList() {
        return cachedDemo.getCachedDataList();
    }

    @GetMapping("/data")
    public Mono<List<String>> getDataList() {
        return cachedDemo.getListData();
    }
}