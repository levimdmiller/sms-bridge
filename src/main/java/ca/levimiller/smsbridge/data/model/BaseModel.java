package ca.levimiller.smsbridge.data.model;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseModel implements Serializable {

  @Id
  @OrderBy
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "deleted", nullable = false)
  private Boolean deleted;

  @Column(name = "created_date", nullable = false)
  private Instant createdDate;

  @Column(name = "modified_date", nullable = false)
  private Instant modifiedDate;

  @PrePersist
  private void setDefaults() {
    if (deleted == null) {
      deleted = false;
    }
  }
}
