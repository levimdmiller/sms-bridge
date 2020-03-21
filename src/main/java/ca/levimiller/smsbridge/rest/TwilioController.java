package ca.levimiller.smsbridge.rest;

import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.dto.TwilioVoiceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twilio")
@Api(value = "/twilio", tags = "Api for requests from Twilio")
public interface TwilioController {

  @PostMapping("/sms")
  @ApiOperation("Create an sms message")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Created", response = String.class),
      @ApiResponse(code = 400, message = "Request not valid")})
  void createSms(@Valid TwilioSmsDto sms);

  @PostMapping(value = "/voice", produces = MediaType.APPLICATION_XML_VALUE)
  @ApiOperation("Start a voice call")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Call initiated", response = String.class),
      @ApiResponse(code = 400, message = "Request not valid")})
  ResponseEntity<String> voice(@Valid TwilioVoiceDto voice);
}
