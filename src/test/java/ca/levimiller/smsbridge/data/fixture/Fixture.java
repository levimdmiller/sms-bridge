package ca.levimiller.smsbridge.data.fixture;

public interface Fixture<T> {

  /**
   * Generates a filled entity.
   * @return - generated entity.
   */
  T create();
}
