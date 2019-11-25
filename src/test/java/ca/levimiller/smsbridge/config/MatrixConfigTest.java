package ca.levimiller.smsbridge.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MatrixConfigTest {
  private final MatrixConfig matrixConfig;

  @Autowired
  MatrixConfigTest(MatrixConfig matrixConfig) {
    this.matrixConfig = matrixConfig;
  }

  @Test
  void getDomain() {
    assertEquals("domain.ca", matrixConfig.getDomain());
  }
}
