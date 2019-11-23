package ca.levimiller.smsbridge.config;

import ca.levimiller.smsbridge.error.RestTemplateResponseErrorLoggerHandler;
import com.google.common.net.InternetDomainName;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@ConfigurationProperties(prefix = "matrix")
@Getter
@Setter
public class MatrixConfig {

  /**
   * Url to Matrix server.
   */
  private String url;
  /**
   * Token for Authorization on Matrix server.
   */
  private String asToken;

  // https://stackoverflow.com/questions/51926704/why-is-guavas-eventbus-marked-unstable-in-intellij-2018-2
  @SuppressWarnings("UnstableApiUsage")
  public String getDomain() {
    // Ignore subdomain as matrix throws a 500 if included
    return InternetDomainName.from(
        Objects.requireNonNull(
            UriComponentsBuilder.fromHttpUrl(url)
            .build()
            .getHost())
    ).topPrivateDomain().toString();
  }

  @Bean
  @Qualifier("matrixTemplate")
  public RestTemplate restTemplate(
      RestTemplateResponseErrorLoggerHandler errorLoggerHandler,
      RestTemplateBuilder builder) {
    UriBuilder uriBuilder = UriComponentsBuilder.fromUriString(url)
        .path("/_matrix/client/r0");
    return builder
        .rootUri(uriBuilder.build().toString())
        .interceptors(new TokenAuthenticationInterceptor(asToken))
        .errorHandler(errorLoggerHandler)
        .build();
  }
}
