package ca.levimiller.smsbridge.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.levimiller.smsbridge.error.NotFoundException;
import io.github.ma1uta.matrix.impl.exception.MatrixException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MatrixUtilTest {
  private MatrixUtil matrixUtil;

  @BeforeEach
  void setUp() {
    matrixUtil = new MatrixUtil();
  }

  @Test
  void causedBy_StatusEqual() {
    RuntimeException e = new RuntimeException(new Throwable(new IllegalArgumentException(
        new NotFoundException(new MatrixException("internal error", "message", 404)))));

    assertTrue(matrixUtil.causedBy(e, HttpStatus.NOT_FOUND));
  }

  @Test
  void causedBy_StatusNotEqual() {
    RuntimeException e = new RuntimeException(new Throwable(new IllegalArgumentException(
        new NotFoundException(new MatrixException("internal error", "message", 500)))));

    assertFalse(matrixUtil.causedBy(e, HttpStatus.NOT_FOUND));
  }

  @Test
  void causedBy_Null() {
    RuntimeException e = new RuntimeException(new Throwable(new IllegalArgumentException(
        new NotFoundException(new MatrixException("internal error", "message", 404)))));

    assertFalse(matrixUtil.causedBy(e, null));
  }

  @Test
  void causedBy_NoMatrixException() {
    RuntimeException e = new RuntimeException(new Throwable(new IllegalArgumentException(
        new NotFoundException())));

    assertFalse(matrixUtil.causedBy(e, HttpStatus.NOT_FOUND));
  }
}