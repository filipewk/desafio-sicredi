package br.com.sicredi.coopvote.interceptor;

import br.com.sicredi.coopvote.util.LogUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

@Slf4j
@ControllerAdvice
public class LoggingPayloadAdviceInterceptor extends RequestBodyAdviceAdapter {

  private final ObjectMapper objectMapper;

  public LoggingPayloadAdviceInterceptor(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public boolean supports(
      MethodParameter methodParameter,
      Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object afterBodyRead(
      Object body,
      HttpInputMessage inputMessage,
      MethodParameter parameter,
      Type targetType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    logPayload(body);
    return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
  }

  private void logPayload(Object payload) {
    try {
      var payloadJson = objectMapper.writeValueAsString(payload);
      log.info(buildPayloadMessage(payloadJson));
    } catch (JsonProcessingException e) {
      log.info(buildFailedMessage());
    }
  }

  private String buildPayloadMessage(String payloadJson) {
    return LogUtil.buildMessagePayload(payloadJson);
  }

  private String buildFailedMessage() {
    return LogUtil.buildMessage("Failed to show payload");
  }
}
