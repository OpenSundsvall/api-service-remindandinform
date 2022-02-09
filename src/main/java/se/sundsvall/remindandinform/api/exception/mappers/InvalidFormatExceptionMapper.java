package se.sundsvall.remindandinform.api.exception.mappers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sundsvall.remindandinform.api.exception.model.ServiceErrorResponse;
import se.sundsvall.remindandinform.api.exception.model.TechnicalDetails;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class InvalidFormatExceptionMapper extends AbstractExceptionMapper<InvalidFormatException> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InvalidFormatExceptionMapper.class);

	@Override
	public Response toResponse(InvalidFormatException e) {

		LOGGER.info("Mapping exception into ServiceErrorResponse: {} ", e.getMessage());

		ServiceErrorResponse serviceErrorResponse = ServiceErrorResponse.create()
			.withMessage("Request validation failed!").withHttpCode(BAD_REQUEST.getStatusCode())
			.withTechnicalDetails(TechnicalDetails.create()
				.withRootCode(BAD_REQUEST.getStatusCode())
				.withRootCause("Bad request format!")
				.withServiceId(getApplicationName())
				.withDetails(List.of("Request: " + uriInfo.getPath())));

		return wrapServiceErrorResponse(serviceErrorResponse);
	}
}
