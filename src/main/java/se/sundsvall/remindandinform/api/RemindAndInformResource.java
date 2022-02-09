package se.sundsvall.remindandinform.api;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sundsvall.remindandinform.api.exception.ServiceException;
import se.sundsvall.remindandinform.api.exception.model.ServiceErrorResponse;
import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.SendRemindersRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.api.validation.ValidUuid;
import se.sundsvall.remindandinform.service.ReminderService;
import se.sundsvall.remindandinform.service.logic.SendRemindersLogic;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Map;

import static javax.ws.rs.core.HttpHeaders.LOCATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.ok;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.STRING;


@Path("/reminders")
@Tag(name = "Reminder", description = "Remind operations")
public class RemindAndInformResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemindAndInformResource.class);

    @Inject
    ReminderService reminderService;

    @Inject
    SendRemindersLogic sendRemindersLogic;

    @Context
    UriInfo uriInfo;

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Create a new reminder.")
    @APIResponse(responseCode = "201", headers = @Header(name = LOCATION, schema = @Schema(type = STRING)), description = "Created", content = @Content(schema = @Schema(implementation = Reminder.class)))
    @APIResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Internal Server error", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    public Response createReminder(
            @RequestBody(required = true, content = @Content(schema = @Schema(implementation = ReminderRequest.class))) @NotNull @Valid ReminderRequest body) {
        LOGGER.debug("Received reminder request: body='{}'", body);

        var reminder = reminderService.createReminder(body);

        // URI to the created resource.
        final var locationUri = uriInfo.getAbsolutePathBuilder()
                .path("/{reminderId}")
                .buildFromMap(Map.of("reminderId", reminder.getReminderId()));

        return created(locationUri)
                        .entity(reminder)
                        .build();
    }

    @POST
    @Operation(summary = "Send reminders.")
    @Consumes(APPLICATION_JSON)
    @Path("/send")
    @APIResponse(responseCode = "204", description = "Successful operation")
    @APIResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Internal Server error", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    public Response sendReminders(
            @RequestBody(required = true, content = @Content(schema = @Schema(implementation = SendRemindersRequest.class))) @NotNull @Valid SendRemindersRequest body)
            throws ServiceException {
        LOGGER.debug("Received send sendReminders request: sendRemindersRequest='{}'", body);

        sendRemindersLogic.sendReminders(body.getReminderDate());

        return noContent().build();
    }

    @GET
    @Path("/persons/{personId}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Returns all reminders for a person.")
    @APIResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(type= SchemaType.ARRAY, implementation = Reminder.class)))
    @APIResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Internal Server error", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    public Response getRemindersByPersonId(
            @Parameter(name = "personId", description = "Person ID", example = "81471222-5798-11e9-ae24-57fa13b361e1", required = true) @ValidUuid @PathParam("personId") String personId)
            throws ServiceException {
        LOGGER.debug("Received getRemindersByPersonId request: personId='{}'", personId);

        return ok().entity(reminderService.findRemindersByPersonId(personId)).build();
    }

    @GET
    @Path("/persons/{personId}/organizations/{organizationId}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Returns all reminders for a person and an organization which the person represent.")
    @APIResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = Reminder.class)))
    @APIResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Internal Server error", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    public Response getRemindersByPersonIdAndOrganizationId(
            @Parameter(name = "personId", description = "Person ID", required = true, example = "81471222-5798-11e9-ae24-57fa13b361e2") @ValidUuid @PathParam("personId") String personId,
            @Parameter(name = "organizationId", description = "Organization ID", example = "81471222-5798-11e9-ae24-57fa13b361e3", required = true) @ValidUuid @PathParam("organizationId") String organizationId)
            throws ServiceException {
        LOGGER.debug("Received getRemindersByPersonIdAndOrganizationId request: personId='{}', organizationId='{}'", personId, organizationId);

        return ok().entity(reminderService.findRemindersByPersonIdAndOrganizationId(personId, organizationId)).build();
    }

    @javax.ws.rs.PATCH
    @Path("/{reminderId}")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Manage updates of a reminder.")
    @APIResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = Reminder.class)))
    @APIResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Internal Server error", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    public Response updateReminder(
            @Parameter(name = "reminderId", description = "Reminder ID", example = "R-81471222-5798-11e9-ae24-57fa13b361e1", required = true) @NotBlank @PathParam("reminderId") String reminderId,
            @RequestBody(required = true, content = @Content(schema = @Schema(implementation = UpdateReminderRequest.class))) @NotNull @Valid UpdateReminderRequest body)
            throws ServiceException {
        LOGGER.debug("Received updateReminder request: reminderId='{}', body='{}'", reminderId, body);

        return ok().entity(reminderService.updateReminder(body, reminderId)).build();
    }

    @javax.ws.rs.DELETE
    @Path("/{reminderId}")
    @Operation(summary = "Deletes a reminder.")
    @APIResponse(responseCode = "204", description = "Successful operation")
    @APIResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Internal Server error", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    public Response deleteReminder(
            @Parameter(name = "reminderId", description = "Reminder ID", required = true, example = "R-81471222-5798-11e9-ae24-57fa13b361e1") @NotBlank @PathParam("reminderId") String reminderId)
            throws ServiceException {
        LOGGER.debug("Received deleteReminder request: reminderId='{}'", reminderId);

        reminderService.deleteReminderByReminderId(reminderId);

        return noContent().build();
    }

    @GET
    @Path("/{reminderId}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Returns a reminder by reminder-id")
    @APIResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(type= SchemaType.ARRAY, implementation = Reminder.class)))
    @APIResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Internal Server error", content = @Content(schema = @Schema(implementation = ServiceErrorResponse.class)))
    public Response getReminderByReminderId(
            @Parameter(name = "reminderId", description = "Reminder ID", example = "R-81471222-5798-11e9-ae24-57fa13b361e1", required = true) @NotBlank @PathParam("reminderId") String reminderId)
            throws ServiceException {
        LOGGER.debug("Received getReminderByReminderId request: reminderId='{}'", reminderId);

        return ok().entity(reminderService.findReminderByReminderId(reminderId)).build();
    }
}