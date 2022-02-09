package se.sundsvall.remindandinform.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class ValidNullableUuidConstraintValidator implements ConstraintValidator<ValidNullableUuid, Object> {

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		try {
			UUID.fromString(value.toString());
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
