package ca.levimiller.smsbridge.rest.impl;

import ca.levimiller.smsbridge.error.NotFoundException;
import ca.levimiller.smsbridge.rest.MatrixProtocolController;
import ca.levimiller.smsbridge.service.matrix.ProtocolService;
import ca.levimiller.smsbridge.util.service.ServiceFactory;
import io.github.ma1uta.matrix.protocol.Protocol;
import io.github.ma1uta.matrix.protocol.ProtocolLocation;
import io.github.ma1uta.matrix.protocol.ProtocolUser;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class MatrixProtocolApi implements MatrixProtocolController {
  private final ServiceFactory<ProtocolService> serviceFactory;

  @Inject
  public MatrixProtocolApi(
      ServiceFactory<ProtocolService> serviceFactory) {
    this.serviceFactory = serviceFactory;
  }

  @Override
  public Protocol protocol(String protocol) {
    return serviceFactory.getService(protocol).protocol();
  }

  @Override
  public List<ProtocolUser> userProtocol(String protocol, Map<String, String> fields) {
    return serviceFactory.getService(protocol).userProtocol(fields);
  }

  @Override
  public List<ProtocolLocation> locationProtocol(String protocol, Map<String, String> fields) {
    return serviceFactory.getService(protocol).locationProtocol(fields);
  }

  @Override
  public List<ProtocolLocation> location(String alias) {
    throw new NotFoundException();
  }

  @Override
  public List<ProtocolUser> user(String userId) {
    throw new NotFoundException();
  }
}
