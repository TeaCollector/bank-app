package ru.alex.msstatement;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.alex.msstatement.service.DealFeignClient;
import ru.alex.msstatement.util.DataForTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class MsStatementApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DealFeignClient dealFeignClient;

    @BeforeEach
    void setUp() {
        when(dealFeignClient.sendLoanOffer(any())).thenReturn(DataForTest.getLoanOfferDtos());
        doNothing().when(dealFeignClient).selectOffer(DataForTest.getSelectedLoanOffer());
    }

    @Test
    @SneakyThrows
    @DisplayName("Success send request on /statement")
    void successSend() {
        mockMvc.perform(post("/statement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataForTest.getLoanStatementRequestBody()))

            .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @DisplayName("Correct content type in HTTP response")
    void correctContentType() {
        mockMvc.perform(post("/statement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataForTest.getLoanStatementRequestBody()))

            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @SneakyThrows
    @Test
    @DisplayName("Correct content type in HTTP response")
    void correctLengthOfResponse() {
        mockMvc.perform(post("/statement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataForTest.getLoanStatementRequestBody()))

            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(4));

    }

    @Test
    @SneakyThrows
    @DisplayName("Success send request on /statement/offer")
    void successSendRequest() {
        mockMvc.perform(post("/statement/offer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataForTest.getLoanStatementRequestBody()))

            .andExpect(status().isOk());
    }
}