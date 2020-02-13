package ca.levimiller.smsbridge.service.matrix;

import ca.levimiller.smsbridge.util.service.IdentifiableService;
import io.github.ma1uta.matrix.protocol.Protocol;
import io.github.ma1uta.matrix.protocol.ProtocolLocation;
import io.github.ma1uta.matrix.protocol.ProtocolUser;
import java.util.List;
import java.util.Map;

public interface ProtocolService extends IdentifiableService {
  enum Type {
    SMS("sms");

    private String id;

    Type(String id) {
      this.id = id;
    }

    public String getId() {
      return id;
    }
  }

  Protocol protocol();

  List<ProtocolUser> userProtocol(Map<String, String> fields);

  List<ProtocolLocation> locationProtocol(Map<String, String> fields);
}
