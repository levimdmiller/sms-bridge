package ca.levimiller.smsbridge.matrixsdk;

import io.github.ma1uta.matrix.EmptyResponse;
import io.github.ma1uta.matrix.client.RequestParams;
import io.github.ma1uta.matrix.client.factory.RequestFactory;
import io.github.ma1uta.matrix.client.methods.RoomMethods;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ExtendedRoomMethods extends RoomMethods {

  public ExtendedRoomMethods(RequestFactory factory,
      RequestParams defaultParams) {
    super(factory, defaultParams);
  }

  /**
   * Invites a matrix user to the given room.
   * @param roomId - room id (not alias)
   * @param request - request with fully qualified matrix id (@id:server.ca)
   * @return - request's future
   */
  public CompletableFuture<EmptyResponse> invite(String roomId, SimpleInviteRequest request) {
    Objects.requireNonNull(request.getUserId(), "UserId cannot be empty.");
    RequestParams params = this.defaults().clone().path("roomId", roomId);
    return this.factory().post(ExtendedRoomApi.class, "simpleInvite", params, request,
        EmptyResponse.class);
  }
}
