import com.technocorp.exception.RestExceptionHandler;
import com.technocorp.exception.StandardError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RestExceptionHandler.class})
class RestExceptionHandlerTest {

    @Autowired
    RestExceptionHandler exceptionHandler;

    @Test
    void shouldReturnPrettyException() {
        var stubExpected = StandardError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        var stubActual = exceptionHandler.handleGeneralExceptions(new Exception());
        assertEquals(stubExpected,stubActual);
    }

    @Test
    void shouldReturnPrettyRuntimeException() {
        var stubExpected = StandardError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error ocurred")
                .build();
        var stubActual = exceptionHandler.handleRuntimeExceptions(new RuntimeException());
        assertEquals(stubExpected,stubActual);
    }

    @Test
    void shouldReturnPrettyIoException() {
        var stubExpected = StandardError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Input/Output error")
                .build();
        var stubActual = exceptionHandler.handleIoExceptionExceptions(new IOException());
        assertEquals(stubExpected,stubActual);
    }

    @Test
    void shouldReturnPrettyStatusException() {
        var exception = new ResponseStatusException(HttpStatus.NOT_FOUND, "test");
        var stubExcpected = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .headers(HttpHeaders.EMPTY)
                .body(StandardError.builder()
                        .status(404)
                        .message(exception.getMessage())
                        .build());
        var stubActual = exceptionHandler
                .handleResponseStatusException(exception);
        assertEquals(stubExcpected, stubActual);
    }
}
