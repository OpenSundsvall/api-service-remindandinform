package se.sundsvall.remindandinform.apptest;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import se.sundsvall.remindandinform.apptest.support.WireMockLifecycleManager;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;

import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.CREATED;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Create reminderandinform application tests
 * 
 * @see src/test/resources/db/testdata.sql for data setup.
 */
@QuarkusTest
@QuarkusTestResource(WireMockLifecycleManager.class)
class CreateReminderTest extends AbstractAppTest {

	@Inject
	ReminderRepository reminderRepository;

	@Test
	void test1_createReminder() throws Exception {
		String personId = "81471222-5798-11e9-ae24-57fa13b361e1";

		assertThat(reminderRepository.findByPersonId(personId).stream().findFirst()).isNotPresent();

		setupCall()
			.withServicePath("/reminders")
			.withHttpMethod(HttpMethod.POST)
			.withRequest("request.json")
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponse("response.json")
			.sendRequestAndVerifyResponse(Map.of("reminderId", "reminderId"));

		assertThat(reminderRepository.findByPersonId(personId).stream().findFirst()).isPresent();
	}
}
