package br.com.sicredi.coopvote.interceptor;

import br.com.sicredi.coopvote.util.LogUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    ensureRequestHasUuid(request);
    logStartRequest();
    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      @Nullable Exception ex) {
    logEndRequest();
  }

  private void ensureRequestHasUuid(HttpServletRequest request) {
    var requestUuid =
        Optional.ofNullable(request.getHeader("Uuid-Request")).orElseGet(this::generateRandomUuid);
    request.setAttribute("Uuid-Request", requestUuid);
  }

  private String generateRandomUuid() {
    return UUID.randomUUID().toString();
  }

  private void logStartRequest() {
    log.info(LogUtil.buildMessageStartRequest("Initiating Request"));
  }

  private void logEndRequest() {
    log.info(LogUtil.buildMessageEndRequest("Finalizing Request"));
  }
}
