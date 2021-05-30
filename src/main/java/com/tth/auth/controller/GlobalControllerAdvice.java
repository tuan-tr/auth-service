package com.tth.auth.controller;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tth.auth.dto.ExceptionResponseBody;
import com.tth.auth.exception.CustomException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

  @Autowired
  private ObjectMapper objectMapper;

  // TODO keep or not
  // @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handle(IllegalArgumentException e, WebRequest request) {
    ExceptionResponseBody body = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getClass().getSimpleName())
        .message(e.getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(body));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> handle(CustomException e, WebRequest request) {
    ExceptionResponseBody body = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(e.getHttpStatus().value())
        .error(e.getClass().getSimpleName())
        .detail(e.getDetail())
        .message(e.getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(body));
    return ResponseEntity.status(e.getHttpStatus()).body(body);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<?> handle(NoHandlerFoundException e, WebRequest request) {
    ExceptionResponseBody body = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.NOT_FOUND.value())
        .error(e.getClass().getSimpleName())
        .message(e.getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(body));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<?> handle(AuthenticationException e, WebRequest request) {
    String error = e.getClass().getSimpleName();
    if (ObjectUtils.isEmpty(error)) { // handle from TokenAuthenticationFilter
      error = e.getCause().getClass().getSimpleName();
    }

    ExceptionResponseBody body = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.UNAUTHORIZED.value())
        .error(error)
        .message(e.getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(body));
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<?> handle(BindException e, WebRequest request) {
    ExceptionResponseBody body = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getClass().getSimpleName())
        .detail(e.getFieldErrors())
        .build();

    log.warn("Exception: {}", convertToJson(body));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<?> handle(MethodArgumentTypeMismatchException e, WebRequest request) {
    ExceptionResponseBody body = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getErrorCode())
        .message(e.getRootCause().getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(body));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  // @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handle(MethodArgumentNotValidException e, WebRequest request) {
    ExceptionResponseBody body = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getClass().getSimpleName())
        .message(e.getMessage())
        .detail(e.getBindingResult().getAllErrors())
        .build();

    log.warn("Exception: {}", convertToJson(body));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  // @ExceptionHandler(InvalidFormatException.class)
  public ResponseEntity<?> handle(InvalidFormatException e, WebRequest request) {
    ExceptionResponseBody body = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getClass().getSimpleName())
        .message(e.getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(body));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  // @ExceptionHandler(JsonParseException.class)
  public ResponseEntity<?> handle(JsonParseException e, WebRequest request) {
    ExceptionResponseBody body = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getClass().getSimpleName())
        .message(e.getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(body));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  private String convertToJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("ObjectMapper failed to convert the object to string", e);
    }
  }

}
