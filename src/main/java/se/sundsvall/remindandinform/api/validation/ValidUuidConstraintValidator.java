package se.sundsvall.remindandinform.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class ValidUuidConstraintValidator implements ConstraintValidator<ValidUuid, Object> {

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		try {
			UUID.fromString(value.toString());
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
