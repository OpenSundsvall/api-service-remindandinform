package se.sundsvall.remindandinform.api.exception.mappers;

import com.fasterxml.jackson.core.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sundsvall.remindandinform.api.exception.model.ServiceErrorResponse;
import se.sundsvall.remindandinform.api.exception.model.TechnicalDetails;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class JsonParseExceptionMapper extends AbstractExceptionMapper<JsonParseException> {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonParseExceptionMapper.class);

	@Override
	public Response toResponse(JsonParseException e) {

		LOGGER.info("Mapping exception into ServiceErrorResponse: {} ", e.getMessage());

		ServiceErrorResponse serviceErrorResponse = ServiceErrorResponse.create()
			.withMessage("Bad request format!").withHttpCode(BAD_REQUEST.getStatusCode())
			.withTechnicalDetails(TechnicalDetails.create()
				.withRootCode(BAD_REQUEST.getStatusCode())
				.withRootCause(e.getOriginalMessage())
				.withServiceId(getApplicationName())
				.withDetails(List.of("Request: " + uriInfo.getPath())));

		return wrapServiceErrorResponse(serviceErrorResponse);
	}
}
