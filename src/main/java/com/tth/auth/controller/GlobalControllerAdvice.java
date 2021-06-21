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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class GlobalControllerAdvice {

  @Autowired
  private ObjectMapper objectMapper;

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> handle(CustomException e, WebRequest request) {
    ExceptionResponseBody response = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(e.getHttpStatus().value())
        .error(e.getClass().getSimpleName())
        .detail(e.getDetail())
        .message(e.getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(response));
    return ResponseEntity.status(e.getHttpStatus()).body(response);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ExceptionResponseBody handle(NoHandlerFoundException e, WebRequest request) {
    ExceptionResponseBody response = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.NOT_FOUND.value())
        .error(e.getClass().getSimpleName())
        .message(e.getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(response));
    return response;
  }
  
  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ExceptionResponseBody handle(AuthenticationException e, WebRequest request) {
    String error = e.getClass().getSimpleName();
    if (ObjectUtils.isEmpty(error)) { // handle from TokenAuthenticationFilter
      error = e.getCause().getClass().getSimpleName();
    }

    ExceptionResponseBody response = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.UNAUTHORIZED.value())
        .error(error)
        .message(e.getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(response));
    return response;
  }

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponseBody handle(BindException e, WebRequest request) {
    ExceptionResponseBody response = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getClass().getSimpleName())
        .detail(e.getFieldErrors())
        .build();

    log.warn("Exception: {}", convertToJson(response));
    return response;
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponseBody handle(MethodArgumentTypeMismatchException e, WebRequest request) {
    ExceptionResponseBody response = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getErrorCode())
        .message(e.getRootCause().getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(response));
    return response;
  }

  // @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponseBody handle(MethodArgumentNotValidException e, WebRequest request) {
    ExceptionResponseBody response = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getClass().getSimpleName())
        .message(e.getMessage())
        .detail(e.getBindingResult().getAllErrors())
        .build();

    log.warn("Exception: {}", convertToJson(response));
    return response;
  }

  // @ExceptionHandler(InvalidFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponseBody handle(InvalidFormatException e, WebRequest request) {
    ExceptionResponseBody response = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getClass().getSimpleName())
        .message(e.getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(response));
    return response;
  }

  // @ExceptionHandler(JsonParseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponseBody handle(JsonParseException e, WebRequest request) {
    ExceptionResponseBody response = ExceptionResponseBody.builder()
        .timestamp(OffsetDateTime.now())
        .path(request.getDescription(false))
        .status(HttpStatus.BAD_REQUEST.value())
        .error(e.getClass().getSimpleName())
        .message(e.getMessage())
        .build();

    log.warn("Exception: {}", convertToJson(response));
    return response;
  }

  private String convertToJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("ObjectMapper failed to convert the object to string", e);
    }
  }

}
