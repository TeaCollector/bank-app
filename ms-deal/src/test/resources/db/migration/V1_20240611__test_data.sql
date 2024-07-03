insert into client values ('7e68f4d6-0311-4a04-8ab5-e0b2082dd7ea', 'Sergeev', 'Alexandr',
                           'Yurievich', '1993-07-22', 'sashaga@gmail.com', 'MALE', 'MARRIED', 30000,
                           '{"id": "28551949-2e80-4993-a4e5-84f562300b81", "number": "423434", "series": "3443", "issueDate": [2010, 5, 13], "issueBranch": "OUFMS Russia"}',
                           '{"id": "89b24de5-e03a-40c7-ae85-b37f4c5df8d0", "salary": 95000, "employerInn": "345278567220", "employmentStatus": "EMPLOYED", "employmentsPosition": "TOP_MANAGER", "workExperienceTotal": 60, "workExperienceCurrent": 20}',
                           '3424252');

insert into credit values (
                           '360b28a7-c2c8-45f2-a197-404d38dc329a', 300000, 6, 52061.4, 14, 312368, '[{"date": [2024, 7, 12], "number": 1, "debtPayment": 48561.4, "totalAmount": 300000, "remainingDebt": 251438.6, "interestPayment": 3500.0}, {"date": [2024, 8, 12], "number": 2, "debtPayment": 49127.95, "totalAmount": 300000, "remainingDebt": 202310.65, "interestPayment": 2933.45}, {"date": [2024, 9, 12], "number": 3, "debtPayment": 49701.11, "totalAmount": 300000, "remainingDebt": 152609.54, "interestPayment": 2360.29}, {"date": [2024, 10, 12], "number": 4, "debtPayment": 50280.96, "totalAmount": 300000, "remainingDebt": 102328.58, "interestPayment": 1780.44}, {"date": [2024, 11, 12], "number": 5, "debtPayment": 50867.57, "totalAmount": 300000, "remainingDebt": 51461.01, "interestPayment": 1193.83}, {"date": [2024, 12, 12], "number": 6, "debtPayment": 51461.02, "totalAmount": 300000, "remainingDebt": -0.01, "interestPayment": 600.38}]',
                           false, false, 'CALCULATED'
                          );

insert into statement values (
                              '80b2d767-b937-49f7-a215-185f45fa3e47', '7e68f4d6-0311-4a04-8ab5-e0b2082dd7ea', '360b28a7-c2c8-45f2-a197-404d38dc329a',
                              'APPROVED', '2024-06-11 16:50:00.846522 +00:00', '{"rate": 15.0, "term": 6, "statementId": "80b2d767-b937-49f7-a215-185f45fa3e47", "totalAmount": 300000, "isSalaryClient": false, "monthlyPayment": 52210.14, "requestedAmount": 300000, "isInsuranceEnabled": false}',
                              now(), 100, '[{"time": 1718124600.8530343, "status": "Выдан список с предварительными условиями кредита", "changeType": "MANUAL"}, {"time": 1718124701.461279, "status": "Клиент выбрал оффер", "changeType": "MANUAL"}, {"time": 1718125391.1159499, "status": "Кредит высчитан, всё хорошо", "changeType": "MANUAL"}]'
                             );