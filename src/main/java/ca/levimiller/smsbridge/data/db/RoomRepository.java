package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.model.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>,
    QuerydslPredicateExecutor<Room> {

  /**
   * Finds a room by the given id.
   * @param roomId - room's id
   * @return - Room object if found
   */
  Optional<Room> findDistinctByRoomId(String roomId);
}
