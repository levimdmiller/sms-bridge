package ca.levimiller.smsbridge.data.transformer.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.auth.SecurityUser;
import com.google.common.collect.Iterables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
class SecurityUserTransformerTest {
  private final Fixture<SecurityUser> fixture;
  private final SecurityUserTransformer transformer;

  private SecurityUser user;

  @Autowired
  SecurityUserTransformerTest(
      Fixture<SecurityUser> fixture,
      SecurityUserTransformer transformer) {
    this.fixture = fixture;
    this.transformer = transformer;
  }

  @BeforeEach
  void setUp() {
    user = fixture.create();
    user.setDeleted(false);
  }

  @Test
  void transformTo() {
    UserDetails result = transformer.transformTo(user);
    assertEquals(user.getUsername(), result.getUsername());
    assertEquals(user.getPassword(), result.getPassword());
    assertEquals(user.getDeleted(), !result.isAccountNonExpired());
    assertEquals(user.getDeleted(), !result.isAccountNonLocked());
    assertEquals(user.getDeleted(), !result.isCredentialsNonExpired());
    assertEquals(user.getDeleted(), !result.isEnabled());

    assertEquals(1, result.getAuthorities().size());
    GrantedAuthority authority = Iterables.get(result.getAuthorities(), 0);
    assertEquals("ROLE_" + user.getRole().toString(), authority.getAuthority());
  }
}
