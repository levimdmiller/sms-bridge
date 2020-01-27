package ca.levimiller.smsbridge.data.transformer.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.Contact;
import io.github.ma1uta.matrix.client.model.account.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MatrixUserRegisterTransformerTest {
  private final MatrixUserRegisterTransformer userRegisterTransformer;
  private final Fixture<Contact> contactFixture;

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
  }

  @Test
  void transform() {
    RegisterRequest result = userRegisterTransformer.transform(contact);
    assertEquals("username", result.getUsername());
    assertEquals(true, result.getInhibitLogin());
  }
}