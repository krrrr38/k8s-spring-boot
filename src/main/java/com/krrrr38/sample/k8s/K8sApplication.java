package com.krrrr38.sample.k8s;

import java.time.Duration;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class K8sApplication {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        SpringApplication.run(K8sApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> routes() {
        return RouterFunctions
                .route(RequestPredicates.path("/hello"), req -> {
                    logger.info("hello: request = {}", req);
                    return ServerResponse.ok().body(Flux.just("hello world"), String.class);
                })
                .andRoute(RequestPredicates.GET("/stream"), req -> {
                    int limit = req.queryParam("limit").map(Integer::parseInt).orElse(100);
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_STREAM_JSON).body(
                            Flux.fromStream(IntStream.iterate(0, i -> i + 1).boxed().limit(limit))
                                .delayElements(Duration.ofSeconds(1)),
                            Integer.class
                    );
                })
                .andRoute(RequestPredicates.path("/health"), req -> {
                    return ServerResponse.ok().body(Flux.just("ok"), String.class);
                });
    }
}
