package ca.levimiller.smsbridge.data.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "media")
@SQLDelete(sql = "UPDATE media SET deleted = 1 WHERE id = ?;",
    check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Media extends BaseModel {

  @Size(max = 255)
  @Column(name = "url")
  private String url;

  @Size(max = 255)
  @Column(name = "content_type")
  private String contentType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "message_id")
  private Message message;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Media media = (Media) o;
    return Objects.equals(url, media.url)
        && Objects.equals(contentType, media.contentType)
        // break infinite loop
        && Objects.equals(message.getId(), media.message.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), url, contentType, message.getId());
  }

  @Override
  public String toString() {
    return "Media{"
        + "url='" + url + '\''
        + ", contentType='" + contentType + '\''
        + ", message=" + message.getId() // break infinite loop
        + '}';
  }
}
