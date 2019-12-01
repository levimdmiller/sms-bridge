package ca.levimiller.smsbridge.data.model.auth;

import ca.levimiller.smsbridge.data.model.BaseModel;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "security_user")
@SQLDelete(sql = "UPDATE security_user SET deleted = 1 WHERE id = ?;")
@Where(clause = "deleted = false")
public class SecurityUser extends BaseModel {

  @Size(max = 255)
  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Size(max = 255)
  @Column(name = "password")
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", length = 16)
  private SecurityRole role;
}
