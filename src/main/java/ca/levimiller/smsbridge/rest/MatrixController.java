package ca.levimiller.smsbridge.rest;

import io.github.ma1uta.matrix.EmptyResponse;
import io.github.ma1uta.matrix.application.model.TransactionRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * https://matrix.org/docs/spec/application_service/r0.1.2
 */
@RestController
@RequestMapping({"/matrix/_matrix/app/v1", "/matrix"})
@Api(value = "/matrix", tags = "Api for requests from Matrix")
public interface MatrixController {

  @PutMapping("/transactions/{id}")
  @ApiOperation("Handle event transactions from Matrix")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully processed"),
      @ApiResponse(code = 400, message = "Request not valid")
  })
  EmptyResponse transaction(@PathVariable("id") String transactionId,
      @RequestBody TransactionRequest data);

  @GetMapping("/users/{userId}")
  @ApiOperation("Handles queries for if a user exists, and creates the user if the number is valid")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "User exists (or was created)"),
      @ApiResponse(code = 401, message = "Missing credentials"),
      @ApiResponse(code = 403, message = "Credentials rejected"),
      @ApiResponse(code = 404, message = "User does not exist")
  })
  EmptyResponse users(@PathVariable("userId") String userId);

  @GetMapping("/rooms/{alias}")
  @ApiOperation("Handles queries for if a room exists, or creates the room")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "User exists (or was created)"),
      @ApiResponse(code = 401, message = "Missing credentials"),
      @ApiResponse(code = 403, message = "Credentials rejected"),
      @ApiResponse(code = 404, message = "User does not exist")
  })
  EmptyResponse rooms(@PathVariable("alias") String alias);
}
