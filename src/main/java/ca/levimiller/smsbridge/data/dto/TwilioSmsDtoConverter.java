package ca.levimiller.smsbridge.data.dto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

@Component
public class TwilioSmsDtoConverter extends AbstractHttpMessageConverter<TwilioSmsDto> {
  private final FormHttpMessageConverter formHttpMessageConverter;

  @Inject
  public TwilioSmsDtoConverter(FormHttpMessageConverter formHttpMessageConverter) {
    super(new MediaType("application","x-www-form-urlencoded", StandardCharsets.UTF_8));
    this.formHttpMessageConverter = formHttpMessageConverter;
  }

  @Override
  protected boolean supports(Class<?> aClass) {
    return TwilioSmsDto.class == aClass;
  }

  @Override
  protected TwilioSmsDto readInternal(Class<? extends TwilioSmsDto> aClass,
      HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    Map<String, String> vals = formHttpMessageConverter.read(null, inputMessage).toSingleValueMap();

    Integer numMedia = getInteger(vals, "NumMedia");

    return TwilioSmsDto.builder()
        .messageSid(vals.get("MessageSid"))
        .messageSid(vals.get("MessageSid"))
        .accountSid(vals.get("AccountSid"))
        .from(vals.get("From"))
        .to(vals.get("To"))
        .body(vals.get("Body"))
        .numSegments(getInteger(vals, "NumSegments"))
        .numMedia(numMedia)
        .mediaContentTypes(getList(vals, "MediaContentType", numMedia))
        .mediaUrls(getList(vals, "MediaUrl", numMedia))
        .messagingServiceSid(vals.get("MessagingServiceSid"))
        .fromCity(vals.get("FromCity"))
        .fromState(vals.get("FromState"))
        .fromZip(vals.get("FromZip"))
        .fromCountry(vals.get("FromCountry"))
        .toCity(vals.get("ToCity"))
        .toState(vals.get("ToState"))
        .toZip(vals.get("ToZip"))
        .toCountry(vals.get("ToCountry"))
        .build();
  }

  @Override
  protected void writeInternal(TwilioSmsDto twilioSmsDto, HttpOutputMessage httpOutputMessage)
      throws HttpMessageNotWritableException {
    // TwilioSmsDto is currently only for incoming requests.
  }

  private Integer getInteger(Map<String, String> vals, String key) {
    try {
      return Integer.parseInt(vals.get(key));
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private List<String> getList(Map<String, String> vals, String key, Integer size) {
    if (size == null) {
      return Collections.emptyList();
    }
    List<String> result = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      result.add(vals.get(key + i));
    }
    return result;
  }
}
