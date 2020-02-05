package ca.levimiller.smsbridge.rest;

import io.github.ma1uta.matrix.protocol.Protocol;
import io.github.ma1uta.matrix.protocol.ProtocolLocation;
import io.github.ma1uta.matrix.protocol.ProtocolUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/matrix/_matrix/app/v1", "/matrix"})
@Api(value = "/_matrix/app/v1/thirdparty", tags = "Api for Matrix 3rd party protocol")
public interface MatrixProtocolApi {

  @GetMapping("/protocol/{protocol}")
  @ApiOperation("Returns information about third party network")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "The protocol was found and metadata returned."),
      @ApiResponse(code = 401, message = "Missing credentials"),
      @ApiResponse(code = 403, message = "Credentials rejected"),
      @ApiResponse(code = 404, message = "Protocol does not exist")
  })
  Protocol protocol(@PathVariable("protocol") String protocol);

  @GetMapping("/user/{protocol}")
  @ApiOperation("Retrieve a Matrix User ID linked to a user on the third party service, given a set of user parameters.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "The Matrix User IDs found with the given parameters."),
      @ApiResponse(code = 401, message = "Missing credentials"),
      @ApiResponse(code = 403, message = "Credentials rejected"),
      @ApiResponse(code = 404, message = "No users were found with the given parameters.")
  })

  List<ProtocolUser> userProtocol(@PathVariable("protocol") String protocol,
      @RequestParam Map<String, String> fields);
  @GetMapping("/location/{protocol}")
  @ApiOperation("Returns a list of Matrix portal rooms that match to a third party location.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "At least one portal room was found."),
      @ApiResponse(code = 401, message = "Missing credentials"),
      @ApiResponse(code = 403, message = "Credentials rejected"),
      @ApiResponse(code = 404, message = "No mappings were found with the given parameters.")
  })
  List<ProtocolLocation> locationProtocol(@PathVariable("protocol") String protocol,
      @RequestParam Map<String, String> fields);

  @GetMapping("/location")
  @ApiOperation("Retrieve an array of third party network locations from a Matrix room alias.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "At least one portal room was found."),
      @ApiResponse(code = 401, message = "Missing credentials"),
      @ApiResponse(code = 403, message = "Credentials rejected"),
      @ApiResponse(code = 404, message = "No portal rooms were found.")
  })
  List<ProtocolLocation> location(@RequestParam("alias") String alias);

  @GetMapping("/user")
  @ApiOperation("Retrieve an array of third party users from a Matrix User ID.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "An array of third party users."),
      @ApiResponse(code = 401, message = "Missing credentials"),
      @ApiResponse(code = 403, message = "Credentials rejected"),
      @ApiResponse(code = 404, message = "No mappings were found with the given parameters.")
  })
  List<ProtocolUser> user(@RequestParam("userid") String userId);
}
