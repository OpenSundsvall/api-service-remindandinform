package se.sundsvall.remindandinform.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Target({ ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidNullableUuidConstraintValidator.class)
public @interface ValidNullableUuid {

	String message() default "not a valid UUID";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
