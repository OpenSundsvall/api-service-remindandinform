package se.sundsvall.remindandinform.api.exception.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.remindandinform.api.exception.ServiceException;
import se.sundsvall.remindandinform.api.exception.mappers.ServiceExceptionMapper;
import se.sundsvall.remindandinform.api.exception.model.ServiceErrorResponse;
import se.sundsvall.remindandinform.api.exception.model.TechnicalDetails;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static javax.ws.rs.core.Response.Status.GATEWAY_TIMEOUT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceExceptionMapperTest {

	private static final String REQUEST_PATH = "http://localhost:1234/path";
	private static final String SERVICE_ID = "Some service-ID";
	private static final String EXCEPTION_MESSAGE = "Service error";
	private static final String ROOT_CAUSE = "Gateway timeout during call";
	private static final int ROOT_CODE = Status.GATEWAY_TIMEOUT.getStatusCode();

	private ServiceException serviceException;

	@Mock
	private UriInfo uriInfoMock;

	@InjectMocks
	private ServiceExceptionMapper exceptionMapper;

	@BeforeEach
	void setup() {

		serviceException = ServiceException.create(EXCEPTION_MESSAGE)
			.withStatus(GATEWAY_TIMEOUT)
			.withTechnicalDetails(TechnicalDetails.create()
				.withRootCause(ROOT_CAUSE)
				.withRootCode(ROOT_CODE)
				.withServiceId(SERVICE_ID)
				.withDetails(List.of("Special detail")));

		when(uriInfoMock.getPath()).thenReturn(REQUEST_PATH);
	}

	@Test
	void serviceException() {

		final var response = exceptionMapper.toResponse(serviceException).readEntity(ServiceErrorResponse.class);

		assertThat(response).isEqualTo(ServiceErrorResponse.create()
			.withMessage(EXCEPTION_MESSAGE)
			.withHttpCode(ROOT_CODE)
			.withTechnicalDetails(TechnicalDetails.create()
				.withRootCode(ROOT_CODE)
				.withRootCause(ROOT_CAUSE)
				.withServiceId(SERVICE_ID)
				.withDetails(List.of("Special detail", "Request: ".concat(REQUEST_PATH)))));
	}
}
