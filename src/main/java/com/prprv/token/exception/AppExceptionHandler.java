package com.prprv.token.exception;

import com.prprv.token.common.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * @author Yoooum
 */
@RestControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<R<Void>> handleTokenException(AppException e) {
        HttpStatus resolve = Optional.ofNullable(HttpStatus.resolve(e.getR().code())).orElse(HttpStatus.OK);
        return ResponseEntity.status(resolve.value()).body(e.getR());
    }
}
