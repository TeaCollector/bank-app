# Простое банковское приложение на микросервисах.

####  После запуска приложения по URL: `http://localhost:8080` можно потестировать: 
1. Для эндпойнта: `/calculator/offers` можно использовать такие данные:
```
{
    "amount": 300000.00,
    "term": 6,
    "firstName": "Alexandr",
    "lastName": "Sergeev",
    "middleName": "Yurievich",
    "email": "sasha@mail.com,
    "birthdate": "1992-05-21",
    "passportSeries": "4456",
    "passportNumber": "346894"
}
```
2. Для: `/calculator/calc`:
```
{
  "amount": 300000,
  "term": 6,
  "firstName": "Alexandr",
  "lastName": "Sergeev",
  "middleName": "Yurievich",
  "gender": "FEMALE",
  "birthdate": "1990-02-04",
  "passportSeries": "4561",
  "passportNumber": "123456",
  "passportIssueDate": "2020-04-01",
  "passportIssueBranch": "OUFMS Russia",
  "maritalStatus": "MARRIED",
  "dependentAmount": 3000,
  "employment": {
     "employmentStatus": "EMPLOYEE",
     "employerINN": "31565562234",
     "salary": 95000,
     "position": "TOP_MANAGER",
     "workExperienceTotal": 50,
     "workExperienceCurrent": 27
    },
  "accountNumber": "342263453245373",
  "isInsuranceEnabled": true,
  "isSalaryClient": false
}
```

3 Swagger находится по адресу: `http://localhost:8080/swagger.html`