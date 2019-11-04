package ca.levimiller.smsbridge.data.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "message")
@SQLDelete(sql = "UPDATE message SET deleted = 1 WHERE id = ?;",
    check = ResultCheckStyle.COUNT)
@Where(clause = "deleted <> 1")
public class Message extends BaseModel {

  @Id
  @OrderBy
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  private String uuid;

  @Column(name = "body")
  private String body;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "to")
  private Contact to;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "from")
  private Contact from;

  @OneToMany(fetch = FetchType.LAZY)
  private List<Media> media;
}
