package se.sundsvall.remindandinform.integration.messaging.mapper;

import generated.se.sundsvall.messaging.ServiceErrorResponse;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sundsvall.remindandinform.api.exception.ServiceException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class MessagingExceptionMapper implements ResponseExceptionMapper<ServiceException> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessagingExceptionMapper.class);

	protected static final String INTEGRATION_NAME = "api-messaging-service";
	private static final String EXCEPTION_MESSAGE = "Error calling api-messaging";

	@Override
	public ServiceException toThrowable(Response response) {
		LOGGER.debug("Received response with status code '{}'", response.getStatus());

		switch (response.getStatusInfo().getFamily()) {
		case CLIENT_ERROR:
		case SERVER_ERROR:
			LOGGER.info("Mapping response with status code '{}' into ServiceException", response.getStatus());
			return mapToServiceException(response);

		default:
			return null;
		}
	}

	private <T> T getResponseBody(Response response, Class<T> responseType) {
		var responseBody = response.readEntity(responseType);
		LOGGER.debug("Extracting body from '{}': {}", INTEGRATION_NAME, responseBody);
		return responseBody;
	}

	private ServiceException mapToServiceException(Response response) {
		// Normal error body.
		if (response.getMediaType().isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
			ServiceErrorResponse error = getResponseBody(response, ServiceErrorResponse.class);
			return ServiceException.create(EXCEPTION_MESSAGE, error.getTechnicalDetails().getServiceId(),
				Status.BAD_GATEWAY, Status.fromStatusCode(error.getHttpCode()),
				Optional.ofNullable(error.getTechnicalDetails().getDetails()).orElse(emptyList()).toArray(String[]::new));
		}

		// Response body is not json.
		return ServiceException.create(EXCEPTION_MESSAGE,
			INTEGRATION_NAME,
			Status.BAD_GATEWAY,
			Status.fromStatusCode(response.getStatus()),
			getResponseBody(response, String.class));
	}
}
