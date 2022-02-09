package se.sundsvall.remindandinform.integration.db;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ReminderRepository implements PanacheRepository<ReminderEntity> {

	public List<ReminderEntity> findByPersonId(String personId) {
		return list("FROM ReminderEntity WHERE personId = ?1",
				personId);
	}

	public Optional<ReminderEntity> findByReminderId(String reminderId) {
		return find("reminderId = :reminderId",
				Parameters.with("reminderId", reminderId)).firstResultOptional();
	}

	public List<ReminderEntity> findByPersonIdAndOrganizationId(String personId, String organizationId) {
			return list("FROM ReminderEntity WHERE personId = ?1 and organizationId = ?2",
					personId, organizationId);
	}

	public List<ReminderEntity> findByReminderDateNotSent(LocalDate reminderDate) {
		return list("FROM ReminderEntity WHERE reminderDate <= ?1 and sent = false",
				reminderDate);

	}

	public void updateSentByReminderDate(LocalDate reminderDate) {
		update("sent = true WHERE reminderDate <= ?1 and sent = false",
				reminderDate);

	}

	public long deleteReminderByReminderId(String reminderId) {
		return delete("reminderId = :reminderId",
		Parameters.with("reminderId", reminderId));
	}

	public ReminderEntity persistAndFetch(ReminderEntity reminderEntity) {
		this.persistAndFlush(reminderEntity);
		return this.findById(reminderEntity.getId());
	}
}
