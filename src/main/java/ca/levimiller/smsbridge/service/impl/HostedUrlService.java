package ca.levimiller.smsbridge.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class HostedUrlService {

  /**
   * Gets the hosted url from the current context path.
   * @return - base url of app.
   */
  public String getBaseUrl() {
    return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
  }
}
