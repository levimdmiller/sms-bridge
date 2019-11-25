package ca.levimiller.smsbridge.data.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "number_registry")
@SQLDelete(sql = "UPDATE number_registry SET deleted = 1 WHERE id = ?;",
    check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class NumberRegistration extends BaseModel {

  @Size(max = 255)
  @Column(name = "owner_id")
  private String ownerId;

  @Enumerated(EnumType.STRING)
  @Column(name = "registration_type", length = 16)
  private NumberRegistrationType registrationType;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "contact")
  private Contact contact;
}