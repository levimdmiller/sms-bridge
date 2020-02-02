package ca.levimiller.smsbridge.data.transformer.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.util.UuidSource;
import io.github.ma1uta.matrix.client.model.account.RegisterRequest;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MatrixUserRegisterTransformerTest {

  private final MatrixUserRegisterTransformer userRegisterTransformer;
  private final Fixture<Contact> contactFixture;

  @MockBean
  private UuidSource uuidSource;

  private Contact contact;

  @Autowired
  MatrixUserRegisterTransformerTest(
      MatrixUserRegisterTransformer userRegisterTransformer,
      Fixture<Contact> contactFixture) {
    this.userRegisterTransformer = userRegisterTransformer;
    this.contactFixture = contactFixture;
  }

  @BeforeEach
  void setUp() {
    contact = contactFixture.create();
    when(uuidSource.newUuid()).thenReturn(UUID.fromString("264ac772-9254-44b9-8e4f-a7310df67813"));
  }

  @Test
  void transform() {
    RegisterRequest result = userRegisterTransformer.transform(contact);
    assertEquals("264ac772-9254-44b9-8e4f-a7310df67813", result.getUsername());
    assertEquals(true, result.getInhibitLogin());
  }
}
