package ca.levimiller.smsbridge.error;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Slf4j
@Component
public class RestTemplateResponseErrorLoggerHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
    return (
        httpResponse.getStatusCode().series() == Series.CLIENT_ERROR
            || httpResponse.getStatusCode().series() == Series.SERVER_ERROR);
  }

  @Override
  public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
    log.error("Http Error - Code: {} - Reason: {}",
        clientHttpResponse.getStatusCode(),
        IOUtils.toString(clientHttpResponse.getBody(), StandardCharsets.UTF_8.name()));
  }
}
