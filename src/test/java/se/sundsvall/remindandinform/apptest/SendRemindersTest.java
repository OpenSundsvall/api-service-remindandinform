package se.sundsvall.remindandinform.apptest;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import se.sundsvall.remindandinform.apptest.support.WireMockLifecycleManager;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;

import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Create remindandinform application tests
 * 
 * @see src/test/resources/db/testdata.sql for data setup.
 */
@QuarkusTest
@QuarkusTestResource(WireMockLifecycleManager.class)
class SendRemindersTest extends AbstractAppTest {

	@Inject
	ReminderRepository reminderRepository;

	@Test
	void test1_sendReminders() throws Exception {

		assertThat(reminderRepository.findByReminderDateNotSent(LocalDate.parse("2021-11-25")).stream().toList().size()).isEqualTo(3);

		setupCall()
			.withServicePath("/reminders/send/")
			.withHttpMethod(HttpMethod.POST)
			.withRequest("request.json")
			.withExpectedResponseStatus(Response.Status.NO_CONTENT)
			.sendRequestAndVerifyResponse();

		assertThat(reminderRepository.findByReminderDateNotSent(LocalDate.parse("2021-11-25")).stream().toList().size()).isZero();
	}
}
