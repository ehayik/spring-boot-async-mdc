package io.github.ehayik.kata;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
class UserService {

    @Async
    @SneakyThrows
    CompletableFuture<User> createAsync(User newUser) {
        log.info("Simulating a slow, time-consuming process to create user {}.", newUser.username());
        Thread.sleep(2000);
        return CompletableFuture.completedFuture(newUser);
    }
}
