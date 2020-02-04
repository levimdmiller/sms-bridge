package ca.levimiller.smsbridge.data.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.levimiller.smsbridge.data.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserNameTransformerTest {

  private final UserNameTransformer userNameTransformer;

  private Contact contact;

  @Autowired
  UserNameTransformerTest(UserNameTransformer userNameTransformer) {
    this.userNameTransformer = userNameTransformer;
  }

  @BeforeEach
  void setUp() {
    contact = Contact.builder()
        .number("+smsNumber")
        .build();
  }

  @Test
  void transform() {
    String result = userNameTransformer.transform(contact);
    assertEquals("+smsNumber", result);
  }
}
