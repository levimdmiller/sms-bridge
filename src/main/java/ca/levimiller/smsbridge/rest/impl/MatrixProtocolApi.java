package ca.levimiller.smsbridge.rest.impl;

import ca.levimiller.smsbridge.data.transformer.PhoneNumberTransformer;
import ca.levimiller.smsbridge.error.NotFoundException;
import ca.levimiller.smsbridge.rest.MatrixProtocolController;
import ca.levimiller.smsbridge.service.matrix.ProtocolService;
import ca.levimiller.smsbridge.util.service.ServiceFactory;
import ca.levimiller.smsbridge.validation.constraints.ValidPhoneNumber;
import io.github.ma1uta.matrix.protocol.Protocol;
import io.github.ma1uta.matrix.protocol.ProtocolLocation;
import io.github.ma1uta.matrix.protocol.ProtocolUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class MatrixProtocolApi implements MatrixProtocolController {
  private final ServiceFactory<ProtocolService> serviceFactory;
  private final PhoneNumberTransformer phoneNumberTransformer;
  private final Validator validator;

  @Inject
  public MatrixProtocolApi(
      ServiceFactory<ProtocolService> serviceFactory,
      PhoneNumberTransformer phoneNumberTransformer,
      Validator validator) {
    this.serviceFactory = serviceFactory;
    this.phoneNumberTransformer = phoneNumberTransformer;
    this.validator = validator;
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
    String number = phoneNumberTransformer.transform(alias);
    if(!validator.validate(number, ValidPhoneNumber.class).isEmpty()) {
      throw new NotFoundException();
    }

    List<ProtocolLocation> locations = new ArrayList<>();
    for(ProtocolService.Type type : ProtocolService.Type.values()) {
      locations.addAll(serviceFactory.getService(type.getId())
          .locationProtocol(Map.of("number", number)));
    }
    return locations;
  }

  @Override
  public List<ProtocolUser> user(String userId) {
    List<ProtocolUser> users = new ArrayList<>();
    for(ProtocolService.Type type : ProtocolService.Type.values()) {
      users.addAll(serviceFactory.getService(type.getId())
          .userProtocol(Map.of("name", userId)));
    }
    return users;
  }
}
