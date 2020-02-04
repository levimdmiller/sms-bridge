package ca.levimiller.smsbridge.data.transformer.auth;

import ca.levimiller.smsbridge.data.model.auth.SecurityUser;
import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper(componentModel = "spring")
public interface SecurityUserTransformer {

  /**
   * Transforms a sms-bridge SecurityUser to a spring UserDetails.
   *
   * @param user - user to transform
   * @return - spring user object
   */
  default UserDetails transformTo(SecurityUser user) {
    boolean deleted = user.getDeleted() != null && user.getDeleted();
    return User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .accountExpired(deleted)
        .accountLocked(deleted)
        .credentialsExpired(deleted)
        .disabled(deleted)
        .roles(user.getRole().name())
        .build();
  }
}
