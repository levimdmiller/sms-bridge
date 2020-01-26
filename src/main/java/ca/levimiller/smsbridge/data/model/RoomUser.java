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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "room_user")
@SQLDelete(sql = "UPDATE room_user SET deleted = 1 WHERE id = ?;",
    check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class RoomUser extends BaseModel {

  @Size(max = 255)
  @Column(name = "user_id", unique = true)
  private String userId;

  @JoinColumn(name = "room_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Room room;

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
    RoomUser roomUser = (RoomUser) o;
    if(room == null ^ roomUser.room == null) {
      return false;
    }
    if(room == null) {
      return true;
    }
    return Objects.equals(userId, roomUser.userId) &&
        Objects.equals(room.getId(), roomUser.room.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), userId, room == null ? null : room.getId());
  }

  @Override
  public String toString() {
    return "RoomUser{"
        + "room='" + (room == null ? null : room.getId())  // break infinite loop
        + '}';
  }
}
