package ca.levimiller.smsbridge.data.dto.matrix;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType {
  ROOM_MESSAGE("m.room.message");

  private final String code;

  EventType(String code) {
    this.code = code;
  }

  @JsonValue
  public String getCode() {
    return code;
  }
}
