package ca.levimiller.smsbridge.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "location")
@SQLDelete(sql = "UPDATE location SET deleted = 1 WHERE id = ?;",
    check = ResultCheckStyle.COUNT)
@Where(clause = "deleted <> 1")
public class Location extends BaseModel {

  @Id
  @OrderBy
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Size(max = 255)
  @Column(name = "city")
  private String city;

  @Size(max = 255)
  @Column(name = "state")
  private String state;

  @Size(max = 255)
  @Column(name = "country")
  private String country;

  @Size(max = 6)
  @Column(name = "zip")
  private String zip;
}
