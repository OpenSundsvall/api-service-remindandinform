package se.sundsvall.remindandinform.service;

import se.sundsvall.remindandinform.api.exception.ServiceException;
import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toMergedReminderEntity;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toReminder;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toReminderEntity;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toReminders;


@ApplicationScoped
public class ReminderService {

	@Inject
	ReminderRepository reminderRepository;

	@Transactional
	public Reminder createReminder(ReminderRequest reminderRequest) {

		String reminderId = "R-" + UUID.randomUUID();

		return toReminder(reminderRepository.persistAndFetch(toReminderEntity(reminderRequest, reminderId)));
	}

	@Transactional
	public Reminder updateReminder(UpdateReminderRequest updateReminderRequest, String reminderId) throws ServiceException{

		var optionalExistingReminderEntity = reminderRepository.findByReminderId(reminderId).orElseThrow(() -> ServiceException.create(format("No reminder with reminderId:'%s' was found!", reminderId), NOT_FOUND));

		//Find changes and create new entity to save
		var newReminderEntity = toMergedReminderEntity(optionalExistingReminderEntity, toReminderEntity(updateReminderRequest, reminderId));
		return toReminder(reminderRepository.persistAndFetch(newReminderEntity));
	}

	@Transactional
	public void deleteReminderByReminderId(String reminderId) throws ServiceException{

		var countOfDeletedEntities = reminderRepository.deleteReminderByReminderId(reminderId);

		if (countOfDeletedEntities == 0) {
			throw ServiceException.create(String.format("No reminder found for reminderId:'%s'", reminderId), NOT_FOUND);
		}
	}


	public List<Reminder> findRemindersByPersonId(String personId) throws ServiceException {
		var reminders = toReminders(reminderRepository.findByPersonId(personId));

		if(reminders.isEmpty()) {
			throw ServiceException.create(String.format("No reminders found for personId:'%s'", personId), NOT_FOUND);
		}

		return reminders;
	}

	public Reminder findReminderByReminderId(String reminderId) throws ServiceException {
		return toReminder(reminderRepository.findByReminderId(reminderId).orElseThrow(() -> ServiceException.create(String.format("No reminders found for reminderId:'%s'", reminderId), NOT_FOUND)));
	}


	public List<Reminder> findRemindersByPersonIdAndOrganizationId(String personId, String organizationId) throws ServiceException {

		var reminders = toReminders(reminderRepository.findByPersonIdAndOrganizationId(personId, organizationId));

		if (reminders.isEmpty()) {
			throw ServiceException.create(String.format("No reminder found for personId:'%s' and organizationId:'%s'", personId, organizationId), NOT_FOUND);
		}

		return reminders;
	}
}
