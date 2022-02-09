package se.sundsvall.remindandinform.api.exception.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sundsvall.remindandinform.api.exception.model.ServiceErrorResponse;
import se.sundsvall.remindandinform.api.exception.model.TechnicalDetails;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class ProcessingExceptionMapper extends AbstractExceptionMapper<ProcessingException> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessingExceptionMapper.class);

	@Override
	public Response toResponse(ProcessingException e) {

		LOGGER.info("Mapping exception into ServiceErrorResponse: {} ", e.getMessage());

		ServiceErrorResponse serviceErrorResponse = ServiceErrorResponse.create()
			.withMessage("Bad request format!").withHttpCode(BAD_REQUEST.getStatusCode())
			.withTechnicalDetails(TechnicalDetails.create()
				.withRootCode(BAD_REQUEST.getStatusCode())
				.withServiceId(getApplicationName())
				.withDetails(List.of(e.getCause().getMessage(), "Request: " + uriInfo.getPath())));

		return wrapServiceErrorResponse(serviceErrorResponse);
	}
}
