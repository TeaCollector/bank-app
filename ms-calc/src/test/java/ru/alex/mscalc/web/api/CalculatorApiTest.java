package ru.alex.mscalc.web.api;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.alex.mscalc.service.CalculatorService;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
class CalculatorApiTest {

    @MockBean
    CalculatorService calculatorService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Receive correct data size")
    @SneakyThrows
    void correctDataSize() {
        var loanStatementRequestDto = String.format(loanStatementRequest(), "\"Alexandr\"", "\"sasha@mail.com\"");

        mockMvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loanStatementRequestDto))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

//    @Test
//    @DisplayName("Receive incorrect data size")
//    @SneakyThrows
//    void inCorrectDataSize() {
//        var loanStatementRequestDto = String.format(loanStatementRequest(), "\"Alexandr\"", "\"sasha@mail.com\"");
//
//        mockMvc.perform(post("/calculator/offers")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(loanStatementRequestDto))
//
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//    }

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
}