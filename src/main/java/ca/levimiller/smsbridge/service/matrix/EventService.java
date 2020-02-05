package ca.levimiller.smsbridge.service.matrix;

import ca.levimiller.smsbridge.util.service.IdentifiableService;
import io.github.ma1uta.matrix.event.Event;

public interface EventService<T extends Event<?>> extends IdentifiableService {

  /**
   * Processes the matrix event.
   *
   * @param event - event to process.
   */
  void process(T event);
}
