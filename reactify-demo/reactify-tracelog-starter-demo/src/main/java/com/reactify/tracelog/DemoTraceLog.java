package com.reactify.tracelog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DemoTraceLog {

    @LogPerformance(logType = "API", actionType = "GET", logOutput = true, logInput = true, title = "Lấy dữ liệu ví dụ")
    @GetMapping("")
    public Mono<String> helloVietNam() {
        return Mono.just("Xin Chao Viet Nam");
    }

    @GetMapping("/list")
    public Mono<List<String>> getListData() {
        return Mono.fromSupplier(this::fetchDataFromDatabase);
    }

    private List<String> fetchDataFromDatabase() {
        List<String> data = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            data.add("Data " + i);
        }
        return data;
    }

}
