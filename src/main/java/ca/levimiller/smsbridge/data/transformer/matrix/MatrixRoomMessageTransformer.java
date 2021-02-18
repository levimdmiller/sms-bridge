package ca.levimiller.smsbridge.data.transformer.matrix;

import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.transformer.qualifiers.From;
import ca.levimiller.smsbridge.data.transformer.qualifiers.To;
import ca.levimiller.smsbridge.error.TransformationException;
import io.github.ma1uta.matrix.event.RoomMessage;
import io.github.ma1uta.matrix.event.content.RoomMessageContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        MatrixContactTransformer.class,
        MatrixMediaTransformer.class
    }
)
public interface MatrixRoomMessageTransformer {

  @Mapping(source = "eventId", target = "uid")
  @Mapping(source = "content.body", target = "body")
  @Mapping(source = "roomId", target = "toContact", qualifiedBy = To.class)
  @Mapping(source = "sender", target = "fromContact", qualifiedBy = From.class)
  @Mapping(source = "content", target = "media")
  Message transform(RoomMessage<RoomMessageContent> roomMessage) throws TransformationException;
}
