package ru.alex.msdeal;

import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.alex.msdeal.repository.StatementRepository;
import ru.alex.msdeal.service.CalculatorFeignClient;
import ru.alex.msdeal.util.DataForTest;
import ru.alex.msdeal.util.PostgresTestContainer;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith({MockitoExtension.class})
@Transactional
@Import(PostgresTestContainer.class)
class MsDealApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CalculatorFeignClient calculatorFeignClient;

    @Autowired
    StatementRepository statementRepository;

    @BeforeEach
    void setUp() {
        when(calculatorFeignClient.sendLoanOffer(any())).thenReturn(DataForTest.getLoanOfferDto());
        when(calculatorFeignClient.calculateCreditOffer(any())).thenReturn(DataForTest.getCreditDto());
    }

    @SneakyThrows
    @Test
    @DisplayName("Correct content type in HTTP response")
    void correctContentType() {
        mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataForTest.getLoanStatementRequestBody()))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @SneakyThrows
    @Test
    @DisplayName("Correct returned type in HTTP response")
    void correctContentLengthType() {
        mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataForTest.getLoanStatementRequestBody()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @SneakyThrows
    @Test
    @DisplayName("All statementId in loan offer are equals")
    void equalStatementIdInLoanOffer() {
        mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataForTest.getLoanStatementRequestBody()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("[?($.[0].statementId == $.[1].statementId && " +
                                    "$.[1].statementId == $.[2].statementId && " +
                                    "$.[2].statementId == $.[3].statementId)]").hasJsonPath());
    }

    @SneakyThrows
    @Test
    @DisplayName("Correct saved statement in db")
    void correctSavedStatement() {
        var response = mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataForTest.getLoanStatementRequestBody()))

                .andReturn();

        var statementId = UUID.fromString(JsonPath.read(response.getResponse().getContentAsString(),
                "$.[0].statementId"));

        var statementEntity = statementRepository.getReferenceById(statementId);

        assertNotNull(statementEntity);
    }

    @SneakyThrows
    @Test
    @DisplayName("The client that was sent and saved is same")
    void clientEquals() {
        var loanStatement = DataForTest.getLoanStatementObject();

        var response = mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataForTest.getLoanStatementRequestBody()))

                .andReturn();

        var statementId = UUID.fromString(JsonPath.read(response.getResponse().getContentAsString(),
                "$.[0].statementId"));

        var statementEntity = statementRepository.getReferenceById(statementId);
        var clientEntity = statementEntity.getClient();

        assertAll("Client must be the same",
                () -> assertEquals(loanStatement.getFirstName(), clientEntity.getFirstName()),
                () -> assertEquals(loanStatement.getLastName(), clientEntity.getLastName()),
                () -> assertEquals(loanStatement.getFirstName(), clientEntity.getFirstName()),
                () -> assertEquals(loanStatement.getMiddleName(), clientEntity.getMiddleName()),
                () -> assertEquals(loanStatement.getBirthdate(), clientEntity.getBirthdate()),
                () -> assertEquals(loanStatement.getEmail(), clientEntity.getEmail())
        );
    }

    @SneakyThrows
    @Test
    @DisplayName("Selected offer saved correct")
    void correctSaveSelectedOffer() {
        var loanStatement = DataForTest.getLoanStatementObject();

        var response = mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataForTest.getLoanStatementRequestBody()))

                .andReturn();

        var statementId = UUID.fromString(JsonPath.read(response.getResponse().getContentAsString(),
                "$.[0].statementId"));

        var statementEntity = statementRepository.getReferenceById(statementId);
        var clientEntity = statementEntity.getClient();

        assertAll("Client must be the same",
                () -> assertEquals(loanStatement.getFirstName(), clientEntity.getFirstName()),
                () -> assertEquals(loanStatement.getLastName(), clientEntity.getLastName()),
                () -> assertEquals(loanStatement.getFirstName(), clientEntity.getFirstName()),
                () -> assertEquals(loanStatement.getMiddleName(), clientEntity.getMiddleName()),
                () -> assertEquals(loanStatement.getBirthdate(), clientEntity.getBirthdate()),
                () -> assertEquals(loanStatement.getEmail(), clientEntity.getEmail())
        );
    }

    @SneakyThrows
    @Test
    @DisplayName("Correct update selected offer")
    void correctUpdatedSelectedOffer() {
        var objectMapper = new ObjectMapper();
        var responseWithOffers = mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataForTest.getLoanStatementRequestBody()))
                .andReturn();

        var statementId = UUID.fromString(JsonPath.read(responseWithOffers.getResponse().getContentAsString(),
                "$.[0].statementId"));

        var selectedOffer = DataForTest.getSelectedLoanOffer();
        selectedOffer.setStatementId(statementId);

        mockMvc.perform(post("/deal/offer/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selectedOffer)))

                .andExpect(status().isOk());

        var statementEntity = statementRepository.getReferenceById(statementId);

        var appliedOffer = statementEntity.getAppliedOffer();

        assertAll("Applied offer and selected offer equals",
                () -> assertEquals(selectedOffer.getRate(), appliedOffer.rate()),
                () -> assertEquals(selectedOffer.getRate(), appliedOffer.rate()),
                () -> assertEquals(selectedOffer.getTerm(), appliedOffer.term()),
                () -> assertEquals(selectedOffer.getStatementId(), appliedOffer.statementId()),
                () -> assertEquals(selectedOffer.getRequestAmount(), appliedOffer.requestedAmount()),
                () -> assertEquals(selectedOffer.getMonthlyPayment(), appliedOffer.monthlyPayment()),
                () -> assertEquals(selectedOffer.getIsInsuranceEnabled(), appliedOffer.isInsuranceEnabled()),
                () -> assertEquals(selectedOffer.getIsSalaryClient(), appliedOffer.isSalaryClient()));
    }

    @SneakyThrows
    @Test
    @DisplayName("Correct update selected offer")
    void correctSaveCreditEntity() {
        var objectMapper = new ObjectMapper();
        var responseWithOffers = mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataForTest.getLoanStatementRequestBody()))
                .andReturn();

        var statementId = UUID.fromString(JsonPath.read(responseWithOffers.getResponse().getContentAsString(),
                "$.[0].statementId"));

        var selectedOffer = DataForTest.getSelectedLoanOffer();
        selectedOffer.setStatementId(statementId);

        mockMvc.perform(post("/deal/offer/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(selectedOffer)))

                .andExpect(status().isOk());

        mockMvc.perform(post("/deal/calculate/{statementId}", statementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataForTest.getFinishRegistrationRequestDto()))

                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @DisplayName("Throw exception when statement status not pre approved")
    void throwExceptionIfStatementNotPreApproved() {
        var responseWithOffers = mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataForTest.getLoanStatementRequestBody()))
                .andReturn();

        var statementId = UUID.fromString(JsonPath.read(responseWithOffers.getResponse().getContentAsString(),
                "$.[0].statementId"));

        var selectedOffer = DataForTest.getSelectedLoanOffer();
        selectedOffer.setStatementId(statementId);

        mockMvc.perform(post("/deal/calculate/{statementId}", statementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataForTest.getFinishRegistrationRequestDto()))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Sorry, your loan was refused: you not pre approved your statement"));

    }
}