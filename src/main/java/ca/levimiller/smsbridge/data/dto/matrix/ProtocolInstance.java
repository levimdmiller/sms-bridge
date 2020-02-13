package ca.levimiller.smsbridge.data.dto.matrix;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.ma1uta.matrix.protocol.Instance;
import java.util.Map;

@JsonInclude
public class ProtocolInstance extends Instance {

  @Override
  @JsonInclude
  public Map<String, String> getFields() {
    return super.getFields();
  }
}
