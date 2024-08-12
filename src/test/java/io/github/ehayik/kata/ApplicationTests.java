package io.github.ehayik.kata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ApplicationTests {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldCreateUser(CapturedOutput capturedOutput) {
        // Given
        var traceId = randomUUID().toString();
        var newUser = new User("Yolo");

        // When
        webTestClient.post().uri("/users")
                .contentType(APPLICATION_JSON)
                .header(TRACE_ID_HEADER, traceId)
                .bodyValue(newUser)
                .exchange()
                // Then
                .expectStatus().isCreated()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody(User.class)
                .isEqualTo(newUser);

        // Then
        assertThat(capturedOutput.getOut())
                .contains("Simulating a slow, time-consuming process to create user Yolo. TraceId=" + traceId);
    }

}
