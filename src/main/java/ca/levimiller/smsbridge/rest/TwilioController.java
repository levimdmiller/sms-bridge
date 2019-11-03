package ca.levimiller.smsbridge.rest;

import ca.levimiller.smsbridge.data.dto.SmsDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
  void createSms(SmsDto sms);
}
