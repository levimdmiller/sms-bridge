package ca.levimiller.smsbridge.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.UUID;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attachment")
@Api(value = "/attachment", tags = "Api for attachments")
public interface AttachmentController {

  @GetMapping("/{media_uid}")
  @ApiOperation("Returns the attachment file")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success", response = String.class),
      @ApiResponse(code = 400, message = "Request not valid")})
  ResponseEntity<InputStreamResource> getAttachment(@PathVariable("media_uid") UUID mediaUid);
}
