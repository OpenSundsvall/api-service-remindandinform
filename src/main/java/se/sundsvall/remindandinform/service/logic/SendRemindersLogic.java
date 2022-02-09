package se.sundsvall.remindandinform.service.logic;

import generated.se.sundsvall.messaging.Message;
import generated.se.sundsvall.messaging.MessageRequest;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sundsvall.remindandinform.api.exception.ServiceException;
import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;
import se.sundsvall.remindandinform.integration.messaging.ApiMessagingClient;
import se.sundsvall.remindandinform.service.mapper.ReminderMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class SendRemindersLogic {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendRemindersLogic.class);

	@Inject
	ReminderRepository reminderRepository;

	@Inject
	@RestClient
	ApiMessagingClient apiMessagingClient;

	@Scheduled(cron = "{sendReminders.cron.expr}")
	@Transactional
	public void sendReminders() throws ServiceException {
		sendReminders(LocalDate.now());
	}

	@Transactional
	public void sendReminders(LocalDate reminderDate) throws ServiceException {
		final var reminders = findRemindersToSendByReminderDate(reminderDate);

		final var messages = createMessages(reminders);

		if (!messages.isEmpty()) {
			final var messageRequest = new MessageRequest().messages(messages);
			LOGGER.debug("Messages to send to api-messaging-service: '{}'", messageRequest);
			apiMessagingClient.sendMessage(new MessageRequest().messages(messages));
			reminderRepository.updateSentByReminderDate(reminderDate);
			LOGGER.info("{} reminders sent for reminderDate: '{}'", messages.size(), reminderDate);
		}
	}


	private List<Reminder> findRemindersToSendByReminderDate(LocalDate reminderDate) {
		var reminders = ReminderMapper.toReminders(reminderRepository.findByReminderDateNotSent(reminderDate));

		if(reminders.isEmpty()) {
			LOGGER.info("No reminders found for reminderDate: '{}'", reminderDate);
		}

		return reminders;
	}

	private List<Message> createMessages(List<Reminder> reminders) {
		return reminders.stream()
				.filter(Objects::nonNull)
				.map(ReminderMapper::toMessage)
				.toList();
	}
}
