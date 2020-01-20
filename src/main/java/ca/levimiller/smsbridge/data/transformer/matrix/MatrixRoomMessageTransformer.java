package ca.levimiller.smsbridge.data.transformer.matrix;

import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.transformer.qualifiers.From;
import ca.levimiller.smsbridge.data.transformer.qualifiers.To;
import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.content.RoomMessageContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MatrixContactTransformer.class})
public interface MatrixRoomMessageTransformer {
  @Mapping(source = "eventId", target = "uid")
  @Mapping(source = "content.body", target = "body")
  @Mapping(source = "roomId", target = "toContact", qualifiedBy = To.class)
  @Mapping(source = "sender", target = "fromContact", qualifiedBy = From.class)
  Message transform(RoomMessage<RoomMessageContent> roomMessage);
}
