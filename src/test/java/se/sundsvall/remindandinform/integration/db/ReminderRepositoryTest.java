package se.sundsvall.remindandinform.integration.db;

import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@QuarkusTest
@Transactional
class ReminderRepositoryTest {

	private static final String REMINDER_ID_1 = "reminderId1";
	private static final String REMINDER_ID_2 = "reminderId2";
	private static final String REMINDER_ID_3 = "reminderId3";
	private static final String REMINDER_ID_4 = "reminderId4";
	private static final String ORGANIZATION_ID_1 = "organizationId1";
	private static final String ORGANIZATION_ID_2 = "organizationId2";
	private static final String ORGANIZATION_ID_4 = "organizationId4";
	private static final String CASE_ID_1 = "caseId1";
	private static final String CASE_ID_2 = "caseId2";
	private static final String CASE_ID_3 = "caseId3";
	private static final String CASE_ID_4 = "caseId4";
	private static final String CASE_LINK_1 = "caseLink1";
	private static final String CASE_LINK_2 = "caseLink2";
	private static final String CASE_LINK_3 = "caseLink3";
	private static final String CASE_LINK_4 = "caseLink4";
	private static final String ACTION_1 = "action1";
	private static final String ACTION_2 = "action2";
	private static final String ACTION_3 = "action3";
	private static final String ACTION_4 = "action4";
	private static final LocalDate REMINDER_DATE_1 = LocalDate.now();
	private static final LocalDate REMINDER_DATE_2 = LocalDate.now().minusDays(2);
	private static final LocalDate REMINDER_DATE_3 = LocalDate.now().minusDays(2);
	private static final LocalDate REMINDER_DATE_4 = LocalDate.now().plusDays(2);
	private static final String PERSON_ID_1 = "personId1";
	private static final String PERSON_ID_2 = "personId2";
	private static final String PERSON_ID_3 = "personId3";
	private static final String PERSON_ID_4 = "personId4";

	private ReminderEntity entity1;
	private ReminderEntity entity2;
	private ReminderEntity entity3;
	private ReminderEntity entity4;

	@Inject
	ReminderRepository reminderRepository;

	@BeforeEach
	void setup() {
		reminderRepository.listAll().forEach(reminder -> reminderRepository.delete(reminder));
		reminderRepository.flush();

		entity1 = new ReminderEntity();
		entity1.setReminderId(REMINDER_ID_1);
		entity1.setPersonId(PERSON_ID_1);
		entity1.setOrganizationId(ORGANIZATION_ID_1);
		entity1.setCaseId(CASE_ID_1);
		entity1.setCaseLink(CASE_LINK_1);
		entity1.setAction(ACTION_1);
		entity1.setReminderDate(REMINDER_DATE_1);
		entity1.setSent(false);

		entity2 = new ReminderEntity();
		entity2.setReminderId(REMINDER_ID_2);
		entity2.setPersonId(PERSON_ID_2);
		entity2.setOrganizationId(ORGANIZATION_ID_2);
		entity2.setCaseId(CASE_ID_2);
		entity2.setCaseLink(CASE_LINK_2);
		entity2.setAction(ACTION_2);
		entity2.setReminderDate(REMINDER_DATE_2);
		entity2.setSent(false);

		//Entity with null in companyId
		entity3 = new ReminderEntity();
		entity3.setReminderId(REMINDER_ID_3);
		entity3.setPersonId(PERSON_ID_3);
		entity3.setCaseId(CASE_ID_3);
		entity3.setCaseLink(CASE_LINK_3);
		entity3.setAction(ACTION_3);
		entity3.setReminderDate(REMINDER_DATE_3);
		entity3.setSent(true);


		//Entity to remove
		entity4 = new ReminderEntity();
		entity4.setReminderId(REMINDER_ID_4);
		entity4.setPersonId(PERSON_ID_4);
		entity4.setOrganizationId(ORGANIZATION_ID_4);
		entity4.setCaseId(CASE_ID_4);
		entity4.setCaseLink(CASE_LINK_4);
		entity4.setAction(ACTION_4);
		entity4.setReminderDate(REMINDER_DATE_4);
		entity4.setSent(false);


		reminderRepository.persist(entity1, entity2, entity3, entity4);
	}

	@Test
	void findByPersonId() {
		final var reminders = reminderRepository.findByPersonId(PERSON_ID_1);
		Assertions.assertThat(reminders).isNotEmpty().hasSize(1);
		Assertions.assertThat(reminders)
				.usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().withIgnoredFieldsMatchingRegexes(".+hibernate.+").build())
				.containsExactlyInAnyOrder(entity1);
	}

	@Test
	void findByPersonIdAndOrganizationId() {
		final var reminders = reminderRepository.findByPersonIdAndOrganizationId(PERSON_ID_1, ORGANIZATION_ID_1);
		Assertions.assertThat(reminders).hasSize(1);
		Assertions.assertThat(reminders.get(0)).isEqualTo(entity1);
	}

	@Test
	void persistAndFetch() {

		final var reminderEntity = new ReminderEntity();
		reminderEntity.setReminderId("reminderId");
		reminderEntity.setPersonId("personId");
		reminderEntity.setAction("action");
		reminderEntity.setCaseLink("caseLink");
		reminderEntity.setCaseId("caseId");
		reminderEntity.setOrganizationId("organizationId");
		reminderEntity.setReminderDate(LocalDate.now());

		final var fetchedEntity = reminderRepository.persistAndFetch(reminderEntity);
		assertThat(fetchedEntity)
			.usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFieldsMatchingRegexes(".+hibernate.+").build())
			.isEqualTo(reminderEntity);
		assertThat(fetchedEntity.getId()).isPositive();
	}

	@Test
	void deleteByReminderId() {

		var reminders = reminderRepository.findByPersonId(PERSON_ID_4);

		Assertions.assertThat(reminders).isNotEmpty().hasSize(1);
		Assertions.assertThat(reminders)
				.usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().withIgnoredFieldsMatchingRegexes(".+hibernate.+").build())
				.containsExactlyInAnyOrder(entity4);

		final var deletedEntities = reminderRepository.deleteReminderByReminderId(REMINDER_ID_4);

		reminders = reminderRepository.findByPersonId(PERSON_ID_4);

		Assertions.assertThat(reminders).isEmpty();
		assertThat(deletedEntities).isEqualTo(1);
	}

	@Test
	void getRemindersByReminderDate() {

		var reminders = reminderRepository.findByReminderDateNotSent(LocalDate.now());

		Assertions.assertThat(reminders).isNotEmpty().hasSize(2);
		Assertions.assertThat(reminders)
				.usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().withIgnoredFieldsMatchingRegexes(".+hibernate.+").build())
				.containsExactlyInAnyOrder(entity1, entity2);
	}

	@Test
	void updateRemindersByReminderDate() {

		var reminders = reminderRepository.findByReminderDateNotSent(LocalDate.now());

		Assertions.assertThat(reminders).isNotEmpty().hasSize(2);
		Assertions.assertThat(reminders)
				.usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().withIgnoredFieldsMatchingRegexes(".+hibernate.+").build())
				.containsExactlyInAnyOrder(entity1, entity2);

		reminderRepository.updateSentByReminderDate(LocalDate.now());

		reminders = reminderRepository.findByReminderDateNotSent(LocalDate.now());

		Assertions.assertThat(reminders).isEmpty();
	}

}
