package ca.levimiller.smsbridge.util.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Loads all implementations of an interface, and selects the one with matching id.
 * @param <S> - interface type
 */
public class ServiceFactory<S extends IdentifiableService> {

  private final S defaultService;
  private final Map<String, S> serviceMap;

  /**
   * Creates a new ServiceFactory, defaulting to noop service.
   * @param clazz - service interface class
   * @param services - list of all services implementing S
   */
  public ServiceFactory(Class<S> clazz, List<S> services) {
    this(NoopIdentifiableService.getProxy(clazz), services);
  }

  /**
   * Creates a new ServiceFactory with given noop to fall back on.
   * @param defaultService - service to default to
   * @param services - list of all services implementing S
   */
  public ServiceFactory(S defaultService, List<S> services) {
    this.defaultService = defaultService;
    this.serviceMap = services.stream()
        .filter(s -> !IdentifiableService.ANY_ID.equals(s.getIdentifier()))
        .collect(Collectors.toMap(IdentifiableService::getIdentifier, Function.identity()));
  }

  /**
   * Gets the service associated with the given identifier.
   * @param id - id of service
   * @return - service or noop service
   */
  public S getService(String id) {
    return serviceMap.getOrDefault(id, defaultService);
  }
}
