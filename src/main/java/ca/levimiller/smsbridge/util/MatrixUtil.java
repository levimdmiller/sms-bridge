package ca.levimiller.smsbridge.util;

import io.github.ma1uta.matrix.impl.exception.MatrixException;
import java.util.Objects;
import org.springframework.http.HttpStatus;

public class MatrixUtil {

  /**
   * Checks if the cause of the throwable t is a MatrixException with the given status code.
   *
   * @param t - throwable to check
   * @param status - status code that caused the error
   * @return true if caused by a MatrixException with the status code false otherwise
   */
  public boolean causedBy(Throwable t, HttpStatus status) {
    while (t != null) {
      if (t instanceof MatrixException
          && Objects.equals(((MatrixException) t).getStatus(), status.value())) {
        return true;
      }
      t = t.getCause();
    }
    return false;
  }
}
