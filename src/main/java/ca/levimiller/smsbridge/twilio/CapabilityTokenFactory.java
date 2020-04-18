package ca.levimiller.smsbridge.twilio;

public interface CapabilityTokenFactory {
  String getToken(String clientName);
}
