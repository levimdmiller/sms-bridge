package ca.levimiller.smsbridge.data.util;

import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomConfig {
  private final long seed = 715225741L;

  @Bean
  public Random seeded() {
    return new Random(seed);
  }

}
