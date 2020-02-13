package ca.levimiller.smsbridge.validation;

import ca.levimiller.smsbridge.validation.constraints.ValidPhoneNumber;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value != null && value.matches("^[0-9]{8,14}$");
  }
}
