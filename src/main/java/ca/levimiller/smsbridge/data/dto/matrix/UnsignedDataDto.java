package ca.levimiller.smsbridge.data.dto.matrix;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnsignedDataDto {

  private Integer age;
  @JsonProperty("redacted_because")
  private EventDto redactedBecause;
  @JsonProperty("transaction_id")
  private String transactionId;
}
