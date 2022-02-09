package se.sundsvall.remindandinform.api.exception;

/**
 * RuntimeException wrapper over ServiceException.
 */
public class ServiceRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -6395176060554455917L;

	public ServiceRuntimeException(se.sundsvall.remindandinform.api.exception.ServiceException exception) {
		super(exception);
	}

	@Override
	public String getMessage() {
		return super.getCause().getMessage();
	}

	public se.sundsvall.remindandinform.api.exception.ServiceException getTypedCause() {
		return (se.sundsvall.remindandinform.api.exception.ServiceException) super.getCause();
	}
}
