package se.sundsvall.remindandinform.integration.messaging;

import io.quarkus.oidc.client.NamedOidcClient;
import io.quarkus.oidc.client.Tokens;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

@Priority(Priorities.AUTHENTICATION)
@RequestScoped
public class ApiMessagingOidcClientRequestFilter implements ClientRequestFilter {

	@Inject
	@NamedOidcClient("api-messaging")
	Tokens tokens;

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken());
	}
}
