package ca.levimiller.smsbridge.data.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PhoneNumberTransformerTest {

  private PhoneNumberTransformer transformer;

  @BeforeEach
  void setUp() {
    transformer = new PhoneNumberTransformer() {};
  }

  @Test
  void transform() {
    String result = transformer.transform("+ (123) - 456 - 7890");
    assertEquals("+1234567890", result);
  }
}
