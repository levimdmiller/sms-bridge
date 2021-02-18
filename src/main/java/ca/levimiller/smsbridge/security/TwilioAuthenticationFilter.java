package ca.levimiller.smsbridge.security;

import com.twilio.security.RequestValidator;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import liquibase.util.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * https://www.twilio.com/docs/usage/tutorials/how-to-secure-your-servlet-app-by-validating-incoming-twilio-requests
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Qualifier("twilioFilter")
public class TwilioAuthenticationFilter extends GenericFilterBean {

  private final RequestValidator requestValidator;

  @Inject
  public TwilioAuthenticationFilter(RequestValidator requestValidator) {
    this.requestValidator = requestValidator;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    boolean isValidRequest = false;
    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      HttpServletResponse httpResponse = (HttpServletResponse) response;

      // Concatenates the request URL with the query string
      String pathAndQueryUrl = getRequestUrlAndQueryString(httpRequest);
      // Extracts only the POST parameters and converts the parameters Map type
      Map<String, String> postParams = extractPostParams(httpRequest);
      String signatureHeader = httpRequest.getHeader("X-Twilio-Signature");
      if (StringUtils.isEmpty(signatureHeader)) {
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header needed");
        return;
      }

      isValidRequest = requestValidator.validate(
          pathAndQueryUrl,
          postParams,
          signatureHeader);

      if (!isValidRequest) {
        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Bad Authorization header");
        return;
      }
    }

    if (isValidRequest) {
      chain.doFilter(request, response);
    }
  }

  private Map<String, String> extractPostParams(HttpServletRequest request) {
    String queryString = request.getQueryString();
    Map<String, String[]> requestParams = request.getParameterMap();
    List<String> queryStringKeys = getQueryStringKeys(queryString);

    return requestParams.entrySet().stream()
        .filter(e -> !queryStringKeys.contains(e.getKey()))
        .collect(Collectors.toMap(Entry::getKey, e -> e.getValue()[0]));
  }

  private List<String> getQueryStringKeys(String queryString) {
    if (StringUtils.isEmpty(queryString)) {
      return Collections.emptyList();
    } else {
      return Arrays.stream(queryString.split("&"))
          .map(pair -> pair.split("=")[0])
          .collect(Collectors.toList());
    }
  }

  private String getRequestUrlAndQueryString(HttpServletRequest request) {
    String queryString = request.getQueryString();
    String requestUrl = request.getRequestURL().toString();
    if (!StringUtils.isEmpty(queryString)) {
      return requestUrl + "?" + queryString;
    }
    return requestUrl;
  }
}
