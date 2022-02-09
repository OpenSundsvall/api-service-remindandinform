package se.sundsvall.remindandinform.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Reminder model")
public class Reminder {

    @Schema(description = "Reminder ID", example = "R-81471222-5798-11e9-ae24-57fa13b361e1")
    private String reminderId;

    @Schema(description = "Person ID", example = "81471222-5798-11e9-ae24-57fa13b361e2")
    private String personId;

    @Schema(description = "Organization ID", example = "81471222-5798-11e9-ae24-57fa13b361e3")
    private String organizationId;

    @Schema(description = "What should be done", example = "Renew application")
    private String action;

    @Schema(description = "Case ID", example = "12345")
    private String caseId;

    @Schema(description = "Link to the case", example = "http://test.sundsvall.se/case12345")
    private String caseLink;

    @Schema(description = "Date for reminding", example = "2021-11-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reminderDate;

    public static Reminder create() {
        return new Reminder();
    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public Reminder withReminderId(String reminderId) {
        this.reminderId = reminderId;
        return this;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Reminder withPersonId(String personId) {
        this.personId = personId;
        return this;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Reminder withOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Reminder withAction(String action) {
        this.action = action;
        return this;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public Reminder withCaseId(String caseId) {
        this.caseId = caseId;
        return this;
    }

    public String getCaseLink() {
        return caseLink;
    }

    public void setCaseLink(String caseLink) {
        this.caseLink = caseLink;
    }

    public Reminder withCaseLink(String caseLink) {
        this.caseLink = caseLink;
        return this;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Reminder withReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reminderId, personId, organizationId, action, caseId, caseLink, reminderDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return Objects.equals(reminderId, reminder.reminderId) && Objects.equals(reminder.personId, personId) && Objects.equals(reminder.organizationId, organizationId) && Objects.equals(reminder.action, action) && Objects.equals(reminder.caseId, caseId) &&
                Objects.equals(reminder.caseLink, caseLink) && Objects.equals(reminder.reminderDate, reminderDate);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.append("Reminder{ reminderId=").append(reminderId).append(", personId=").append(personId).append(", organizationId=").append(organizationId)
                .append(", action=").append(action).append(", caseId=").append(caseId).append(", caseLink=").append(caseLink).append(", reminderDate=").append(reminderDate).append("}").toString();
    }
}
