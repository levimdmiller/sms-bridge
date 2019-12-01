package ca.levimiller.smsbridge.service.impl;

import ca.levimiller.smsbridge.data.db.SecurityUserRepository;
import ca.levimiller.smsbridge.data.transformer.auth.SecurityUserTransformer;
import ca.levimiller.smsbridge.error.NotFoundException;
import javax.inject.Inject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DbUserDetailsService implements UserDetailsService {
  private final SecurityUserRepository userRepository;
  private final SecurityUserTransformer userTransformer;

  @Inject
  public DbUserDetailsService(SecurityUserRepository userRepository,
      SecurityUserTransformer userTransformer) {
    this.userRepository = userRepository;
    this.userTransformer = userTransformer;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(userTransformer::transformTo)
        .orElseThrow(() -> new NotFoundException("User not found for username: " + username));
  }
}
