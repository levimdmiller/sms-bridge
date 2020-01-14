package ca.levimiller.smsbridge.service.impl.matrix.events;

import ca.levimiller.smsbridge.service.MatrixEventService;
import io.github.ma1uta.matrix.event.Event;
import io.github.ma1uta.matrix.event.content.EventContent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class CompositeEventService implements MatrixEventService {

  private final MatrixEventService noopEventService;
  private final Map<String, MatrixEventService> eventServiceMap;

  @Inject
  public CompositeEventService(
      @Qualifier("noopEventService") MatrixEventService noopEventService,
      List<MatrixEventService> eventServices) {
    this.noopEventService = noopEventService;
    this.eventServiceMap = initMap(eventServices);
  }

  @Override
  public void process(Event<EventContent> event) {
    eventServiceMap.getOrDefault(event.getType(), noopEventService)
        .process(event);
  }

  @Override
  public String getType() {
    return ANY_TYPE;
  }

  /**
   * Creates a map from event type to event service, ignoring ANY_TYPE services.
   *
   * @param eventServices - matrix event service
   * @return - event service map
   */
  private Map<String, MatrixEventService> initMap(
      List<MatrixEventService> eventServices) {
    Map<String, MatrixEventService> map = new HashMap<>();
    for (MatrixEventService eventService : eventServices) {
      if (!ANY_TYPE.equals(eventService.getType())) {
        map.put(eventService.getType(), eventService);
      }
    }
    return map;
  }
}
