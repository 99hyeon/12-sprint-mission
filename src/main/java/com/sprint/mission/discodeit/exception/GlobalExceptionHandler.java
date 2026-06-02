package com.sprint.mission.discodeit.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException exception) {
    ErrorResponse response = ErrorResponse.from(exception);

    return ResponseEntity
        .status(exception.getErrorCode().getStatus())
        .body(response);
  }

}
