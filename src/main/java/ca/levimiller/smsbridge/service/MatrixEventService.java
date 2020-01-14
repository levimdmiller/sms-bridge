package ca.levimiller.smsbridge.service;

import io.github.ma1uta.matrix.event.Event;
import io.github.ma1uta.matrix.event.content.EventContent;

public interface MatrixEventService {
  String ANY_TYPE = "any";

  /**
   * Processes the matrix event.
   * @param event - event to process.
   */
  void process(Event<EventContent> event);

  /**
   * Event Type
   * @return - event type.
   */
  String getType();
}
