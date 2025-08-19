package br.com.cneshub.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.TooManyRequests;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({ MethodArgumentNotValidException.class, ConstraintViolationException.class,
            IllegalArgumentException.class })
    public ResponseEntity<String> handleBadRequest(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler({ TooManyRequests.class, RequestNotPermitted.class })
    public ResponseEntity<String> handleTooMany(Exception ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests");
    }

    @ExceptionHandler({ WebClientResponseException.class, CallNotPermittedException.class, Exception.class })
    public ResponseEntity<String> handleBadGateway(Exception ex) {
        log.error("Erro ao consultar serviço upstream", ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Upstream error");
    }
}
