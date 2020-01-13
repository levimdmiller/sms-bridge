package ca.levimiller.smsbridge.service.impl.matrix;

import ca.levimiller.smsbridge.service.MatrixEventService;
import io.github.ma1uta.matrix.event.Event;
import org.springframework.stereotype.Service;

@Service
public class MatrixEventServiceFactory {

  public <E extends Event<?>> MatrixEventService<?> getEventService(
      Class<E> eventType) {
    return null;
  }
}
