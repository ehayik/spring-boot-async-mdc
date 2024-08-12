package io.github.ehayik.kata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @PostMapping
    CompletableFuture<ResponseEntity<User>> createUserAsync(@RequestBody User newUser) {
        log.info("Creating user {}.", newUser.username());
        return userService.createAsync(newUser).thenApply(user -> ResponseEntity.status(CREATED).body(user));
    }
}
