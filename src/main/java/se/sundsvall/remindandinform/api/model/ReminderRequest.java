package se.sundsvall.remindandinform.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import se.sundsvall.remindandinform.api.validation.ValidUuid;
import se.sundsvall.remindandinform.api.validation.ValidNullableUuid;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@Schema(description = "Reminder creation request model")
public class ReminderRequest {
    @Schema(description = "Person Id", required = true, example = "81471222-5798-11e9-ae24-57fa13b361e2")
    @NotNull
    @ValidUuid
    private String personId;

    @Schema(description = "Organization Id", example = "81471222-5798-11e9-ae24-57fa13b361e3")
    @ValidNullableUuid
    private String organizationId;

    @Schema(description = "What should be done", required = true, example = "Renew application")
    @NotNull
    @Size(max = 8192)
    private String action;

    @Schema(description = "Id for the case", example = "12345")
    @Size(max = 255)
    private String caseId;

    @Schema(description = "Link to the case", example = "http://test.sundsvall.se/case1337")
    @Size(max = 512)
    private String caseLink;

    @Schema(description = "Date for reminding", required = true, example = "2021-11-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate reminderDate;

    public static ReminderRequest create() {
        return new ReminderRequest();
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public ReminderRequest withPersonId(String personId) {
        this.personId = personId;
        return this;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public ReminderRequest withOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ReminderRequest withAction(String action) {
        this.action = action;
        return this;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public ReminderRequest withCaseId(String caseId) {
        this.caseId = caseId;
        return this;
    }

    public String getCaseLink() {
        return caseLink;
    }

    public void setCaseLink(String caseLink) {
        this.caseLink = caseLink;
    }

    public ReminderRequest withCaseLink(String caseLink) {
        this.caseLink = caseLink;
        return this;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }
    public ReminderRequest withReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, organizationId, action, caseId, caseLink, reminderDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReminderRequest reminderRequest = (ReminderRequest) o;
        return Objects.equals(reminderRequest.personId, personId) && Objects.equals(reminderRequest.organizationId, organizationId) && Objects.equals(reminderRequest.action, action) && Objects.equals(reminderRequest.caseId, caseId) &&
                Objects.equals(reminderRequest.caseLink, caseLink) && Objects.equals(reminderRequest.reminderDate, reminderDate);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.append("ReminderRequest{ personId=").append(personId).append(", organizationId=").append(organizationId)
                .append(", action=").append(action).append(", caseId=").append(caseId).append(", caseLink=").append(caseLink).append(", reminderDate=").append(reminderDate).append("}").toString();
    }
}
