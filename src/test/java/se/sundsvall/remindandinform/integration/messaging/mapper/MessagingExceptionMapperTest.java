package se.sundsvall.remindandinform.integration.messaging.mapper;

import generated.se.sundsvall.messaging.ServiceErrorResponse;
import generated.se.sundsvall.messaging.TechnicalDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.remindandinform.api.exception.ServiceException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MessagingExceptionMapperTest {

	@Mock
	private Response responseMock;
	
	@Test
	void testClientErrorResponse() {
		
		when(responseMock.getStatus()).thenReturn(Status.BAD_REQUEST.getStatusCode());
		when(responseMock.getStatusInfo()).thenReturn(Status.BAD_REQUEST);
		when(responseMock.getMediaType()).thenReturn(MediaType.APPLICATION_JSON_TYPE);
		when(responseMock.readEntity(ServiceErrorResponse.class)).thenReturn(new ServiceErrorResponse()
				.httpCode(400)
				.message("Bad request")
				.technicalDetails(new TechnicalDetails()
						.rootCode(400)
						.serviceId("called-service")
						.details(List.of("error1", "error2"))));
		
		MessagingExceptionMapper mapper = new MessagingExceptionMapper();
		ServiceException result = mapper.toThrowable(responseMock);
		
		assertThat(result).isNotNull();
		assertThat(result.getMessage()).isEqualTo("Error calling api-messaging");
		assertThat(result.getStatus()).isEqualTo(Status.BAD_GATEWAY);
		assertThat(result.getTechnicalDetails().getRootCode()).isEqualTo(Status.BAD_REQUEST.getStatusCode());
		assertThat(result.getTechnicalDetails().getRootCause()).isEqualTo("Bad Request");
		assertThat(result.getTechnicalDetails().getServiceId()).isEqualTo("called-service");
		assertThat(result.getTechnicalDetails().getDetails()).containsExactly("error1", "error2");
	}
	
	@Test
	void testServerErrorResponse() {
		
		when(responseMock.getStatus()).thenReturn(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		when(responseMock.getStatusInfo()).thenReturn(Status.INTERNAL_SERVER_ERROR);
		when(responseMock.getMediaType()).thenReturn(MediaType.APPLICATION_JSON_TYPE);
		when(responseMock.readEntity(ServiceErrorResponse.class)).thenReturn(new ServiceErrorResponse()
				.httpCode(500)
				.message("Service is not up and running")
				.technicalDetails(new TechnicalDetails()
						.rootCode(500)
						.serviceId("called-service")
						.details(List.of("error1", "error2"))));
		
		MessagingExceptionMapper mapper = new MessagingExceptionMapper();
		ServiceException result = mapper.toThrowable(responseMock);
		
		assertThat(result).isNotNull();
		assertThat(result.getMessage()).isEqualTo("Error calling api-messaging");
		assertThat(result.getStatus()).isEqualTo(Status.BAD_GATEWAY);
		assertThat(result.getTechnicalDetails().getRootCode()).isEqualTo(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		assertThat(result.getTechnicalDetails().getRootCause()).isEqualTo("Internal Server Error");
		assertThat(result.getTechnicalDetails().getServiceId()).isEqualTo("called-service");
		assertThat(result.getTechnicalDetails().getDetails()).containsExactly("error1", "error2");
	}
	
	@Test
	void testServerErrorResponseWithNonJsonMediaType() {
		
		when(responseMock.getStatus()).thenReturn(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		when(responseMock.getStatusInfo()).thenReturn(Status.INTERNAL_SERVER_ERROR);
		when(responseMock.getMediaType()).thenReturn(MediaType.TEXT_HTML_TYPE);
		when(responseMock.readEntity(String.class)).thenReturn("<body>Something went wrong</body>");
		
		MessagingExceptionMapper mapper = new MessagingExceptionMapper();
		ServiceException result = mapper.toThrowable(responseMock);
		
		assertThat(result).isNotNull();
		assertThat(result.getMessage()).isEqualTo("Error calling api-messaging");
		assertThat(result.getStatus()).isEqualTo(Status.BAD_GATEWAY);
		assertThat(result.getTechnicalDetails().getRootCode()).isEqualTo(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		assertThat(result.getTechnicalDetails().getRootCause()).isEqualTo("Internal Server Error");
		assertThat(result.getTechnicalDetails().getServiceId()).isEqualTo(MessagingExceptionMapper.INTEGRATION_NAME);
		assertThat(result.getTechnicalDetails().getDetails()).containsExactly("<body>Something went wrong</body>");
	}
	
	@Test
	void testOKResponse() {
		
		when(responseMock.getStatus()).thenReturn(Status.OK.getStatusCode());
		when(responseMock.getStatusInfo()).thenReturn(Status.OK);
		
		MessagingExceptionMapper mapper = new MessagingExceptionMapper();
		ServiceException result = mapper.toThrowable(responseMock);
		
		assertThat(result).isNull();
	}
}
