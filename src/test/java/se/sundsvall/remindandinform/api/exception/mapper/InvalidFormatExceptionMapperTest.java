package se.sundsvall.remindandinform.api.exception.mapper;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.eclipse.microprofile.config.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.remindandinform.api.exception.mappers.InvalidFormatExceptionMapper;
import se.sundsvall.remindandinform.api.exception.model.ServiceErrorResponse;
import se.sundsvall.remindandinform.api.exception.model.TechnicalDetails;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvalidFormatExceptionMapperTest {

	private static final String REQUEST_PATH = "http://localhost:1234/path";
	private static final String APPLICATION_NAME = "The-app";
	private static final String EXCEPTION_MESSAGE = "Bad request format!";

	@Mock
	private InvalidFormatException invalidFormatExceptionMock;

	@Mock
	private UriInfo uriInfoMock;

	@Mock
	private Config configMock;

	@InjectMocks
	private InvalidFormatExceptionMapper exceptionMapper;

	@BeforeEach
	void setup() {

		when(configMock.getOptionalValue("quarkus.application.name", String.class)).thenReturn(Optional.of(APPLICATION_NAME));
		when(uriInfoMock.getPath()).thenReturn(REQUEST_PATH);
	}

	@Test
	void invalidFormatException() {

		final var response = exceptionMapper.toResponse(invalidFormatExceptionMock).readEntity(ServiceErrorResponse.class);

		assertThat(response).isEqualTo(ServiceErrorResponse.create()
			.withMessage("Request validation failed!")
			.withHttpCode(BAD_REQUEST.getStatusCode())
			.withTechnicalDetails(TechnicalDetails.create()
				.withRootCode(BAD_REQUEST.getStatusCode())
				.withRootCause(EXCEPTION_MESSAGE)
				.withServiceId(APPLICATION_NAME)
				.withDetails(List.of("Request: ".concat(REQUEST_PATH)))));
	}
}
