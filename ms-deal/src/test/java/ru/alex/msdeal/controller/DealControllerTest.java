package ru.alex.msdeal.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.alex.msdeal.service.DealService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {DealController.class})
class DealControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DealService dealService;

    @BeforeEach
    void setup() {
        when(dealService.offer(any())).thenReturn(any());
    }



    @SneakyThrows
    @DisplayName("Request seccesful")
    @Test
    void sendRequest() {
        mockMvc.perform(post("/deal/statement")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(getLoanStatementRequestBody()))

                .andExpect(status().isOk());

    }

    private String getLoanStatementRequestBody() {
        return """
                {
                  "amount": 300000.00,
                  "term": 6,
                  "firstName": "Alexandr",
                  "lastName": "Sergeev",
                  "middleName": "Yurievich",
                  "email": "sasha@gmail.com",
                  "birthdate": "1992-05-21",
                  "passportSeries": "4456",
                  "passportNumber": "346894"
                }
                """;
    }

}