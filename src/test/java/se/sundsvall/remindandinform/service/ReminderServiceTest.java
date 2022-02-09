package se.sundsvall.remindandinform.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.remindandinform.api.exception.ServiceException;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;

import javax.ws.rs.core.Response.Status;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

	@Mock
	private ReminderRepository reminderRepositoryMock;

	@InjectMocks
	private ReminderService reminderService;

	@Captor
	private ArgumentCaptor<ReminderEntity> reminderEntityArgumentCaptor;

	@Test
	void findByPersonIdSuccess() throws ServiceException {

		// Parameters
		final var personId = "personId";
		final var reminderId1 = "reminderId1";
		final var reminderId2 = "reminderId2";

		final var reminderEntity1 = new ReminderEntity();
		reminderEntity1.setReminderId(reminderId1);
		reminderEntity1.setPersonId(personId);

		final var reminderEntity2 = new ReminderEntity();
		reminderEntity2.setReminderId(reminderId2);
		reminderEntity2.setPersonId(personId);

		when(reminderRepositoryMock.findByPersonId(personId)).thenReturn(List.of(reminderEntity1, reminderEntity2));

		final var reminders = reminderService.findRemindersByPersonId(personId);

		assertThat(reminders).isNotNull().hasSize(2);
		assertThat(reminders.get(0).getReminderId()).isEqualTo(reminderId1);
		assertThat(reminders.get(0).getPersonId()).isEqualTo(personId);
		assertThat(reminders.get(1).getReminderId()).isEqualTo(reminderId2);
		assertThat(reminders.get(1).getPersonId()).isEqualTo(personId);

		verify(reminderRepositoryMock).findByPersonId(personId);
	}

	@Test
	void findByPersonIdNotFound() {

		// Parameters
		final var personId = "personId";

		when(reminderRepositoryMock.findByPersonId(personId)).thenReturn(Collections.emptyList());

		final var serviceException = assertThrows(ServiceException.class, () -> reminderService.findRemindersByPersonId(personId));

		assertThat(serviceException.getMessage()).isEqualTo("No reminders found for personId:'personId'");
		assertThat(serviceException.getStatus()).isEqualTo(Status.NOT_FOUND);

		verify(reminderRepositoryMock).findByPersonId(personId);
	}

	@Test
	void deleteByReminderIdSuccess() throws ServiceException {

		// Parameters
		final var reminderId = "reminderId";

		when(reminderRepositoryMock.deleteReminderByReminderId(reminderId)).thenReturn(1L);

		reminderService.deleteReminderByReminderId(reminderId);

		verify(reminderRepositoryMock).deleteReminderByReminderId(reminderId);
	}

	@Test
	void updateReminderSuccess() throws ServiceException {
		// Parameters
		final var reminderId = "reminderId";
		final var personId = "personId";
		final var organizationId = "organizationId";
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var action = "action";
		final var reminderDate = LocalDate.now();

		final var updatedPersonId = "updatePersonId";
		final var updatedOrganizationId = "updatedOrganizationId";
		final var updatedCaseId = "updatedCaseId";
		final var updatedCaseLink = "updatedCaseLink";
		final var updatedAction = "updatedAction";
		final var updatedReminderDate = LocalDate.now().plusDays(10);

		final var updateReminderRequest = UpdateReminderRequest.create()
																.withOrganizationId(updatedOrganizationId)
																.withPersonId(updatedPersonId)
																.withReminderDate(updatedReminderDate)
																.withCaseId(updatedCaseId)
																.withCaseLink(updatedCaseLink)
																.withAction(updatedAction);

		//Create existing ReminderEntity
		final var reminderEntity = new ReminderEntity();
		reminderEntity.setReminderDate(reminderDate);
		reminderEntity.setReminderId(reminderId);
		reminderEntity.setOrganizationId(organizationId);
		reminderEntity.setPersonId(personId);
		reminderEntity.setCaseId(caseId);
		reminderEntity.setCaseLink(caseLink);
		reminderEntity.setAction(action);

		//Create updated ReminderEntity
		final var updatedReminderEntity = new ReminderEntity();
		reminderEntity.setReminderDate(updatedReminderDate);
		reminderEntity.setReminderId(reminderId);
		reminderEntity.setOrganizationId(updatedOrganizationId);
		reminderEntity.setPersonId(updatedPersonId);
		reminderEntity.setCaseId(updatedCaseId);
		reminderEntity.setCaseLink(updatedCaseLink);
		reminderEntity.setAction(updatedAction);

		when(reminderRepositoryMock.findByReminderId(reminderId)).thenReturn(Optional.of(reminderEntity));
		when(reminderRepositoryMock.persistAndFetch(any(ReminderEntity.class))).thenReturn(updatedReminderEntity);

		final var reminder = reminderService.updateReminder(updateReminderRequest, reminderId);

		verify(reminderRepositoryMock).persistAndFetch(reminderEntityArgumentCaptor.capture());

		verify(reminderRepositoryMock).findByReminderId(reminderId);

		final var reminderEntityCaptorValue = reminderEntityArgumentCaptor.getValue();
		assertThat(reminderEntityCaptorValue).isNotNull();
		assertThat(reminderEntityCaptorValue.getReminderId()).isEqualTo(reminderId);
		assertThat(reminderEntityCaptorValue.getPersonId()).isEqualTo(updatedPersonId);
		assertThat(reminderEntityCaptorValue.getOrganizationId()).isEqualTo(updatedOrganizationId);
		assertThat(reminderEntityCaptorValue.getCaseId()).isEqualTo(updatedCaseId);
		assertThat(reminderEntityCaptorValue.getCaseLink()).isEqualTo(updatedCaseLink);
		assertThat(reminderEntityCaptorValue.getAction()).isEqualTo(updatedAction);
		assertThat(reminderEntityCaptorValue.getReminderDate()).isEqualTo(updatedReminderDate);
	}

	@Test
	void deleteByReminderIdNotFound() {

		// Parameters
		final var reminderId = "reminderId";

		when(reminderRepositoryMock.deleteReminderByReminderId(reminderId)).thenReturn(0L);

		final var serviceException = assertThrows(ServiceException.class, () -> reminderService.deleteReminderByReminderId(reminderId));

		assertThat(serviceException.getMessage()).isEqualTo("No reminder found for reminderId:'reminderId'");
		assertThat(serviceException.getStatus()).isEqualTo(Status.NOT_FOUND);

		verify(reminderRepositoryMock).deleteReminderByReminderId(reminderId);
	}

	@Test
	void findByPersonIdAndOrganizationIdSuccess() throws ServiceException {

		// Parameters
		final var personId = "personId";
		final var organizationId = "organizationId";
		final var reminderId1 = "reminderId1";
		final var reminderId2 = "reminderId2";

		final var reminderEntity1 = new ReminderEntity();
		reminderEntity1.setReminderId(reminderId1);
		reminderEntity1.setPersonId(personId);
		reminderEntity1.setOrganizationId(organizationId);

		final var reminderEntity2 = new ReminderEntity();
		reminderEntity2.setReminderId(reminderId2);
		reminderEntity2.setPersonId(personId);
		reminderEntity2.setOrganizationId(organizationId);

		when(reminderRepositoryMock.findByPersonIdAndOrganizationId(personId, organizationId)).thenReturn(List.of(reminderEntity1, reminderEntity2));

		final var reminders = reminderService.findRemindersByPersonIdAndOrganizationId(personId, organizationId);

		assertThat(reminders).isNotNull().hasSize(2);
		assertThat(reminders.get(0).getReminderId()).isEqualTo(reminderId1);
		assertThat(reminders.get(0).getPersonId()).isEqualTo(personId);
		assertThat(reminders.get(0).getOrganizationId()).isEqualTo(organizationId);
		assertThat(reminders.get(1).getReminderId()).isEqualTo(reminderId2);
		assertThat(reminders.get(1).getPersonId()).isEqualTo(personId);
		assertThat(reminders.get(1).getOrganizationId()).isEqualTo(organizationId);

		verify(reminderRepositoryMock).findByPersonIdAndOrganizationId(personId, organizationId);
	}

	@Test
	void findByPersonIdAndOrganizaitionIdNotFound() {

		// Parameters
		final var personId = "personId";
		final var organizationId = "organizationId";

		when(reminderRepositoryMock.findByPersonIdAndOrganizationId(personId, organizationId)).thenReturn(Collections.emptyList());

		final var serviceException = assertThrows(ServiceException.class, () -> reminderService.findRemindersByPersonIdAndOrganizationId(personId, organizationId));

		assertThat(serviceException.getMessage()).isEqualTo("No reminder found for personId:'personId' and organizationId:'organizationId'");
		assertThat(serviceException.getStatus()).isEqualTo(Status.NOT_FOUND);

		verify(reminderRepositoryMock).findByPersonIdAndOrganizationId(personId, organizationId);
	}

	@Test
	void createReminder() {

		final var personId = "personId";
		final var organizationId = "organizationId";
		final var action = "action";
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var reminderDate = LocalDate.now();

		// Parameters
		final var reminderRequest = ReminderRequest.create()
				.withPersonId(personId)
				.withOrganizationId(organizationId)
				.withAction(action)
				.withCaseId(caseId)
				.withCaseLink(caseLink)
				.withReminderDate(reminderDate);

		when(reminderRepositoryMock.persistAndFetch(any(ReminderEntity.class))).thenReturn(new ReminderEntity());

		final var reminder = reminderService.createReminder(reminderRequest);
		assertThat(reminder).isNotNull();

		verify(reminderRepositoryMock).persistAndFetch(reminderEntityArgumentCaptor.capture());

		final var reminderEntityCaptorValue = reminderEntityArgumentCaptor.getValue();
		assertThat(reminderEntityCaptorValue).isNotNull();
		assertThat(reminderEntityCaptorValue.getReminderId()).startsWith("R-");
		assertThat(reminderEntityCaptorValue.getPersonId()).isEqualTo(personId);
		assertThat(reminderEntityCaptorValue.getOrganizationId()).isEqualTo(organizationId);
		assertThat(reminderEntityCaptorValue.getCaseId()).isEqualTo(caseId);
		assertThat(reminderEntityCaptorValue.getCaseLink()).isEqualTo(caseLink);
		assertThat(reminderEntityCaptorValue.getAction()).isEqualTo(action);
		assertThat(reminderEntityCaptorValue.getReminderDate()).isEqualTo(reminderDate);
	}
}
