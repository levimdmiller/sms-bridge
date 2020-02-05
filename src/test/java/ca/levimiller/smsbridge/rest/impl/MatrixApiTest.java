package ca.levimiller.smsbridge.rest.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.db.TransactionRepository;
import ca.levimiller.smsbridge.data.model.Transaction;
import ca.levimiller.smsbridge.service.matrix.EventService;
import ca.levimiller.smsbridge.util.MockLogger;
import ch.qos.logback.classic.Level;
import io.github.ma1uta.matrix.EmptyResponse;
import io.github.ma1uta.matrix.application.model.TransactionRequest;
import io.github.ma1uta.matrix.event.Event;
import io.github.ma1uta.matrix.event.RoomAliases;
import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.content.EventContent;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MatrixApiTest {
  private final MatrixApi matrixApi;

  @MockBean
  private TransactionRepository transactionRepository;
  @MockBean
  private EventService<Event<EventContent>> eventService;
  private MockLogger mockLogger;

  private String transactionId;
  private Transaction transaction;
  private TransactionRequest transactionRequest;
  private Event messageEvent;
  private Event aliasEvent;

  @Autowired
  MatrixApiTest(MatrixApi matrixApi) {
    this.matrixApi = matrixApi;
  }

  @BeforeEach
  void setUp() {
    transactionId = "transactionId";
    transaction = Transaction.builder()
        .transactionId(transactionId)
        .completed(true)
        .build();
    transactionRequest = new TransactionRequest();
    messageEvent = new RoomMessage<>();
    aliasEvent = new RoomAliases();
    transactionRequest.setEvents(List.of(messageEvent, aliasEvent));

    when(transactionRepository.save(any())).thenReturn(transaction);
    mockLogger = new MockLogger(MatrixApi.class);
  }

  @AfterEach
  void tearDown() {
    mockLogger.teardown();
  }

  @Test
  void transactionNotStartedBefore() {
    when(transactionRepository.findDistinctByTransactionId(transactionId))
        .thenReturn(Optional.empty());

    matrixApi.transaction(transactionId, transactionRequest);

    // saved with not completed
    verify(transactionRepository, times(1))
        .save(eq(Transaction.builder()
            .transactionId(transactionId)
            .completed(false)
            .build()));
    // all events processed
    transactionRequest.getEvents().forEach(e -> {
      verify(eventService, times(1)).process(e);
    });
    // saved with completed
    verify(transactionRepository, times(1))
        .save(eq(Transaction.builder()
            .transactionId(transactionId)
            .completed(true)
            .build()));
  }

  @Test
  void transactionStartedBefore() {
    when(transactionRepository.findDistinctByTransactionId(transactionId))
        .thenReturn(Optional.of(transaction));

    matrixApi.transaction(transactionId, transactionRequest);

    // never called
    verify(transactionRepository, times(0))
        .save(any());
    // no events processed
    verify(eventService, times(0)).process(any());
  }

  @Test
  void testEventError() {
    RuntimeException e = new RuntimeException();
    when(transactionRepository.findDistinctByTransactionId(transactionId))
      .thenReturn(Optional.empty());
    doThrow(e).when(eventService).process(messageEvent);

    matrixApi.transaction(transactionId, transactionRequest);

    mockLogger.verify(Level.ERROR, "Error processing event: ", e);
    verify(eventService).process(aliasEvent);
    // saved with completed
    verify(transactionRepository, times(1))
        .save(eq(Transaction.builder()
            .transactionId(transactionId)
            .completed(true)
            .build()));
  }

  @Test
  void rooms() {
    assertEquals(EmptyResponse.class, matrixApi.rooms("rooms").getClass());
  }

  @Test
  void users() {
    assertEquals(EmptyResponse.class, matrixApi.users("users").getClass());
  }
}
