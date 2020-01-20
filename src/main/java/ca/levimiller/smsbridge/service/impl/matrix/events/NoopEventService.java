package ca.levimiller.smsbridge.service.impl.matrix.events;

import ca.levimiller.smsbridge.service.MatrixEventService;
import io.github.ma1uta.matrix.event.Event;
import io.github.ma1uta.matrix.event.content.EventContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("noopEventService")
public class NoopEventService implements MatrixEventService<Event<EventContent>, EventContent> {

  @Override
  public void process(Event<EventContent> event) {
    log.info("Event ignored: {}", event.getType());
  }

  @Override
  public String getType() {
    return ANY_TYPE;
  }
}
