package br.com.sicredi.coopvote.util;

import br.com.sicredi.coopvote.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@UtilityClass
public class RequestUtil {

  public HttpServletRequest getCurrentRequest() {
    try {
      var servletRequestAttributes =
          (ServletRequestAttributes)
              Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
      return servletRequestAttributes.getRequest();
    } catch (Exception ex) {
      throw new ValidationException("response.api.request.failed");
    }
  }

  public static String getUri() {
    return getCurrentRequest().getRequestURI();
  }

  public static String getMethod() {
    return getCurrentRequest().getMethod();
  }

  public static String getUuidRequest() {
    var uuid = getCurrentRequest().getAttribute("Uuid-Request");
    return uuid != null ? uuid.toString() : null;
  }
}
