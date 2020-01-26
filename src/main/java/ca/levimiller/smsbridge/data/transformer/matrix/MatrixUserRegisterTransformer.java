package ca.levimiller.smsbridge.data.transformer.matrix;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.transformer.UserNameTransformer;
import io.github.ma1uta.matrix.client.model.account.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserNameTransformer.class})
public interface MatrixUserRegisterTransformer {

  @Mapping(source = ".", target = "username")
  @Mapping(target = "inhibitLogin", constant = "true")
  RegisterRequest transform(Contact contact);
}
