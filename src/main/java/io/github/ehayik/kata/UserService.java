package io.github.ehayik.kata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
class UserService {

    @Async
    public CompletableFuture<User> createAsync(User newUser) {
        log.info("Simulating a slow, time-consuming process to create user {}.", newUser.username());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
           log.debug(ex.getMessage(), ex);
           Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(newUser);
    }
}
