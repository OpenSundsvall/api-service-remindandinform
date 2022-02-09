package se.sundsvall.remindandinform.service.logic;

import generated.se.sundsvall.messaging.MessageStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.remindandinform.api.exception.ServiceException;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;
import se.sundsvall.remindandinform.integration.messaging.ApiMessagingClient;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendRemindersLogicTest {

	private static final String personId1 = "personId1";
	private static final String personId2 = "personId2";
	private static final String reminderId1 = "reminderId1";
	private static final String reminderId2 = "reminderId2";
	private static final String action1 = "action1";
	private static final String action2 = "action2";
	private static final String caseId1 = "caseId1";
	private static final String caseId2 = "caseId2";
	private static final String caseLink1 = "caseLink1";
	private static final String caseLink2 = "caseLink2";

	private static final ReminderEntity reminderEntity1 = new ReminderEntity();
	private static final ReminderEntity reminderEntity2 = new ReminderEntity();

	@Mock
	private ReminderRepository reminderRepositoryMock;

	@Mock
	private ApiMessagingClient apiMessagingClientMock;

	@InjectMocks
	private SendRemindersLogic sendRemindersLogic;

	@BeforeEach
	void setup() {
		// Parameters
		reminderEntity1.setReminderId(reminderId1);
		reminderEntity1.setPersonId(personId1);
		reminderEntity1.setAction(action1);
		reminderEntity1.setCaseId(caseId1);
		reminderEntity1.setCaseLink(caseLink1);

		reminderEntity2.setReminderId(reminderId2);
		reminderEntity2.setPersonId(personId2);
		reminderEntity2.setAction(action2);
		reminderEntity2.setCaseId(caseId2);
		reminderEntity2.setCaseLink(caseLink2);
	}

	@Test
	void sendRemindersSuccess() throws ServiceException {

		when(reminderRepositoryMock.findByReminderDateNotSent(LocalDate.now())).thenReturn(List.of(reminderEntity1, reminderEntity2));

		when(apiMessagingClientMock.sendMessage(any())).thenReturn(new MessageStatusResponse().sent(true));

		sendRemindersLogic.sendReminders();

		verify(reminderRepositoryMock).findByReminderDateNotSent(LocalDate.now());
		verify(apiMessagingClientMock).sendMessage(any());
	}

	@Test
	void sendRemindersNotFound() throws ServiceException {

		when(reminderRepositoryMock.findByReminderDateNotSent(LocalDate.now())).thenReturn(Collections.emptyList());

		sendRemindersLogic.sendReminders();

		verify(reminderRepositoryMock).findByReminderDateNotSent(LocalDate.now());
		verify(apiMessagingClientMock, never()).sendMessage(any());
	}

	@Test
	void sendRemindersMessagesNotSent() throws ServiceException {

		when(reminderRepositoryMock.findByReminderDateNotSent(LocalDate.now())).thenReturn(List.of(reminderEntity1, reminderEntity2));

		when(apiMessagingClientMock.sendMessage(any())).thenReturn(new MessageStatusResponse().sent(false).messageId("messageId"));

		sendRemindersLogic.sendReminders();

		verify(reminderRepositoryMock).findByReminderDateNotSent(LocalDate.now());
		verify(apiMessagingClientMock).sendMessage(any());
	}
}
