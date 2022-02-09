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

class UpdateReminderRequestTest {
    @BeforeEach
    void setup() {
        BeanMatchers.registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
    }

    @Test
    void testBean() {
        assertThat(UpdateReminderRequest.class, allOf(
                hasValidBeanConstructor(),
                hasValidGettersAndSetters(),
                hasValidBeanHashCode(),
                hasValidBeanEquals(),
                hasValidBeanToString()));
    }

    @Test
    void testBuilderMethods() {
        final var personId = "personId";
        final var organizationId = "organizationId";
        final var caseId = "caseId";
        final var caseLink = "caselink";
        final var action = "action";
        final var reminderDate = LocalDate.now();

        final var updateReminderRequest = UpdateReminderRequest.create()
                .withPersonId(personId)
                .withOrganizationId(organizationId)
                .withCaseId(caseId)
                .withCaseLink(caseLink)
                .withAction(action)
                .withReminderDate(reminderDate);

        assertThat(updateReminderRequest).isNotNull().hasNoNullFieldsOrProperties();
        assertThat(updateReminderRequest.getPersonId()).isEqualTo(personId);
        assertThat(updateReminderRequest.getOrganizationId()).isEqualTo(organizationId);
        assertThat(updateReminderRequest.getCaseId()).isEqualTo(caseId);
        assertThat(updateReminderRequest.getCaseLink()).isEqualTo(caseLink);
        assertThat(updateReminderRequest.getAction()).isEqualTo(action);
        assertThat(updateReminderRequest.getReminderDate()).isEqualTo(reminderDate);
    }

    @Test
    void testNoDirtOnCreatedBean() {
        assertThat(UpdateReminderRequest.create()).hasAllNullFieldsOrProperties();
    }

}
