package ca.levimiller.smsbridge.service.impl.matrix.protocol;

import ca.levimiller.smsbridge.data.dto.matrix.MatrixProtocol;
import ca.levimiller.smsbridge.data.dto.matrix.ProtocolInstance;
import ca.levimiller.smsbridge.service.matrix.ProtocolService;
import io.github.ma1uta.matrix.protocol.FieldMetadata;
import io.github.ma1uta.matrix.protocol.Instance;
import io.github.ma1uta.matrix.protocol.Protocol;
import io.github.ma1uta.matrix.protocol.ProtocolLocation;
import io.github.ma1uta.matrix.protocol.ProtocolUser;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SmsProtocolService implements ProtocolService {

  @Override
  public Protocol protocol() {
    Protocol protocol = new MatrixProtocol();
    protocol.setUserFields(List.of("name", "number"));
    protocol.setLocationFields(List.of("name", "number"));

    FieldMetadata nameMeta = new FieldMetadata();
    nameMeta.setPlaceholder("Levi Miller");
    nameMeta.setRegexp("[^\\s]+");

    FieldMetadata numberMeta = new FieldMetadata();
    numberMeta.setPlaceholder("+1 (123) 456-7890");
    numberMeta.setRegexp("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$");
    protocol.setFieldTypes(Map.of(
        "name", nameMeta, "number", numberMeta
    ));

    Instance instance = new ProtocolInstance();
    instance.setNetworkId("twilio");
    instance.setDesc("Twilio");
    instance.setFields(Map.of());
    protocol.setInstances(List.of(instance));
    return protocol;
  }

  @Override
  public List<ProtocolUser> userProtocol(Map<String, String> fields) {
    return null;
  }

  @Override
  public List<ProtocolLocation> locationProtocol(Map<String, String> fields) {
    // no rooms/locations, only users (maybe for multi-user sms?).
    return Collections.emptyList();
  }

  @Override
  public String getIdentifier() {
    return "sms";
  }
}
