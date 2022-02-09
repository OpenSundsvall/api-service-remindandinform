package se.sundsvall.remindandinform.api.exception.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sundsvall.remindandinform.api.exception.model.ServiceErrorResponse;
import se.sundsvall.remindandinform.api.exception.model.TechnicalDetails;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Provider
public class DefaultExceptionMapper extends AbstractExceptionMapper<Exception> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionMapper.class);
	
	@Override
	public Response toResponse(Exception e) {
		
		LOGGER.info("Mapping exception into ServiceErrorResponse: ", e);
		
		ServiceErrorResponse serviceErrorResponse = ServiceErrorResponse.create()
				.withMessage("Service error!").withHttpCode(INTERNAL_SERVER_ERROR.getStatusCode())
				.withTechnicalDetails(TechnicalDetails.create()
						.withRootCode(INTERNAL_SERVER_ERROR.getStatusCode())
						.withRootCause(extractMessage(e))
						.withServiceId(getApplicationName())
						.withDetails(List.of("Type: " + e.getClass().getSimpleName(),"Request: " + uriInfo.getPath())));

		return wrapServiceErrorResponse(serviceErrorResponse);
	}
	
	private String extractMessage(Exception e) {
		return Optional.ofNullable(e.getMessage()).orElse(String.valueOf(e));
	}
}
