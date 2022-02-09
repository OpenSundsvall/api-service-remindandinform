package se.sundsvall.remindandinform.api;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.junit.jupiter.api.Test;
import se.sundsvall.remindandinform.api.exception.model.ServiceErrorResponse;
import se.sundsvall.remindandinform.api.exception.model.TechnicalDetails;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.SendRemindersRequest;
import se.sundsvall.remindandinform.service.ReminderService;
import se.sundsvall.remindandinform.service.logic.SendRemindersLogic;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

@QuarkusTest
class RemindAndInformResourceFailuresTest {

	@ConfigProperty(name = "quarkus.application.name")
	String applicationName;

	@InjectMock
	ReminderService reminderServiceMock;

	@InjectMock
	SendRemindersLogic sendRemindersLogicMock;

	/**
	 * Create reminders tests:
	 */

	@Test
	void createMissingBody() {

		final var response = given()
			.contentType(APPLICATION_JSON)
			.when()
			.post("/reminders/")
			.then().assertThat()
			.statusCode(BAD_REQUEST.getStatusCode())
			.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
			.withRootCode(BAD_REQUEST.getStatusCode())
			.withRootCause("Constraint violation")
			.withServiceId(applicationName)
			.withDetails(List.of(
				"body: must not be null",
				"Request: /reminders/")));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderEmptyBody() {

		final var body = ReminderRequest.create(); // Empty body.

		final var response = given()
			.contentType(APPLICATION_JSON)
			.body(body)
			.when()
			.post("/reminders/")
			.then().assertThat()
			.statusCode(BAD_REQUEST.getStatusCode())
			.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
			.withRootCode(BAD_REQUEST.getStatusCode())
			.withRootCause("Constraint violation")
			.withServiceId(applicationName)
			.withDetails(List.of(
				"body.action: must not be null",
				"body.personId: must not be null",
				"body.personId: not a valid UUID",
				"body.reminderDate: must not be null",
				"Request: /reminders/")));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderMissingPersonId() {

		final var body = ReminderRequest.create()
				.withOrganizationId("81471222-5798-11e9-ae24-57fa13b3612")
				.withAction("action")
				.withCaseId("caseId")
				.withCaseLink("caseLink")
				.withReminderDate(LocalDate.now()); // Body with missing personId.


		final var response = given()
			.contentType(APPLICATION_JSON)
			.body(body)
			.when()
			.post("/reminders/")
			.then().assertThat()
			.statusCode(BAD_REQUEST.getStatusCode())
			.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
			.withRootCode(BAD_REQUEST.getStatusCode())
			.withRootCause("Constraint violation")
			.withServiceId(applicationName)
			.withDetails(List.of(
				"body.personId: must not be null",
				"body.personId: not a valid UUID",
				"Request: /reminders/")));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderContainsInvalidPersonId() {

		final var body = ReminderRequest.create()
				.withPersonId("invalid-person-id")
				.withOrganizationId("81471222-5798-11e9-ae24-57fa13b3612")
				.withAction("action")
				.withCaseId("caseId")
				.withCaseLink("caseLink")
				.withReminderDate(LocalDate.now()); // Body with not valid personId.

		final var response = given()
			.contentType(APPLICATION_JSON)
			.body(body)
			.when()
			.post("/reminders/")
			.then().assertThat()
			.statusCode(BAD_REQUEST.getStatusCode())
			.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
			.withRootCode(BAD_REQUEST.getStatusCode())
			.withRootCause("Constraint violation")
			.withServiceId(applicationName)
			.withDetails(List.of(
				"body.personId: not a valid UUID",
				"Request: /reminders/")));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderContainsInvalidOrganizationId() {

		final var body = ReminderRequest.create()
				.withPersonId("81471222-5798-11e9-ae24-57fa13b361e")
				.withOrganizationId("invalid-organization-id")
				.withAction("action")
				.withCaseId("caseId")
				.withCaseLink("caseLink")
				.withReminderDate(LocalDate.now()); // Body with not valid personId.

		final var response = given()
				.contentType(APPLICATION_JSON)
				.body(body)
				.when()
				.post("/reminders/")
				.then().assertThat()
				.statusCode(BAD_REQUEST.getStatusCode())
				.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
				.withRootCode(BAD_REQUEST.getStatusCode())
				.withRootCause("Constraint violation")
				.withServiceId(applicationName)
				.withDetails(List.of(
						"body.organizationId: not a valid UUID",
						"Request: /reminders/")));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderContainsLocalDateOfWrongFormat() {

		var body = new JSONObject();
		body.put("personId", "81471222-5798-11e9-ae24-57fa13b361e");
		body.put("organizationId", "81471222-5798-11e9-ae24-57fa13b3612");
		body.put("action", "action");
		body.put("caseId", "caseId");
		body.put("caseLink", "caseLink");
		body.put("reminderDate", "2021-13-01");

		final var response = given()
				.contentType(APPLICATION_JSON)
				.body(body)
				.when()
				.post("/reminders/")
				.then().assertThat()
				.statusCode(BAD_REQUEST.getStatusCode())
				.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Bad request format!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails().getRootCode()).isEqualTo(BAD_REQUEST.getStatusCode());
		assertThat(response.getTechnicalDetails().getServiceId()).isEqualTo(applicationName);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderMissingBody() {

		// Parameter values
		final var reminderId = "reminderId";

		final var response = given()
			.pathParam("reminderId", reminderId)
			.contentType(APPLICATION_JSON)
			.when()
			.patch("/reminders/{reminderId}")
			.then().assertThat()
			.statusCode(BAD_REQUEST.getStatusCode())
			.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
			.withRootCode(BAD_REQUEST.getStatusCode())
			.withRootCause("Constraint violation")
			.withServiceId(applicationName)
			.withDetails(List.of(
				"body: must not be null",
				"Request: /reminders/" + reminderId)));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderBadReminderId() {

		// Parameter values
		final var reminderId = " ";
		final var body = ReminderRequest.create()
				.withPersonId("81471222-5798-11e9-ae24-57fa13b361e")
				.withOrganizationId("81471222-5798-11e9-ae24-57fa13b3612")
				.withAction("action")
				.withCaseId("caseId")
				.withCaseLink("caseLink")
				.withReminderDate(LocalDate.now()); // Body with not valid personId

		final var response = given()
				.pathParam("reminderId", reminderId)
				.contentType(APPLICATION_JSON)
				.body(body)
				.when()
				.patch("/reminders/{reminderId}")
				.then().assertThat()
				.statusCode(BAD_REQUEST.getStatusCode())
				.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
				.withRootCode(BAD_REQUEST.getStatusCode())
				.withRootCause("Constraint violation")
				.withServiceId(applicationName)
				.withDetails(List.of(
						"reminderId: must not be blank",
						"Request: /reminders/" + reminderId)));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderContainsLocalDateOfWrongFormat() {

		// Parameter values
		final var reminderId = "reminderId";

		JSONObject body = new JSONObject();
		body.put("reminderDate", "2021-13-01");


		final var response = given()
				.pathParam("reminderId", reminderId)
				.contentType(APPLICATION_JSON)
				.body(body)
				.when()
				.patch("/reminders/{reminderId}")
				.then().assertThat()
				.statusCode(BAD_REQUEST.getStatusCode())
				.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Bad request format!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails().getRootCode()).isEqualTo(BAD_REQUEST.getStatusCode());
		assertThat(response.getTechnicalDetails().getServiceId()).isEqualTo(applicationName);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderContainsInvalidPersonId() {

		// Parameter values.
		final var reminderId = "reminderId";
		final var personId = "invalidPersonId";
		final var organizationId = "81471222-5798-11e9-ae24-57fa13b361e";
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var action = "action";
		final var reminderDate = LocalDate.now();

		final var body = ReminderRequest.create() // Body with invalid personId
			.withPersonId(personId)
			.withOrganizationId(organizationId)
			.withCaseId(caseId)
			.withCaseLink(caseLink)
			.withAction(action)
			.withReminderDate(reminderDate);

		final var response = given()
				.pathParam("reminderId", reminderId)
				.contentType(APPLICATION_JSON)
				.body(body)
				.when()
				.patch("/reminders/{reminderId}")
				.then().assertThat()
				.statusCode(BAD_REQUEST.getStatusCode())
				.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
			.withRootCode(BAD_REQUEST.getStatusCode())
			.withRootCause("Constraint violation")
			.withServiceId(applicationName)
			.withDetails(List.of(
					"body.personId: not a valid UUID",
					"Request: /reminders/reminderId")));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void deleteReminderBadReminderId() {

		// Parameter values
		final var reminderId = " ";

		final var response = given()
			.pathParam("reminderId", reminderId)
			.contentType(APPLICATION_JSON)
			.when()
			.delete("/reminders/{reminderId}")
			.then().assertThat()
			.statusCode(BAD_REQUEST.getStatusCode())
			.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
			.withRootCode(BAD_REQUEST.getStatusCode())
			.withRootCause("Constraint violation")
			.withServiceId(applicationName)
			.withDetails(List.of(
				"reminderId: must not be blank",
				"Request: /reminders/" + reminderId)));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void sendRemindersWrongFormatOfDate() {

		final var request = new JSONObject();
		request.put("reminderDate", "2021-13-01");
		final var response = given()
				.contentType(APPLICATION_JSON)
				.body(request)
				.when()
				.post("/reminders/send/")
				.then().assertThat()
				.statusCode(BAD_REQUEST.getStatusCode())
				.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Bad request format!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails().getRootCode()).isEqualTo(BAD_REQUEST.getStatusCode());
		assertThat(response.getTechnicalDetails().getServiceId()).isEqualTo(applicationName);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void sendRemindersMissingBody() {

		final var response = given()
				.contentType(APPLICATION_JSON)
				.when()
				.post("/reminders/send/")
				.then().assertThat()
				.statusCode(BAD_REQUEST.getStatusCode())
				.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
				.withRootCode(BAD_REQUEST.getStatusCode())
				.withRootCause("Constraint violation")
				.withServiceId(applicationName)
				.withDetails(List.of(
						"body: must not be null",
						"Request: /reminders/send/")));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void sendRemindersMissingReminderDate() {

		final var response = given()
				.contentType(APPLICATION_JSON)
				.body(SendRemindersRequest.create())
				.when()
				.post("/reminders/send/")
				.then().assertThat()
				.statusCode(BAD_REQUEST.getStatusCode())
				.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
				.withRootCode(BAD_REQUEST.getStatusCode())
				.withRootCause("Constraint violation")
				.withServiceId(applicationName)
				.withDetails(List.of(
						"body.reminderDate: must not be null",
						"Request: /reminders/send/")));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void getReminderMissingReminderId() {

		final var reminderId = " ";
		final var response = given()
				.pathParam("reminderId", reminderId)
				.contentType(APPLICATION_JSON)
				.when()
				.get("/reminders/{reminderId}")
				.then().assertThat()
				.statusCode(BAD_REQUEST.getStatusCode())
				.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
				.withRootCode(BAD_REQUEST.getStatusCode())
				.withRootCause("Constraint violation")
				.withServiceId(applicationName)
				.withDetails(List.of(
						"reminderId: must not be blank",
						"Request: /reminders/ ")));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void getRemindersInvalidPersonId() {

		final var personId = "not-valid-person-id";
		final var response = given()
				.pathParam("personId", personId)
				.contentType(APPLICATION_JSON)
				.when()
				.get("/reminders/persons/{personId}")
				.then().assertThat()
				.statusCode(BAD_REQUEST.getStatusCode())
				.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
				.withRootCode(BAD_REQUEST.getStatusCode())
				.withRootCause("Constraint violation")
				.withServiceId(applicationName)
				.withDetails(List.of(
						"personId: not a valid UUID",
						"Request: /reminders/persons/not-valid-person-id")));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void getRemindersInvalidOrganizationId() {

		final var personId = "81471222-5798-11e9-ae24-57fa13b361e";
		final var organizationId = "not-valid-organization-id";
		final var response = given()
				.pathParam("personId", personId)
				.pathParam("organizationId", organizationId)
				.contentType(APPLICATION_JSON)
				.when()
				.get("/reminders/persons/{personId}/organizations/{organizationId}")
				.then().assertThat()
				.statusCode(BAD_REQUEST.getStatusCode())
				.extract().as(ServiceErrorResponse.class);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("Request validation failed!");
		assertThat(response.getHttpCode()).isEqualTo(400);
		assertThat(response.getTechnicalDetails()).isEqualTo(TechnicalDetails.create()
				.withRootCode(BAD_REQUEST.getStatusCode())
				.withRootCause("Constraint violation")
				.withServiceId(applicationName)
				.withDetails(List.of(
						"organizationId: not a valid UUID",
						"Request: /reminders/persons/81471222-5798-11e9-ae24-57fa13b361e/organizations/not-valid-organization-id")));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

}
