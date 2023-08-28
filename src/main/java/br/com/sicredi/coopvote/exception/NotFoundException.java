package br.com.sicredi.coopvote.exception;

import java.io.Serializable;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

  private final String messageKey;
  private final Serializable[] args;

  public NotFoundException(String messageKey, Serializable... args) {
    this.messageKey = messageKey;
    this.args = args;
  }
}
