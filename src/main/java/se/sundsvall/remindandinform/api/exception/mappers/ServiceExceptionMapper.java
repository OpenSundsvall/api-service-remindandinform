package se.sundsvall.remindandinform.api.exception.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sundsvall.remindandinform.api.exception.ServiceException;
import se.sundsvall.remindandinform.api.exception.model.ServiceErrorResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Provider
public class ServiceExceptionMapper extends AbstractExceptionMapper<ServiceException> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionMapper.class);

	@Override
	public Response toResponse(final ServiceException e) {

		LOGGER.info("Mapping exception into ServiceErrorResponse: {} ", e.getMessage());

		ServiceErrorResponse serviceErrorResponse = ServiceErrorResponse.createFrom(e)
			.withTechnicalDetails(
				e.getTechnicalDetails()
					.withServiceId(e.getTechnicalDetails().getServiceId() != null ? e.getTechnicalDetails().getServiceId() : getApplicationName())
					.withDetails(mergeLists(
						e.getTechnicalDetails().getDetails(),
						List.of("Request: " + uriInfo.getPath()))));

		return wrapServiceErrorResponse(serviceErrorResponse);
	}

	@SafeVarargs
	private List<String> mergeLists(List<String>... lists) {
		return Stream.of(lists).filter(Objects::nonNull).flatMap(Collection::stream).toList();
	}
}
