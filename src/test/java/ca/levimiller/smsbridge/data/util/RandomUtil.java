package ca.levimiller.smsbridge.data.util;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RandomUtil {

  private final Random random;

  @Autowired
  public RandomUtil(Random random) {
    this.random = random;
  }

  /**
   * Gets a random string of the given length.
   *
   * @param length - length of the string
   * @return - randomly generated string
   */
  public String getString(int length) {
    byte[] array = new byte[length];
    random.nextBytes(array);
    return new String(array, StandardCharsets.UTF_8);
  }

  /**
   * Gets a random int.
   *
   * @return - random int.
   */
  public int getInt() {
    return random.nextInt();
  }

  /**
   * Gets a random bounded int between 0 (inclusive) and bound (exclusive).
   *
   * @return - random int.
   */
  public int getInt(int bound) {
    return random.nextInt(bound);
  }

  public boolean getBoolean() {
    return random.nextBoolean();
  }

  /**
   * Creates a list of the given size filled with the given fixture.
   *
   * @param fixture - fixture to fill list
   * @param <T> - type of list
   * @return - randomly generated list
   */
  public <T> List<T> getList(Fixture<T> fixture, int size) {
    List<T> list = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      list.add(i, fixture.create());
    }
    return list;
  }

  /**
   * Creates a list filled with the given fixture. The size of the list is between 1-10.
   *
   * @param fixture - fixture to fill list
   * @param <T> - type of list
   * @return - randomly generated list
   */
  public <T> List<T> getList(Fixture<T> fixture) {
    return getList(fixture, getInt(9) + 1);
  }

  /**
   * Gets a random enum value.
   * @param enumClass - enum class type
   * @param <T> - enum type
   * @return - random value from the enum class
   */
  public <T extends Enum> T getEnum(Class<T> enumClass) {
    T[] values = enumClass.getEnumConstants();
    return values[getInt(values.length)];
  }
}
