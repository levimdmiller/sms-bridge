package ca.levimiller.smsbridge.data.transformer.matrix;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.util.UuidSource;
import io.github.ma1uta.matrix.client.model.account.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class MatrixUserRegisterTransformer {

  private final UuidSource uuidSource;

  protected MatrixUserRegisterTransformer(UuidSource uuidSource) {
    this.uuidSource = uuidSource;
  }

  /**
   * Transforms the Contact to a matrix user registration request.
   *
   * @param contact - contact to transform.
   * @return - matrix register user request
   */
  public RegisterRequest transform(Contact contact) {
    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest.setUsername("sms-" + uuidSource.newUuid().toString());
    registerRequest.setInhibitLogin(true);
    return registerRequest;
  }
}
