package br.com.sicredi.coopvote.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LogUtil {

    private static final String FORMAT_DATE_TIME = "dd/MM/yyyy HH:mm:ss.SSS";

    public static String buildMessage(final String message) {
        return initMessageBuilder()
                .message(message)
                .build();
    }

    public static String buildMessagePayload(final String payload) {
        return initMessageBuilderWithUriAndMethod()
                .payload((payload))
                .build();
    }

    public static String buildMessageStartRequest(final String message) {
        return initMessageBuilderWithUriAndMethod()
                .dateStart(getCurrentDateTime()).and()
                .message(message)
                .build();
    }

    public static String buildMessageEndRequest(final String message) {
        return initMessageBuilderWithUriAndMethod()
                .dateEnd(getCurrentDateTime()).and()
                .message(message)
                .build();
    }

    private static MessageBuilder initMessageBuilder() {
        return new MessageBuilder()
                .uuidRequest(RequestUtil.getUuidRequest()).and()
                .appName("CoopVote");
    }

    private static MessageBuilder initMessageBuilderWithUriAndMethod() {
        return initMessageBuilder()
                .uri(RequestUtil.getUri()).and()
                .method(RequestUtil.getMethod());
    }

    private static String getCurrentDateTime() {
        var formatterLocalDateTime = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME);
        return formatterLocalDateTime.format(LocalDateTime.now());
    }
}
