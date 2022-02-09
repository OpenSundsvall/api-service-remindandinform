package se.sundsvall.remindandinform.api.model;

import com.google.code.beanmatchers.BeanMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Random;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

class ReminderTest {

	@BeforeEach
	void setup() {
		BeanMatchers.registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(Reminder.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var reminderId = "reminderId";
		final var personId = "personId";
		final var organizationId = "organizationId";
		final var caseId = "caseId";
		final var caseLink = "caselink";
		final var action = "action";
		final var reminderDate = LocalDate.now();

		final var reminder = Reminder.create()
				.withReminderId(reminderId)
				.withPersonId(personId)
				.withOrganizationId(organizationId)
				.withCaseId(caseId)
				.withCaseLink(caseLink)
				.withAction(action)
				.withReminderDate(reminderDate);

		assertThat(reminder).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(reminder.getReminderId()).isEqualTo(reminderId);
		assertThat(reminder.getPersonId()).isEqualTo(personId);
		assertThat(reminder.getOrganizationId()).isEqualTo(organizationId);
		assertThat(reminder.getCaseId()).isEqualTo(caseId);
		assertThat(reminder.getCaseLink()).isEqualTo(caseLink);
		assertThat(reminder.getAction()).isEqualTo(action);
		assertThat(reminder.getReminderDate()).isEqualTo(reminderDate);

	}
	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Reminder.create()).hasAllNullFieldsOrProperties();
	}

}
