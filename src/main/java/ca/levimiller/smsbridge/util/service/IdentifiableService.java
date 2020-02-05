package ca.levimiller.smsbridge.util.service;

public interface IdentifiableService {

  String ANY_ID = "any";

  /**
   * Returns the service's identifier.
   *
   * @return - service id
   */
  String getIdentifier();
}
