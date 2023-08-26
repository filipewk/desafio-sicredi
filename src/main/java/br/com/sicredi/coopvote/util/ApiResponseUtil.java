package br.com.sicredi.coopvote.util;

import br.com.sicredi.coopvote.record.ApiResult;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseUtil {

  private final MessageSource messageSource;

  public ApiResponseUtil(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public <T> ResponseEntity<ApiResult<T>> createdResponse(T data) {
    var response =
        new ApiResult<>(HttpStatus.CREATED.value(), getMessage("response.api.created"), data);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  private String getMessage(String message) {
    return messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
  }
}
