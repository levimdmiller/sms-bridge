package ca.levimiller.smsbridge.data.dto.matrix.room;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VisibilityType {
  PUBLIC("public"), PRIVATE("private");

  private final String code;

  VisibilityType(String code) {
    this.code = code;
  }

  @JsonValue
  public String getCode() {
    return code;
  }
}
