package ca.levimiller.smsbridge.data.transformer.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.db.NumberRegistryRepository;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.error.TransformationException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MatrixContactTransformerTest {

  private final MatrixContactTransformer matrixContactTransformer;
  @MockBean
  private NumberRegistryRepository numberRegistryRepository;

  private String sender;
  private Contact contact;

  @Autowired
  MatrixContactTransformerTest(
      MatrixContactTransformer matrixContactTransformer) {
    this.matrixContactTransformer = matrixContactTransformer;
  }

  @BeforeEach
  void setUp() {
    sender = "sender";
    contact = new Contact();
  }

  @Test
  void transformTo() {
  }

  @Test
  void transformFrom() throws TransformationException {
    NumberRegistration numberRegistration = NumberRegistration.builder()
        .ownerId(sender)
        .contact(contact)
        .build();
    when(numberRegistryRepository.findDistinctByOwnerId(sender))
        .thenReturn(Optional.of(numberRegistration));

    Contact result = matrixContactTransformer.transformFrom(sender);
    assertEquals(contact, result);
  }

  @Test
  void transformFrom_NoLink() {
    when(numberRegistryRepository.findDistinctByOwnerId(sender))
        .thenReturn(Optional.empty());
    TransformationException thrown = assertThrows(TransformationException.class,
        () -> matrixContactTransformer.transformFrom(sender));
    assertEquals("Message sender isn't linked to a number: sender", thrown.getMessage());
  }
}