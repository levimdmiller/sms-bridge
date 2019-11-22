package ca.levimiller.smsbridge.data.dto.matrix.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TextContent extends EventContent {

  private String format;
  @JsonProperty("formatted_body")
  private String formattedBody;

  @Builder
  public TextContent(String body, String format, String formattedBody) {
    super(ContentType.TEXT, body);
    this.format = format;
    this.formattedBody = formattedBody;
  }
}
