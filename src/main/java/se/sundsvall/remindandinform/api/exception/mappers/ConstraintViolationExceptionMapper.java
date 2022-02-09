package se.sundsvall.remindandinform.api.exception.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sundsvall.remindandinform.api.exception.model.ServiceErrorResponse;
import se.sundsvall.remindandinform.api.exception.model.TechnicalDetails;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.String.format;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class ConstraintViolationExceptionMapper extends AbstractExceptionMapper<ConstraintViolationException> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConstraintViolationExceptionMapper.class);

	@Override
	public Response toResponse(ConstraintViolationException e) {

		LOGGER.info("Mapping exception into ServiceErrorResponse: {} ", e.getMessage());

		ServiceErrorResponse serviceErrorResponse = ServiceErrorResponse.create()
			.withMessage("Request validation failed!")
			.withHttpCode(BAD_REQUEST.getStatusCode()).withTechnicalDetails(
				TechnicalDetails.create().withRootCode(BAD_REQUEST.getStatusCode())
					.withServiceId(getApplicationName())
					.withRootCause("Constraint violation")
					.withDetails(Stream.of(getConstraintViolationDetails(e.getConstraintViolations()), List.of("Request: " + uriInfo.getPath()))
						.flatMap(Collection::stream)
						.toList()));

		return wrapServiceErrorResponse(serviceErrorResponse);
	}

	private List<String> getConstraintViolationDetails(final Set<ConstraintViolation<?>> violations) {
		return violations.stream().map(v -> format("%s: %s", mapPropertyPathToFieldName(v), v.getMessage())).sorted()
			.toList();
	}

	private String mapPropertyPathToFieldName(final ConstraintViolation<?> constraintViolation) {
		String fieldViolated = String.valueOf(constraintViolation.getPropertyPath());
		fieldViolated = fieldViolated.substring(fieldViolated.indexOf('.') + 1);
		if (fieldViolated.equals("null")) {
			fieldViolated = "Unknown field";
		}
		return fieldViolated;
	}
}