package se.sundsvall.remindandinform.integration.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "reminder", indexes = {
	@Index(name = "reminder_id_index", columnList = "reminder_id"),
	@Index(name = "person_id_index", columnList = "person_id"),
	@Index(name = "organization_id_index", columnList = "organization_id")
})
public class ReminderEntity implements Serializable {

	private static final long serialVersionUID = -771490219800899398L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "reminder_id", unique = true)
	private String reminderId;

	@Column(name = "person_id")
	private String personId;

	@Column(name = "organization_id")
	private String organizationId;

	@Column(name = "action", nullable = false, length = 8192)
	private String action;

	@Column(name = "case_id")
	private String caseId;

	@Column(name = "case_link", length = 512)
	private String caseLink;

	@Column(name = "reminder_date")
	private LocalDate reminderDate;

	@Column(name = "sent")
	private boolean sent;

	@Column(name = "created")
	private LocalDateTime created;

	@Column(name = "updated")
	private LocalDateTime updated;

	@PrePersist
	protected void onCreate() {
		created = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
	}

	@PreUpdate
	protected void onUpdate() {
		updated = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReminderId() {
		return reminderId;
	}

	public void setReminderId(String reminderId) {
		this.reminderId = reminderId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getCaseLink() {
		return caseLink;
	}

	public void setCaseLink(String caseLink) {
		this.caseLink = caseLink;
	}

	public LocalDate getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(LocalDate reminderDate) {
		this.reminderDate = reminderDate;
	}


	public boolean getSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ReminderEntity that = (ReminderEntity) o;
		return id == that.id && Objects.equals(reminderId, that.reminderId) && Objects.equals(personId, that.personId) && Objects.equals(organizationId, that.organizationId) && Objects.equals(action, that.action) &&
				Objects.equals(caseId, that.caseId) && Objects.equals(caseLink, that.caseLink) && Objects.equals(reminderDate, that.reminderDate) && Objects.equals(sent, that.sent) && Objects.equals(created, that.created) && Objects.equals(updated, that.updated);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, reminderId, personId, organizationId, action, caseId, caseLink, reminderDate, sent, created, updated);
	}

	@Override
	public String toString() {
		return new StringBuilder().append("ReminderEntity{id=").append(id).append(", reminderId=").append(reminderId).append(", personId=").append(personId)
				.append(", organizationId=").append(organizationId).append(", action=").append(action).append(", caseId='").append(caseId).append(", caseLink=").append(caseLink)
				.append(", reminderDate=").append(reminderDate).append(", sent=").append(sent).append(", created=").append(created).append(", updated=").append(updated).append("}").toString();
	}
}
