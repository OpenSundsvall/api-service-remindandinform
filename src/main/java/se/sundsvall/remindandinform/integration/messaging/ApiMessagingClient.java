package se.sundsvall.remindandinform.integration.messaging;

import generated.se.sundsvall.messaging.MessageRequest;
import generated.se.sundsvall.messaging.MessageStatusResponse;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import se.sundsvall.remindandinform.api.exception.ServiceException;
import se.sundsvall.remindandinform.integration.messaging.mapper.MessagingExceptionMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "api-messaging")
@RegisterProvider(MessagingExceptionMapper.class)
@RegisterProvider(ApiMessagingOidcClientRequestFilter.class)
@ApplicationScoped
public interface ApiMessagingClient {

	/**
	 * Send messages as email or SMS to list of recipients.
	 * 
	 * @param messageRequest with a list of messages.
	 * @return a MessageStatusResponse
	 * @throws ServiceException
	 */
	@POST
	MessageStatusResponse sendMessage(MessageRequest messageRequest) throws ServiceException;
}
