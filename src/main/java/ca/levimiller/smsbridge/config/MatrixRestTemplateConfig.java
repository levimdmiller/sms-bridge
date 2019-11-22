package ca.levimiller.smsbridge.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class MatrixRestTemplateConfig {
  private final String matrixUrl;
  private final String matrixToken;

  public MatrixRestTemplateConfig(
      @Value("${matrix.url}") String matrixUrl,
      @Value("${matrix.as_token}") String matrixToken) {
    this.matrixUrl = matrixUrl;
    this.matrixToken = matrixToken;
  }

  @Bean
  @Qualifier("matrixTemplate")
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    UriBuilder uriBuilder = UriComponentsBuilder.fromUriString(matrixUrl)
        .path("/_matrix/client/r0");
    return builder
        .rootUri(uriBuilder.build().toString())
        .interceptors(new TokenAuthenticationInterceptor(matrixToken))
        .build();
  }
}
