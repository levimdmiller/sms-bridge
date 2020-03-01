package ca.levimiller.smsbridge.data.transformer;

import ca.levimiller.smsbridge.data.db.ContactRepository;
import ca.levimiller.smsbridge.data.model.Contact;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class ContactTransformer {
  private final ContactRepository contactRepository;
  private final PhoneNumberTransformer phoneNumberTransformer;

  @Inject
  public ContactTransformer(
      ContactRepository contactRepository,
      PhoneNumberTransformer phoneNumberTransformer) {
    this.contactRepository = contactRepository;
    this.phoneNumberTransformer = phoneNumberTransformer;
  }

  /**
   * Searches for existing contact by number.
   * @param number - number to search for
   * @return - contact if found
   */
  public Optional<Contact> transform(String number) {
    return contactRepository.findDistinctByNumber(phoneNumberTransformer.transform(number));
  }
}
