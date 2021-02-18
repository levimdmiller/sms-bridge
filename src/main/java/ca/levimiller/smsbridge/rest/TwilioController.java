package ca.levimiller.smsbridge.rest;

import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twilio")
@Api(value = "/twilio", tags = "Api for requests from Twilio")
public interface TwilioController {

  @PostMapping(
      value = "/sms",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
  )
  @ApiOperation("Create an sms message")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Created", response = String.class),
      @ApiResponse(code = 400, message = "Request not valid")})
  void createSms(@Valid @RequestBody TwilioSmsDto sms);

  @GetMapping("/attachment/{media_uid}")
  @ApiOperation("Returns the attachment file")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success", response = String.class),
      @ApiResponse(code = 400, message = "Request not valid")})
  ResponseEntity<InputStreamResource> getAttachment(@PathVariable("media_uid") UUID mediaUid);
}
