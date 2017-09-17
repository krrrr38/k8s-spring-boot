package com.krrrr38.sample.k8s;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Repository
public class JdbcRepository {
    private final JdbcTemplate jdbcTemplate;
    private final Scheduler jdbcScheduler;

    public JdbcRepository(JdbcTemplate jdbcTemplate, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcScheduler = jdbcScheduler;
    }

    public Mono<Integer> selectOne() {
        return async(() -> jdbcTemplate.queryForObject("SELECT 1", Integer.class));
    }

    private <T> Mono<T> async(Callable<T> callable) {
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }
}
