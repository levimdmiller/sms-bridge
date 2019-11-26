package ca.levimiller.smsbridge.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.util.UriComponentsBuilder;

public class TokenAuthenticationInterceptor implements ClientHttpRequestInterceptor {

  private final String token;

  public TokenAuthenticationInterceptor(String token) {
    this.token = token;
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    ParamHttpRequestWrapper requestWrapper = new ParamHttpRequestWrapper(request);
    requestWrapper.addParam("access_token", token);
    return execution.execute(requestWrapper, body);
  }

  static class ParamHttpRequestWrapper extends HttpRequestWrapper {

    private UriComponentsBuilder uriBuilder;

    ParamHttpRequestWrapper(HttpRequest request) {
      super(request);
      this.uriBuilder = UriComponentsBuilder.fromUri(request.getURI());
    }

    void addParam(String key, String value) throws UnsupportedEncodingException {
      uriBuilder.replaceQueryParam(key, URLEncoder.encode(value, "UTF-8"));
    }

    @Override
    public URI getURI() {
      // mark as encoded already, as restTemplate will encode the request already
      return uriBuilder.build(true).toUri();
    }
  }
}
