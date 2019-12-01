package ca.levimiller.smsbridge.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.db.SecurityUserRepository;
import ca.levimiller.smsbridge.data.model.auth.SecurityUser;
import ca.levimiller.smsbridge.data.transformer.auth.SecurityUserTransformer;
import ca.levimiller.smsbridge.error.NotFoundException;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootTest
class DbUserDetailsServiceTest {
  @MockBean
  private SecurityUserRepository userRepository;
  @MockBean
  private SecurityUserTransformer userTransformer;

  private final UserDetailsService userDetailsService;
  private String username = "username";

  @Autowired
  DbUserDetailsServiceTest(
      @Qualifier("userDetailsService") UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Test
  void loadUserByUsername_notFound() {
    when(userRepository.findByUsername(username))
        .thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
  }

  @Test
  void loadUserByUsername() {
    SecurityUser securityUser = new SecurityUser();
    UserDetails userDetails = new User(
        "username","password", Collections.emptyList());
    when(userRepository.findByUsername(username))
        .thenReturn(Optional.of(securityUser));
    when(userTransformer.transformTo(securityUser))
        .thenReturn(userDetails);

    assertEquals(userDetails, userDetailsService.loadUserByUsername(username));
  }
}
