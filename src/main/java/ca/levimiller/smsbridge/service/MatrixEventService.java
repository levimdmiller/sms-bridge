package ca.levimiller.smsbridge.service;

import io.github.ma1uta.matrix.event.Event;
import io.github.ma1uta.matrix.event.content.EventContent;

public interface MatrixEventService<C extends EventContent> {

  /**
   * Processes the matrix event.
   * @param event - event to process.
   */
  void process(Event<C> event);
}
