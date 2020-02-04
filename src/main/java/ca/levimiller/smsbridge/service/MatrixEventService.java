package ca.levimiller.smsbridge.service;

import io.github.ma1uta.matrix.event.Event;
import io.github.ma1uta.matrix.event.content.EventContent;

public interface MatrixEventService<T extends Event<?>> {

  String ANY_TYPE = "any";

  /**
   * Processes the matrix event.
   *
   * @param event - event to process.
   */
  void process(T event);

  /**
   * Matrix Event Type.
   *
   * @return - event type.
   */
  String getType();
}
