package ca.levimiller.smsbridge.data.dto.matrix.room;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoomPreset {
  PUBLIC("public_chat"),
  PRIVATE("private_chat"),
  TRUSTED_PRIVATE("trusted_private_chat");

  private final String code;

  RoomPreset(String code) {
    this.code = code;
  }

  @JsonValue
  public String getCode() {
    return code;
  }
}
