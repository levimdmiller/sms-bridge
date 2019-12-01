package ca.levimiller.smsbridge.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ca.levimiller.smsbridge.error.ForbiddenException;
import ca.levimiller.smsbridge.error.UnauthorizedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import com.twilio.security.RequestValidator;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@SpringBootTest
class TwilioAuthenticationFilterTest {
  @MockBean
  private RequestValidator requestValidator;
  private final Filter twilioAuthenticationFilter;

  private MockHttpServletRequest request;
  private ServletResponse response;
  private FilterChain filterChain;

  @Autowired
  TwilioAuthenticationFilterTest(
      @Qualifier("twilioFilter") Filter twilioAuthenticationFilter) {
    this.twilioAuthenticationFilter = twilioAuthenticationFilter;
  }

  @BeforeEach
  void setUp() throws URISyntaxException, JsonProcessingException {
    request = new MockHttpServletRequest("POST", "/endpoint");
    request.addHeader("X-Twilio-Signature", "twilio-hash");
    request.setParameter("param1", "query-param");
    request.setParameter("param2", "post-param");
    request.setQueryString("param1=query-param");

    response = new MockHttpServletResponse();
    filterChain = mock(FilterChain.class);

    when(requestValidator.validate("http://localhost/endpoint?param1=query-param",
        ImmutableMap.of("param2", "post-param"), "twilio-hash"))
        .thenReturn(true);
  }

  @Test
  void testNotHttpServletRequest() throws IOException, ServletException {
    assertThrows(ForbiddenException.class, () -> twilioAuthenticationFilter.doFilter(
            new ServletRequestWrapper(request), response, filterChain));
    verify(filterChain, times(0)).doFilter(request, response);
  }

  @Test
  void testNoSignature() throws IOException, ServletException {
    request.removeHeader("X-Twilio-Signature");
    assertThrows(UnauthorizedException.class, () -> twilioAuthenticationFilter.doFilter(
        request, response, filterChain));
    verify(filterChain, times(0)).doFilter(request, response);
  }

  @Test
  void testSuccess() throws IOException, ServletException {
    twilioAuthenticationFilter.doFilter(request, response, filterChain);
    verify(requestValidator, times(1))
        .validate("http://localhost/endpoint?param1=query-param",
        ImmutableMap.of("param2", "post-param"), "twilio-hash");
    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  void testSuccess_NoQueryString() throws IOException, ServletException {
    request.setQueryString(null);
    request.removeAllParameters();
    when(requestValidator.validate("http://localhost/endpoint",
        Collections.emptyMap(), "twilio-hash"))
        .thenReturn(true);

    twilioAuthenticationFilter.doFilter(request, response, filterChain);
    verify(requestValidator, times(1))
        .validate("http://localhost/endpoint",
            Collections.emptyMap(), "twilio-hash");
    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  void testNotValid() throws IOException, ServletException {
    request.setScheme("https");

    assertThrows(ForbiddenException.class, () ->
        twilioAuthenticationFilter.doFilter(request, response, filterChain));
    verify(requestValidator, times(1))
        .validate("https://localhost:80/endpoint?param1=query-param",
            ImmutableMap.of("param2", "post-param"), "twilio-hash");
    verify(filterChain, times(0)).doFilter(request, response);
  }
}
