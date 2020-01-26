package ca.levimiller.smsbridge.data.transformer.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.transformer.UserNameTransformer;
import io.github.ma1uta.matrix.client.model.account.RegisterRequest;
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
  private UserNameTransformer userNameTransformer;

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
    when(userNameTransformer.transform(contact)).thenReturn("username");
  }

  @Test
  void transform() {
    RegisterRequest result = userRegisterTransformer.transform(contact);
    assertEquals("username", result.getUsername());
    assertEquals(true, result.getInhibitLogin());
  }
}