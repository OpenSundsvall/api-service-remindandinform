-------------------------------------
-- UpdateReminderTest.test1
-------------------------------------
INSERT INTO reminder(action, case_id, case_link, organization_id, person_id, reminder_date, reminder_id, sent)
VALUES('action', 'caseId', 'caseLink', 'fbfbd90c-4c47-11ec-81d3-0242ac130012', 'fbfbd90c-4c47-11ec-81d3-0242ac130003', '2021-11-25', 'R-fbfbd90c-4c47-11ec-81d3-0242ac130004', False);

-------------------------------------
-- ReadRemindersTest.test1
-------------------------------------
INSERT INTO reminder(action, case_id, case_link, organization_id, person_id, reminder_date, reminder_id, sent)
VALUES('Förnya tillstånd', 'caseId', 'caseLink', 'fbfbd90c-4c47-11ec-81d3-0242ac130012', 'fbfbd90c-4c47-11ec-81d3-0242ac130001', '2021-11-25', 'R-fbfbd90c-4c47-11ec-81d3-0242ac130001', False);
INSERT INTO reminder(action, case_id, case_link, organization_id, person_id, reminder_date, reminder_id, sent)
VALUES('Det är julafton', 'caseId2', 'caseLink2', 'fbfbd90c-4c47-11ec-81d3-0242ac130013', 'fbfbd90c-4c47-11ec-81d3-0242ac130001', '2021-12-24', 'R-fbfbd90c-4c47-11ec-81d3-0242ac130002', False);

-------------------------------------
-- ReadRemindersTest.test2
-------------------------------------
INSERT INTO reminder(action, case_id, case_link, organization_id, person_id, reminder_date, reminder_id, sent)
VALUES('Förbered inför julfest', 'caseId', 'caseLink', 'fbfbd90c-4c47-11ec-81d3-0242ac130013', 'fbfbd90c-4c47-11ec-81d3-0242ac130002', '2021-11-26', 'R-fbfbd90c-4c47-11ec-81d3-0242ac130003', False);
INSERT INTO reminder(action, case_id, case_link, organization_id, person_id, reminder_date, reminder_id, sent)
VALUES('Julfest', 'caseId2', 'caseLink2', 'fbfbd90c-4c47-11ec-81d3-0242ac130013', 'fbfbd90c-4c47-11ec-81d3-0242ac130002', '2021-11-25', 'R-fbfbd90c-4c47-11ec-81d3-0242ac130005', False);

-------------------------------------
-- DeleteReminderTest.test1
-------------------------------------
INSERT INTO reminder(action, case_id, case_link, organization_id, person_id, reminder_date, reminder_id, sent)
VALUES('Reminder som ska tas bort', 'caseId', 'caseLink', 'fbfbd90c-4c47-11ec-81d3-0242ac130013', 'fbfbd90c-4c47-11ec-81d3-0242ac130004', '2021-11-26', 'R-fbfbd90c-4c47-11ec-81d3-0242ac130006', False);