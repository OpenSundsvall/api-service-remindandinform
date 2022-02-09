package se.sundsvall.remindandinform.apptest;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import se.sundsvall.remindandinform.apptest.support.WireMockLifecycleManager;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;

import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Create remindandinform application tests
 * 
 * @see src/test/resources/db/testdata.sql for data setup.
 */
@QuarkusTest
@QuarkusTestResource(WireMockLifecycleManager.class)
class UpdateReminderTest extends AbstractAppTest {

	@Inject
	ReminderRepository reminderRepository;

	@Test
	void test1_updateReminder() throws Exception {
		String reminderId = "R-fbfbd90c-4c47-11ec-81d3-0242ac130004";

		setupCall()
			.withServicePath("/reminders/" + reminderId)
			.withHttpMethod(HttpMethod.PATCH)
			.withRequest("request.json")
			.withExpectedResponseStatus(Response.Status.OK)
			.withExpectedResponse("response.json")
			.sendRequestAndVerifyResponse();

		assertThat(reminderRepository.findByReminderId(reminderId).stream().findFirst()).isPresent();
	}
}
