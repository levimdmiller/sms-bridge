package ca.levimiller.smsbridge.util;

import java.util.UUID;

/**
 * A source of new {@link UUID} instances.
 */
public interface UuidSource {

  /**
   * Returns a new {@link UUID} instance.
   *
   * <p>The returned value is guaranteed to be unique.
   */
  UUID newUuid();
}
