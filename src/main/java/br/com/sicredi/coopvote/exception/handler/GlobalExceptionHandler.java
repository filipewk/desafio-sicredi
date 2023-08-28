package br.com.sicredi.coopvote.exception.handler;

import br.com.sicredi.coopvote.exception.NotFoundException;
import br.com.sicredi.coopvote.exception.ValidationException;
import br.com.sicredi.coopvote.record.ApiResult;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageSource messageSource;

  public GlobalExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
    return handleException(
        getMessage(ex.getMessage()), ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Object> handleValidationException(
      ValidationException ex, WebRequest request) {
    var message =
        messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), LocaleContextHolder.getLocale());
    return handleException(message, ex, request, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
    var message =
        messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), LocaleContextHolder.getLocale());
    return handleException(message, ex, request, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    return handleException(getMessage(ex.getMessage()), ex, request, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<Object> handleException(
      String message, Exception ex, WebRequest request, HttpStatus httpStatus) {
    var error = new ApiResult<>(true, httpStatus.value(), message, null);
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return handleExceptionInternal(ex, error, headers, httpStatus, request);
  }

  private String getMessage(String key) {
    try {
      return messageSource.getMessage(key, new Object[] {key}, LocaleContextHolder.getLocale());
    } catch (Exception ex) {
      return key;
    }
  }
}
