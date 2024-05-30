package ru.alex.mscalc.web.api;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.alex.mscalc.exception.TooLittleSalaryException;
import ru.alex.mscalc.service.CalculatorService;
import ru.alex.mscalc.web.dto.LoanOfferDto;
import ru.alex.mscalc.web.dto.LoanStatementRequestDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {

    @MockBean
    CalculatorService calculatorService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void init() {
        when(calculatorService.scoreData(any())).thenReturn(any());
        when(calculatorService.offer(new LoanStatementRequestDto())).thenReturn(List.of(new LoanOfferDto(), new LoanOfferDto(),
            new LoanOfferDto(), new LoanOfferDto()));
    }


    @Test
    @DisplayName("Receive correct data size")
    @SneakyThrows
    void correctDataSize() {
        var loanStatementRequestDto = String.format(getLoanStatementRequest(), "\"Alexandr\"", "\"sasha@mail.com\"", "\"1992-05-21\"");

        mockMvc.perform(post("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanStatementRequestDto))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    @DisplayName("Receive incorrect data size")
    @SneakyThrows
    void inCorrectDataSize() {
        var loanStatementRequestDto = String.format(getLoanStatementRequest(), "\"Alexandr\"", "\"sasha@mail.com\"", "\"1992-05-21\"");

        mockMvc.perform(post("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanStatementRequestDto))

            .andExpect(status().isOk())
            .andExpect((result) -> assertNotEquals(2, result.getResponse().getContentLength()));
    }

    @Test
    @DisplayName("Return correct exception")
    @SneakyThrows
    void correctException() {
        var loanStatementRequestDto = String.format(getLoanStatementRequest(), "\"Alexandr\"", "\"bala-bala\"", "\"1992-05-21\"");

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
        var loanStatementRequestDto = String.format(getLoanStatementRequest(), "\"Alexandr\"", "\"bala-bala\"", "\"1992-05-21\"");

        mockMvc.perform(post("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanStatementRequestDto))

            .andExpect(status().isBadRequest())
            .andExpect(result -> assertNotEquals(ClassCastException.class,
                result.getResolvedException()));
    }

    @Test
    @DisplayName("Correct MIME type application/json")
    @SneakyThrows
    void correctMIMEType() {
        var loanStatementRequestDto = String.format(getLoanStatementRequest(), "\"Alexandr\"", "\"sasha@mail.com\"", "\"1992-05-21\"");

        var result = mockMvc.perform(post("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanStatementRequestDto))

            .andExpect(status().isOk())
            .andReturn();

        assertEquals("application/json", result.getResponse().getContentType());
    }

    @Test
    @DisplayName("Correct default validate message on @IsLatin")
    @SneakyThrows
    void checkingValidateMessageInIsLatinAnnotation() {
        var loanStatementRequestDto = String.format(getLoanStatementRequest(), "\"Александр\"", "\"sasha@mail.com\"", "\"1992-05-21\"");

        mockMvc.perform(post("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanStatementRequestDto))

            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors.firstName")
                .value("Your first/middle/last name must be only latin"));
    }

    @Test
    @DisplayName("Correct default validate message on @Adult")
    @SneakyThrows
    void checkingValidateMessageInAdultAnnotation() {
        var loanStatementRequestDto = String.format(getLoanStatementRequest(),
            "\"Alexandr\"", "\"sasha@mail.com\"", "\"2020-01-30\"");

        mockMvc.perform(post("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loanStatementRequestDto))

            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors.birthdate")
                .value("Your age is less than 18 years old"));
    }

    @Test
    @DisplayName("When send valid data is OK ")
    @SneakyThrows
    void checkOffer() {
        var scoringDataDto = getScoringDataDto();

        mockMvc.perform(post("/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(scoringDataDto))

            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Correct exception message when reject ")
    @SneakyThrows
    void checkExceptionMessage() {
        when(calculatorService.scoreData(any())).thenThrow(TooLittleSalaryException.class);

        mockMvc.perform(post("/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getScoringDataDto()))

            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Sorry, you were refused a loan"));

    }

//    @Test
//    @DisplayName("@IsLatin is receive null value")
//    @SneakyThrows
//    void checkingAnnotationIsLatinOnNull() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        var loanStatementRequestDto = String.format(getLoanStatementRequest(),
//                "\"Alexandr\"", "\"sasha@mail.com\"", "\"1992-05-21\"");
//        var loanStatementRequestDtoFromMapper = objectMapper.readValue(loanStatementRequestDto, LoanStatementRequestDto.class);
//        loanStatementRequestDtoFromMapper.setMiddleName(null);
//
//
//        mockMvc.perform(post("/calculator/offers")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(loanStatementRequestDtoFromMapper)))
//
//            .andExpect(status().isOk());
//    }

    private String getLoanStatementRequest() {
        return """
            {
              "amount": 300000.00,
              "term": 6,
              "firstName": %s,
              "lastName": "Sergeev",
              "email": %s,
              "birthdate": %s,
              "passportSeries": "4456",
              "passportNumber": "346894"
            }
            """;
    }

    private String getScoringDataDto() {
        return """
            {
              "amount": 300000.00,
              "term": 6,
              "firstName": "Aleksandr",
              "lastName": "Sergeev",
              "middleName": "Yurievich",
              "gender": "MALE",
              "birthdate": "1990-05-28",
              "passportSeries": "3546",
              "passportNumber": "409672",
              "passportIssueDate": "2020-11-10",
              "passportIssueBranch": "String",
              "maritalStatus": "MARRIED",
              "dependentAmount": 30000.00,
              "employment": {
                    "employmentStatus": "EMPLOYEE",
                    "employerINN": "396829604",
                    "salary": 95000.00,
                    "position": "SIMPLE_MANAGER",
                    "workExperienceTotal": 50,
                    "workExperienceCurrent": 10
            },
              "accountNumber": "13456",
              "isInsuranceEnabled": true,
              "isSalaryClient": false
            }
            """;
    }
}