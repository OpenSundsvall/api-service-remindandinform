package se.sundsvall.remindandinform.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Schema(description = "Request model for sending reminders of a specified date")
public class SendRemindersRequest {
    @Schema(description = "Date for reminding", required = true, example = "2021-11-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate reminderDate;

    public static SendRemindersRequest create() {
        return new SendRemindersRequest();
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public SendRemindersRequest withReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reminderDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendRemindersRequest sendRemindersRequest = (SendRemindersRequest) o;
        return Objects.equals(sendRemindersRequest.reminderDate, reminderDate);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.append("SendRemindersRequest{ reminderDate=").append(reminderDate).append("}").toString();
    }
}
