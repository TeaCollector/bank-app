package ru.alex;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.alex.mscalc.service.CalculatorService;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class MsCalcApplicationTests {

    @Autowired
    CalculatorService calculatorService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Checking endpoint /calculator/offers")
    @SneakyThrows
    void correctDataSize() {
        var loanStatementRequestDto = String.format(loanStatementRequest(), "\"Alexandr\"", "\"sasha@mail.com\"");

        mockMvc.perform(post("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanStatementRequestDto))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    @DisplayName("Return correct exception")
    @SneakyThrows
    void correctException() {
        var loanStatementRequestDto = String.format(loanStatementRequest(), "\"Александр\"", "\"2ref4511\"");

        mockMvc.perform(post("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanStatementRequestDto))

            .andExpect(status().isBadRequest())
            .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class,
                result.getResolvedException()));
    }

    @Test
    @DisplayName("Return incorrect exception")
    @SneakyThrows
    void inCorrectException() {
        var loanStatementRequestDto = String.format(loanStatementRequest(), "\"Александр\"", "\"2ref4511\"");

        mockMvc.perform(post("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanStatementRequestDto))

            .andExpect(status().isBadRequest())
            .andExpect(result -> assertNotEquals(ClassCastException.class,
                result.getResolvedException()));
    }

    @Test
    @DisplayName("Checking endpoint /calculator/calc")
    @SneakyThrows
    void correctResult() {
        var term = 6;
        var scoringData = String.format(scoringData(), term, "\"Alexandr\"");

        mockMvc.perform(post("/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(scoringData))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.monthlyPayment").value(51764.52))
            .andExpect(jsonPath("$.psk").value(310587.06))
            .andExpect(jsonPath("$.rate").value(12.00))
            .andExpect(jsonPath("$.paymentSchedule.length()").value(term))
            .andDo(MockMvcResultHandlers.print());

    }

    private String loanStatementRequest() {
        return """
            {
              "amount": 300000.00,
              "term": 6,
              "firstName": %s,
              "lastName": "Sergeev",
              "middleName": "Yurievich",
              "email": %s,
              "birthdate": "1992-05-21",
              "passportSeries": "4456",
              "passportNumber": "346894"
            }
            """;
    }

    private String scoringData() {
        return """
            {
              "amount": 300000,
              "term": %d,
              "firstName": %s,
              "lastName": "Sergeev",
              "middleName": "Yurievich",
              "gender": "MALE",
              "birthdate": "1992-07-31",
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
              "isInsuranceEnabled": false,
              "isSalaryClient": true
            }
            """;
    }
}