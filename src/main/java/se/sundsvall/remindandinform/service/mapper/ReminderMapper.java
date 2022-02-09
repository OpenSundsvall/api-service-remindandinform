package se.sundsvall.remindandinform.service.mapper;

import generated.se.sundsvall.messaging.Message;
import generated.se.sundsvall.messaging.Sender;
import se.sundsvall.remindandinform.api.exception.ServiceException;
import se.sundsvall.remindandinform.api.exception.ServiceRuntimeException;
import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.eclipse.microprofile.config.ConfigProvider.getConfig;

public class ReminderMapper {

	private static final String REMINDER_MESSAGE = getConfigValue("reminder.message");

	private static final String SUBJECT = getConfigValue("reminder.subject");
	private static final String SENDER_EMAIL_NAME = getConfigValue("reminder.sender.email.name");
	private static final String SENDER_EMAIL_ADDRESS = getConfigValue("reminder.sender.email.address");
	private static final String SENDER_SMS_NAME = getConfigValue("reminder.sender.sms.name");

	private ReminderMapper() {}

	public static Reminder toReminder(ReminderEntity reminderEntity) {
		return Reminder.create()
				.withReminderId(reminderEntity.getReminderId())
				.withPersonId(reminderEntity.getPersonId())
				.withOrganizationId(reminderEntity.getOrganizationId())
				.withAction(reminderEntity.getAction())
				.withCaseId(reminderEntity.getCaseId())
				.withCaseLink(reminderEntity.getCaseLink())
				.withReminderDate(reminderEntity.getReminderDate());
	}

	public static ReminderEntity toReminderEntity(ReminderRequest reminderRequest, String reminderId) {
		final var reminderEntity = new ReminderEntity();
		reminderEntity.setReminderId(reminderId);
		reminderEntity.setPersonId(reminderRequest.getPersonId());
		reminderEntity.setOrganizationId(reminderRequest.getOrganizationId());
		reminderEntity.setCaseId(reminderRequest.getCaseId());
		reminderEntity.setCaseLink(reminderRequest.getCaseLink());
		reminderEntity.setAction(reminderRequest.getAction());
		reminderEntity.setReminderDate(reminderRequest.getReminderDate());

		return reminderEntity;
	}

	public static ReminderEntity toReminderEntity(UpdateReminderRequest updateReminderRequest, String reminderId) {
		final var reminderEntity = new ReminderEntity();
		reminderEntity.setReminderId(reminderId);
		reminderEntity.setPersonId(updateReminderRequest.getPersonId());
		reminderEntity.setOrganizationId(updateReminderRequest.getOrganizationId());
		reminderEntity.setCaseId(updateReminderRequest.getCaseId());
		reminderEntity.setCaseLink(updateReminderRequest.getCaseLink());
		reminderEntity.setAction(updateReminderRequest.getAction());
		reminderEntity.setReminderDate(updateReminderRequest.getReminderDate());

		return reminderEntity;
	}

	public static ReminderEntity toMergedReminderEntity(ReminderEntity oldEntity, ReminderEntity newEntity) {

		if (nonNull(newEntity.getPersonId())) {
			oldEntity.setPersonId(newEntity.getPersonId());
		}
		if (nonNull(newEntity.getOrganizationId())) {
			oldEntity.setOrganizationId(newEntity.getOrganizationId());
		}
		if (nonNull(newEntity.getCaseId())) {
			oldEntity.setCaseId(newEntity.getCaseId());
		}
		if (nonNull(newEntity.getCaseLink())) {
			oldEntity.setCaseLink(newEntity.getCaseLink());
		}
		if (nonNull(newEntity.getAction())) {
			oldEntity.setAction(newEntity.getAction());
		}
		if (nonNull(newEntity.getReminderDate())) {
			oldEntity.setReminderDate(newEntity.getReminderDate());
		}

		return oldEntity;
	}

	public static List<Reminder> toReminders(List<ReminderEntity> reminderEntities)  {
		return reminderEntities.stream().filter(Objects::nonNull)
				.map(ReminderMapper::toReminder)
				.toList();
	}

	public static Message toMessage(Reminder reminder) {
		final var messageText = format(REMINDER_MESSAGE, reminder.getAction(), reminder.getCaseId(), reminder.getCaseLink());
		final var sender = new Sender().emailAddress(SENDER_EMAIL_ADDRESS)
				.emailName(SENDER_EMAIL_NAME)
				.smsName(SENDER_SMS_NAME);

		return new Message()
				.sender(sender)
				.partyId(reminder.getPersonId())
				.message(messageText)
				.subject(SUBJECT);
	}

	private static String getConfigValue(String key) {
		return getConfig().getOptionalValue(key, String.class).orElseThrow(() -> new ServiceRuntimeException(ServiceException.create(String.format("No property set for '%s'", key), Response.Status.INTERNAL_SERVER_ERROR)));
	}
}