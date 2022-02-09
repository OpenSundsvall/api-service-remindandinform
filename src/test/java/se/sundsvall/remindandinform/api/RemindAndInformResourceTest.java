package se.sundsvall.remindandinform.api;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import se.sundsvall.remindandinform.api.exception.ServiceException;
import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.SendRemindersRequest;
import se.sundsvall.remindandinform.service.ReminderService;
import se.sundsvall.remindandinform.service.logic.SendRemindersLogic;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.LOCATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class RemindAndInformResourceTest {

	@InjectMock
	ReminderService reminderServiceMock;

	@InjectMock
	SendRemindersLogic sendRemindersLogicMock;

	@Test
	void getRemindersByPersonId() throws ServiceException {

		// Parameters
		final var personId = UUID.randomUUID().toString();
		final var reminderId = "reminderId";
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var action = "action";
		final var organizationId = UUID.randomUUID().toString();
		final var reminderDate = LocalDate.now();

		final var reminder = Reminder.create()
						.withPersonId(personId)
						.withReminderId(reminderId)
						.withOrganizationId(organizationId)
						.withAction(action)
						.withCaseId(caseId)
						.withCaseLink(caseLink)
						.withReminderDate(reminderDate);

		when(reminderServiceMock.findRemindersByPersonId(personId)).thenReturn(List.of(reminder));

		final var response = given()
			.pathParam("personId", personId)
			.contentType(APPLICATION_JSON)
			.when()
			.get("/reminders/persons/{personId}")
			.then().assertThat()
			.statusCode(OK.getStatusCode())
			.extract()
			.as(Reminder[].class);

		assertThat(response).isNotNull();
		verify(reminderServiceMock).findRemindersByPersonId(personId);
	}

	@Test
	void getReminderByReminderId() throws ServiceException {

		// Parameters
		final var personId = UUID.randomUUID().toString();
		final var reminderId = "reminderId";
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var action = "action";
		final var organizationId = UUID.randomUUID().toString();
		final var reminderDate = LocalDate.now();

		final var reminder = Reminder.create()
				.withPersonId(personId)
				.withReminderId(reminderId)
				.withOrganizationId(organizationId)
				.withAction(action)
				.withCaseId(caseId)
				.withCaseLink(caseLink)
				.withReminderDate(reminderDate);

		when(reminderServiceMock.findReminderByReminderId(reminderId)).thenReturn(reminder);

		final var response = given()
				.pathParam("reminderId", reminderId)
				.contentType(APPLICATION_JSON)
				.when()
				.get("/reminders/{reminderId}")
				.then().assertThat()
				.statusCode(OK.getStatusCode())
				.extract()
				.as(Reminder.class);

		assertThat(response).isNotNull();
		verify(reminderServiceMock).findReminderByReminderId(reminderId);
	}

	@Test
	void postRemindersByReminderDate() throws ServiceException {

		// Parameters
		final var sendRemindersRequest = SendRemindersRequest.create().withReminderDate(LocalDate.now());

		doNothing().when(sendRemindersLogicMock).sendReminders(sendRemindersRequest.getReminderDate());

		final var response = given()
				.body(sendRemindersRequest)
				.contentType(APPLICATION_JSON)
				.when()
				.post("/reminders/send/")
				.then().assertThat()
				.statusCode(NO_CONTENT.getStatusCode());

		assertThat(response).isNotNull();
		verify(sendRemindersLogicMock).sendReminders(LocalDate.now());
	}

	@Test
	void getRemindersByPersonIdAndOrganizationId() throws ServiceException{

		// Parameters
		final var personId = UUID.randomUUID().toString();
		final var organizationId = UUID.randomUUID().toString();

		final var response = given()
			.pathParam("personId", personId)
			.pathParam("organizationId", organizationId)
			.contentType(APPLICATION_JSON)
			.when()
			.get("/reminders/persons/{personId}/organizations/{organizationId}")
			.then().assertThat()
			.statusCode(OK.getStatusCode())
			.extract()
			.as(Reminder[].class);

		assertThat(response).isNotNull();
		verify(reminderServiceMock).findRemindersByPersonIdAndOrganizationId(personId, organizationId);
	}

	@Test
	void updateReminder() throws ServiceException {

		// Parameters
		final var reminderId = "reminderId";
		final var body = ReminderRequest.create()
				.withPersonId("81471222-5798-11e9-ae24-57fa13b361e")
				.withOrganizationId("81471222-5798-11e9-ae24-57fa13b3612")
				.withAction("action")
				.withCaseId("caseId")
				.withCaseLink("caseLink")
				.withReminderDate(LocalDate.now());

		when(reminderServiceMock.updateReminder(argThat(reminderRequest -> reminderRequest.getPersonId().equals("81471222-5798-11e9-ae24-57fa13b361e")), argThat(id -> id.equals(reminderId)))).thenReturn(Reminder.create());

		final var response = given()
			.pathParam("reminderId", reminderId)
			.contentType(APPLICATION_JSON)
			.body(body)
			.when()
			.patch("/reminders/{reminderId}")
			.then().assertThat()
			.statusCode(OK.getStatusCode())
			.extract().as(Reminder.class);

		assertThat(response).isNotNull();
		verify(reminderServiceMock).updateReminder(argThat(reminderRequest -> reminderRequest.getPersonId().equals("81471222-5798-11e9-ae24-57fa13b361e")), anyString());
	}

	@Test
	void deleteReminder() throws ServiceException {

		// Parameters
		final var reminderId = "reminderId";
		doNothing().when(reminderServiceMock).deleteReminderByReminderId(reminderId);

		given()
			.pathParam("reminderId", reminderId)
			.contentType(APPLICATION_JSON)
			.when()
			.delete("/reminders/{reminderId}")
			.then().assertThat()
			.statusCode(NO_CONTENT.getStatusCode());

		verify(reminderServiceMock).deleteReminderByReminderId(reminderId);
	}

	@Test
	void createReminder() {

		final var body = ReminderRequest.create()
				.withPersonId("81471222-5798-11e9-ae24-57fa13b361e")
				.withAction("action")
				.withCaseId("caseId")
				.withCaseLink("caseLink")
				.withOrganizationId("81471222-5798-11e9-ae24-57fa13b3612")
				.withReminderDate(LocalDate.now());

		when(reminderServiceMock.createReminder(argThat(reminderRequest -> reminderRequest.getPersonId().equals("81471222-5798-11e9-ae24-57fa13b361e")))).thenReturn(Reminder.create().withReminderId("reminderId"));

		final var response = given()
			.contentType(APPLICATION_JSON)
			.body(body)
			.when()
			.post("/reminders/")
			.then().assertThat()
			.statusCode(CREATED.getStatusCode())
			.header(LOCATION, "http://localhost:8081/reminders/reminderId")
			.extract().as(Reminder.class);

		assertThat(response).isNotNull();
		verify(reminderServiceMock).createReminder(argThat(reminderRequest -> reminderRequest.getPersonId().equals("81471222-5798-11e9-ae24-57fa13b361e")));
	}
}
