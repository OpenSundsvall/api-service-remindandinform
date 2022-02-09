package se.sundsvall.remindandinform.apptest;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import se.sundsvall.remindandinform.apptest.support.WireMockLifecycleManager;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;

import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Create reminderandinform application tests
 * 
 * @see src/test/resources/db/testdata.sql for data setup.
 */
@QuarkusTest
@QuarkusTestResource(WireMockLifecycleManager.class)
class ReadRemindersTest extends AbstractAppTest {

	@Test
	void test1_getRemindersByPersonId() throws Exception {
		String personId = "fbfbd90c-4c47-11ec-81d3-0242ac130001";

		setupCall()
			.withServicePath("/reminders/persons/" + personId)
			.withHttpMethod(HttpMethod.GET)
			.withExpectedResponseStatus(Response.Status.OK)
			.withExpectedResponse("response.json")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test2_getRemindersByPersonIdAndOrganizationId() throws Exception {
		String personId = "fbfbd90c-4c47-11ec-81d3-0242ac130002";
		String organizationId = "fbfbd90c-4c47-11ec-81d3-0242ac130013";

		setupCall()
				.withServicePath("/reminders/persons/" + personId + "/organizations/" + organizationId)
				.withHttpMethod(HttpMethod.GET)
				.withExpectedResponseStatus(Response.Status.OK)
				.withExpectedResponse("response.json")
				.sendRequestAndVerifyResponse();
	}
}
