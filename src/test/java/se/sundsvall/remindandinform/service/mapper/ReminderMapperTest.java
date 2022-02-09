package se.sundsvall.remindandinform.service.mapper;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class ReminderMapperTest {

	@Test
	void toReminderSuccess() {

		final var reminderEntity = new ReminderEntity();
		reminderEntity.setReminderId("reminderId");
		reminderEntity.setPersonId("personId");
		reminderEntity.setOrganizationId("organizationId");
		reminderEntity.setAction("action");
		reminderEntity.setCaseLink("caseLink");
		reminderEntity.setCaseId("caseId");
		reminderEntity.setReminderDate(LocalDate.now());

		final var reminder = ReminderMapper.toReminder(reminderEntity);

		assertThat(reminder.getReminderId()).isEqualTo("reminderId");
		assertThat(reminder.getPersonId()).isEqualTo("personId");
		assertThat(reminder.getOrganizationId()).isEqualTo("organizationId");
		assertThat(reminder.getCaseId()).isEqualTo("caseId");
		assertThat(reminder.getCaseLink()).isEqualTo("caseLink");
		assertThat(reminder.getAction()).isEqualTo("action");
		assertThat(reminder.getReminderDate()).isEqualTo(LocalDate.now());
	}

	@Test
	void toReminderFromReminderRequestEntitySuccess() {

		final var personId = "personId";
		final var organizationId = "organizationId";
		final var action = "action";
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var reminderId = "reminderId";
		final var reminderDate = LocalDate.now();

		// Parameters
		final var reminderRequest = ReminderRequest.create()
				.withPersonId(personId)
				.withOrganizationId(organizationId)
				.withAction(action)
				.withCaseId(caseId)
				.withCaseLink(caseLink)
				.withReminderDate(reminderDate);

		final var reminderEntity = ReminderMapper.toReminderEntity(reminderRequest, reminderId);

		assertThat(reminderEntity.getReminderId()).isEqualTo(reminderId);
		assertThat(reminderEntity.getPersonId()).isEqualTo(personId);
		assertThat(reminderEntity.getOrganizationId()).isEqualTo(organizationId);
		assertThat(reminderEntity.getCaseId()).isEqualTo(caseId);
		assertThat(reminderEntity.getCaseLink()).isEqualTo(caseLink);
		assertThat(reminderEntity.getAction()).isEqualTo(action);
		assertThat(reminderEntity.getReminderDate()).isEqualTo(LocalDate.now());
	}

	@Test
	void toReminderFromUpdateReminderRequestEntitySuccess() {

		final var personId = "personId";
		final var organizationId = "organizationId";
		final var action = "action";
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var reminderId = "reminderId";
		final var reminderDate = LocalDate.now();

		// Parameters
		final var updateReminderRequest = UpdateReminderRequest.create()
				.withPersonId(personId)
				.withOrganizationId(organizationId)
				.withAction(action)
				.withCaseId(caseId)
				.withCaseLink(caseLink)
				.withReminderDate(reminderDate);

		final var reminderEntity = ReminderMapper.toReminderEntity(updateReminderRequest, reminderId);

		assertThat(reminderEntity.getReminderId()).isEqualTo(reminderId);
		assertThat(reminderEntity.getPersonId()).isEqualTo(personId);
		assertThat(reminderEntity.getOrganizationId()).isEqualTo(organizationId);
		assertThat(reminderEntity.getCaseId()).isEqualTo(caseId);
		assertThat(reminderEntity.getCaseLink()).isEqualTo(caseLink);
		assertThat(reminderEntity.getAction()).isEqualTo(action);
		assertThat(reminderEntity.getReminderDate()).isEqualTo(LocalDate.now());
	}

	@Test
	void toRemindersSuccess() {

		final var reminders = ReminderMapper.toReminders(createReminderEntities());

		assertThat(reminders.get(0).getReminderId()).isEqualTo("reminderId1");
		assertThat(reminders.get(0).getPersonId()).isEqualTo("personId1");
		assertThat(reminders.get(0).getOrganizationId()).isEqualTo("organizationId1");
		assertThat(reminders.get(0).getCaseId()).isEqualTo("caseId1");
		assertThat(reminders.get(0).getCaseLink()).isEqualTo("caseLink1");
		assertThat(reminders.get(0).getAction()).isEqualTo("action1");
		assertThat(reminders.get(0).getReminderDate()).isEqualTo(LocalDate.now());

		assertThat(reminders.get(1).getReminderId()).isEqualTo("reminderId2");
		assertThat(reminders.get(1).getPersonId()).isEqualTo("personId2");
		assertThat(reminders.get(1).getOrganizationId()).isEqualTo("organizationId2");
		assertThat(reminders.get(1).getCaseId()).isEqualTo("caseId2");
		assertThat(reminders.get(1).getCaseLink()).isEqualTo("caseLink2");
		assertThat(reminders.get(1).getAction()).isEqualTo("action2");
		assertThat(reminders.get(1).getReminderDate()).isEqualTo(LocalDate.now().plusDays(1));
	}

	@Test
	void toMessageSuccess() {

		final var reminder = new Reminder().withCaseLink("caseLink")
											.withCaseId("caseId")
											.withAction("action")
											.withPersonId("personId");
		final var message = ReminderMapper.toMessage(reminder);

		assertThat(message.getPartyId()).isEqualTo(reminder.getPersonId());
		assertThat(message.getSubject()).isEqualTo("TestSubjectÅÄÖåäö");
		assertThat(message.getSender().getEmailName()).isEqualTo("TestEmailNameÅÄÖåäö");
		assertThat(message.getSender().getEmailAddress()).isEqualTo("TestEmailAddressÅÄÖåäö");
		assertThat(message.getSender().getSmsName()).isEqualTo("TestSmsNameÅÄÖåäö");
	}

	private List<ReminderEntity> createReminderEntities() {

		final var personId1 = "personId1";
		final var personId2 = "personId2";
		final var reminderId1 = "reminderId1";
		final var reminderId2 = "reminderId2";
		final var organizationId1 = "organizationId1";
		final var organizationId2 = "organizationId2";
		final var caseId1 = "caseId1";
		final var caseId2 = "caseId2";
		final var caseLink1 = "caseLink1";
		final var caseLink2 = "caseLink2";
		final var action1 = "action1";
		final var action2 = "action2";
		final var reminderDate1 = LocalDate.now();
		final var reminderDate2 = LocalDate.now().plusDays(1);

		final var reminderEntity1 = new ReminderEntity();
		reminderEntity1.setReminderId(reminderId1);
		reminderEntity1.setPersonId(personId1);
		reminderEntity1.setOrganizationId(organizationId1);
		reminderEntity1.setCaseId(caseId1);
		reminderEntity1.setCaseLink(caseLink1);
		reminderEntity1.setAction(action1);
		reminderEntity1.setReminderDate(reminderDate1);

		final var reminderEntity2 = new ReminderEntity();
		reminderEntity2.setReminderId(reminderId2);
		reminderEntity2.setPersonId(personId2);
		reminderEntity2.setOrganizationId(organizationId2);
		reminderEntity2.setCaseId(caseId2);
		reminderEntity2.setCaseLink(caseLink2);
		reminderEntity2.setAction(action2);
		reminderEntity2.setReminderDate(reminderDate2);

		return List.of(reminderEntity1, reminderEntity2);
	}

}
