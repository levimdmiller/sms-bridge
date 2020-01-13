package ca.levimiller.smsbridge.service.impl.matrix.events;

import ca.levimiller.smsbridge.service.MatrixEventService;
import io.github.ma1uta.matrix.event.Event;
import io.github.ma1uta.matrix.event.content.EventContent;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MatrixEventServiceFactory {

  private final MatrixEventService<EventContent> noopEventService;

  @Inject
  public MatrixEventServiceFactory(
      @Qualifier("NoopEventService") MatrixEventService<EventContent> noopEventService) {
    this.noopEventService = noopEventService;
  }

  public <E extends Event<?>> MatrixEventService<?> getEventService(
      Class<E> eventType) {
    return noopEventService;
  }
}
