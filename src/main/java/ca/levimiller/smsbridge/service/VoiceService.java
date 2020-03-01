package ca.levimiller.smsbridge.service;

import ca.levimiller.smsbridge.data.model.VoiceCall;

public interface VoiceService {

  /**
   * Starts a voice call.
   * @param voiceCall - voice call info
   */
  void startCall(VoiceCall voiceCall);
}
