package ca.levimiller.smsbridge.data.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
@Table(name = "message")
@SQLDelete(sql = "UPDATE message SET deleted = 1 WHERE id = ?;",
    check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Message extends BaseModel {

  @Size(max = 255)
  @Column(name = "uid", unique = true)
  private String uid;

  @Column(name = "body")
  private String body;

  @Valid
  @NotNull
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "to_contact")
  private Contact toContact;

  @Valid
  @NotNull
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "from_contact")
  private Contact fromContact;

  @Valid
  @OneToMany(mappedBy = "message", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Media> media;
}
