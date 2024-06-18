package ru.alex.msdeal.controller;

import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.alex.msdeal.service.DealService;
import ru.alex.msdeal.util.DataForTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ExtendWith(MockitoExtension.class)
class DealControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DealService dealService;

    @BeforeEach
    void setup() {
        when(dealService.offer(any())).thenReturn(DataForTest.getLoanOfferDto());

        doThrow(new RuntimeException()).when(dealService).calculate(any(), any());
        doNothing().when(dealService).calculate(any(), any());
    }

    @SneakyThrows
    @DisplayName("Request on /deal/statement successful")
    @Test
    void successfulRequestStatement() {
        var selectedLoanOfferBody = DataForTest.getSelectedLoanOfferBody();
        mockMvc.perform(post("/deal/statement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(selectedLoanOfferBody))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(4));

    }

    @SneakyThrows
    @DisplayName("Request on /deal/offer/select successful")
    @Test
    void sendRequestSelect() {
        mockMvc.perform(post("/deal/offer/select")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataForTest.getSelectedLoanOfferBody()))

            .andExpect(status().isOk());
    }

    @SneakyThrows
    @DisplayName("Request on /deal/calculate successful")
    @Test
    void sendRequestCalculate() {
        mockMvc.perform(post("/deal/calculate/{statementId}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataForTest.getLoanStatementRequestBody()))

            .andExpect(status().isOk());
    }
}