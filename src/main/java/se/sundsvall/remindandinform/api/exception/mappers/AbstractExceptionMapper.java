package se.sundsvall.remindandinform.api.exception.mappers;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import se.sundsvall.remindandinform.api.exception.model.ServiceErrorResponse;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;

public abstract class AbstractExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {

	@Context
	protected UriInfo uriInfo;

	protected Config config = ConfigProvider.getConfig();

	protected Response wrapServiceErrorResponse(final ServiceErrorResponse serviceErrorResponse) {
		return Response.status(serviceErrorResponse.getHttpCode()).entity(serviceErrorResponse).build();
	}

	protected String getApplicationName() {
		return config.getOptionalValue("quarkus.application.name", String.class).orElse("Unknown");
	}
}
