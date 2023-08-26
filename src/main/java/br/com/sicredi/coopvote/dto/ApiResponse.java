package br.com.sicredi.coopvote.dto;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
  private final int status;
  private final String message;
  private final T content;

  public ApiResponse(int status, String message, T content) {
    this.status = status;
    this.message = message;
    this.content = content;
  }
}
