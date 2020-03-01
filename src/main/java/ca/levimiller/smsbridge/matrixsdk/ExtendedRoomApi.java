package ca.levimiller.smsbridge.matrixsdk;

import io.github.ma1uta.matrix.EmptyResponse;
import io.github.ma1uta.matrix.ErrorResponse;
import io.github.ma1uta.matrix.RateLimit;
import io.github.ma1uta.matrix.RateLimitedErrorResponse;
import io.github.ma1uta.matrix.Secured;
import io.github.ma1uta.matrix.client.api.RoomApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("/_matrix/client/r0")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface ExtendedRoomApi extends RoomApi {

  @Operation(
      summary = "This API invites a user to participate in a particular room. "
          + "They do not start participating in the room until they actually join the room.",
      responses = {@ApiResponse(
          responseCode = "200",
          description = "The user has been invited to join the room.",
          content = {@Content(
              schema = @Schema(
                  implementation = EmptyResponse.class
              )
          )}
      ), @ApiResponse(
          responseCode = "403",
          description = "You do not have permission to invite the user to the room. "
              + "A meaningful errcode and description error text will be returned. ",
          content = {@Content(
              schema = @Schema(
                  implementation = ErrorResponse.class
              )
          )}
      ), @ApiResponse(
          responseCode = "429",
          description = "This request was rate-limited.",
          content = {@Content(
              schema = @Schema(
                  implementation = RateLimitedErrorResponse.class
              )
          )}
      )},
      security = {@SecurityRequirement(
          name = "accessToken"
      )},
      tags = {"Room membership"}
  )
  @POST
  @RateLimit
  @Secured
  @Path("/rooms/{roomId}/invite")
  void simpleInvite(
      @Parameter(description = "The room identifier (not alias) to which to invite the user.",
          required = true)
      @PathParam("roomId") String var1,
      @RequestBody(description = "JSON body request") SimpleInviteRequest var2,
      @Context UriInfo var3,
      @Context HttpHeaders var4,
      @Suspended AsyncResponse var5,
      @Context SecurityContext var6);

}
