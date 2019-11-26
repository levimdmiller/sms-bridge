package ca.levimiller.smsbridge.data.dto.matrix.content;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ContentType {
  TEXT("m.text");

  private final String type;

  ContentType(String type) {
    this.type = type;
  }

  @JsonValue
  public String getType() {
    return type;
  }
}
