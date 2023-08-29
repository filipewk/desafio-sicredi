package br.com.sicredi.coopvote.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageBuilder {

  private final Map<String, Object> keyValueMap = new HashMap<>();
  protected StringBuilder stringBuilder = new StringBuilder();

  public MessageBuilder add(String key, Object value) {
    keyValueMap.put(key, value);
    return this;
  }

  public MessageBuilder and() {
    this.stringBuilder.append(Constants.AND_KEY);
    return this;
  }

  public MessageBuilder method(Object value) {
    return add(Constants.METHOD_KEY, value);
  }

  public MessageBuilder message(Object value) {
    return add(Constants.MESSAGE_KEY, value);
  }

  public MessageBuilder uuidRequest(Object value) {
    return add(Constants.UUID_REQUEST, value);
  }

  public MessageBuilder appName(Object value) {
    return add(Constants.APP_NAME, value);
  }

  public MessageBuilder dateStart(Object value) {
    return add(Constants.DATE_START, value);
  }

  public MessageBuilder dateEnd(Object value) {
    return add(Constants.DATE_END, value);
  }

  public MessageBuilder payload(Object value) {
    return add(Constants.PAYLOAD_VALUE, value);
  }

  public MessageBuilder uri(Object value) {
    return add(Constants.URI_VALUE, value);
  }

  public String build() {
    return keyValueMap.entrySet().stream()
        .map(entry -> entry.getKey() + Constants.EQUALS_KEY + entry.getValue())
        .collect(Collectors.joining(Constants.AND_KEY));
  }
}
