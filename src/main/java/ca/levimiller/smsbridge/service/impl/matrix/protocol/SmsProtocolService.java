package ca.levimiller.smsbridge.service.impl.matrix.protocol;

import ca.levimiller.smsbridge.data.db.ChatUserRepository;
import ca.levimiller.smsbridge.data.dto.matrix.MatrixProtocol;
import ca.levimiller.smsbridge.data.dto.matrix.ProtocolInstance;
import ca.levimiller.smsbridge.data.transformer.RoomNameTransformer;
import ca.levimiller.smsbridge.service.matrix.ProtocolService;
import io.github.ma1uta.matrix.protocol.FieldMetadata;
import io.github.ma1uta.matrix.protocol.Instance;
import io.github.ma1uta.matrix.protocol.Protocol;
import io.github.ma1uta.matrix.protocol.ProtocolLocation;
import io.github.ma1uta.matrix.protocol.ProtocolUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class SmsProtocolService implements ProtocolService {
  private final RoomNameTransformer roomNameTransformer;
  private final ChatUserRepository userRepository;

  @Inject
  public SmsProtocolService(
      RoomNameTransformer roomNameTransformer,
      ChatUserRepository userRepository) {
    this.roomNameTransformer = roomNameTransformer;
    this.userRepository = userRepository;
  }

  @Override
  public Protocol protocol() {
    Protocol protocol = new MatrixProtocol();
    protocol.setUserFields(List.of("id", "name", "number"));
    protocol.setLocationFields(List.of("id", "name", "number"));

    FieldMetadata idMeta = new FieldMetadata();
    idMeta.setPlaceholder("Identifier");
    idMeta.setRegexp("[^\\s]+");

    FieldMetadata nameMeta = new FieldMetadata();
    nameMeta.setPlaceholder("Levi Miller");
    nameMeta.setRegexp("[^\\s]+");

    FieldMetadata numberMeta = new FieldMetadata();
    numberMeta.setPlaceholder("+1 (123) 456-7890");
    numberMeta.setRegexp("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$");
    protocol.setFieldTypes(Map.of(
        "id", idMeta, "name", nameMeta, "number", numberMeta
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
    List<ProtocolUser> users = new ArrayList<>();
    if(fields.containsKey("id")) {
      ProtocolUser user = new ProtocolUser(); 
    }
    return users;
  }

  @Override
  public List<ProtocolLocation> locationProtocol(Map<String, String> fields) {
    List<ProtocolLocation> locations = new ArrayList<>();
    if(fields.containsKey("number")) {
      String number = fields.get("number");
      ProtocolLocation location = new ProtocolLocation();
      location.setAlias(roomNameTransformer.transformNumber(number));
      location.setProtocol(getIdentifier());
      location.setFields(Map.of("number", number));
    }
    return locations;
  }

  @Override
  public String getIdentifier() {
    return "sms";
  }
}
