package ca.levimiller.smsbridge.service.impl.matrix;

import ca.levimiller.smsbridge.data.db.ChatUserRepository;
import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.VoiceCall;
import ca.levimiller.smsbridge.error.NotFoundException;
import ca.levimiller.smsbridge.service.RoomService;
import ca.levimiller.smsbridge.service.UserService;
import ca.levimiller.smsbridge.service.VoiceService;
import ca.levimiller.smsbridge.util.UuidSource;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.event.CallCandidates;
import io.github.ma1uta.matrix.event.CallInvite;
import io.github.ma1uta.matrix.event.content.CallCandidatesContent;
import io.github.ma1uta.matrix.event.content.CallInviteContent;
import io.github.ma1uta.matrix.event.nested.Candidate;
import io.github.ma1uta.matrix.event.nested.Offer;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatrixVoiceService implements VoiceService {
  private final UuidSource uuidSource;
  private final ChatUserRepository chatUserRepository;
  private final RoomService roomService;
  private final UserService userService;
  private final AppServiceClient matrixClient;

  @Inject
  public MatrixVoiceService(UuidSource uuidSource,
      ChatUserRepository chatUserRepository,
      RoomService roomService, UserService userService,
      AppServiceClient matrixClient) {
    this.uuidSource = uuidSource;
    this.chatUserRepository = chatUserRepository;
    this.roomService = roomService;
    this.userService = userService;
    this.matrixClient = matrixClient;
  }

  @Override
  public void startCall(VoiceCall voiceCall) {
    // Find channel or room registered to the destination of the message
    ChatUser to = chatUserRepository.findDistinctByContact(voiceCall.getToContact())
        .orElseThrow(() -> {
          log.error("Destination number is not registered: {}", voiceCall.getToContact());
          return new NotFoundException("Destination number is not registered.");
        });

    // Get room id and user id (ensure created/joined/etc.)
    ChatUser from = userService.getUser(voiceCall.getFromContact());
    String roomId = roomService.getRoom(to, from);

    CallInviteContent content = new CallInviteContent();
    content.setCallId(uuidSource.newUuid().toString());
    content.setLifetime(60000L); // 1 min
    content.setVersion(0L);
    Offer offer = new Offer();
    offer.setType("offer");
    offer.setSdp("");
    content.setOffer(offer);
    matrixClient.userId(from.getOwnerId()).event()
        .sendEvent(roomId, CallInvite.TYPE, content);

    CallCandidatesContent candidatesContent = new CallCandidatesContent();
    candidatesContent.setCallId(content.getCallId());
    candidatesContent.setVersion(0L);
    Candidate candidate = new Candidate();
    candidate.setSdpMid("audio");
    candidate.setSdpMLineIndex(0L);
    candidate.setCandidate("");
    candidatesContent.setCandidates(List.of(
        candidate
    ));
    matrixClient.userId(from.getOwnerId()).event()
        .sendEvent(roomId, CallCandidates.TYPE, candidatesContent);
  }
}
