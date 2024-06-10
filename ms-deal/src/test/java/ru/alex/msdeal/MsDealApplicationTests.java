package ru.alex.msdeal;

import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.alex.msdeal.dto.LoanOfferDto;
import ru.alex.msdeal.dto.LoanStatementRequestDto;
import ru.alex.msdeal.repository.StatementRepository;
import ru.alex.msdeal.service.CalculatorFeignClient;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith({MockitoExtension.class})
@Transactional
class MsDealApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CalculatorFeignClient calculatorFeignClient;

    @Autowired
    StatementRepository statementRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        when(calculatorFeignClient.sendLoanOffer(any())).thenReturn(getLoanOfferDto());
    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test
    @DisplayName("Correct content type in HTTP response")
    void correctContentType() {
        mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getLoanStatementRequestBody()))

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
                        .content(getLoanStatementRequestBody()))

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
                        .content(getLoanStatementRequestBody()))

                .andExpect(status().isOk())
                .andExpect(jsonPath("[?($.[0].statementId == $.[1].statementId && " +
                                    "$.[1].statementId == $.[2].statementId && " +
                                    "$.[2].statementId == $.[3].statementId)]").hasJsonPath());
    }

    @SneakyThrows
    @Test
    @DisplayName("The client that was sent and saved is same")
    void clientEquals() {
        var objectMapper = new ObjectMapper();
        var loanStatement = getLoanStatementObject();

        var jsonLoanStatement = objectMapper.writeValueAsString(loanStatement);

        var response = mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoanStatement))

                .andReturn();

        var statementId = UUID.fromString(JsonPath.read(response.getResponse().getContentAsString(),
                "$.statementId"));

        var statementEntity = statementRepository.getReferenceById(statementId);
        var clientEntity = statementEntity.getClient();

        assertAll("Client must be the same",
                () -> assertEquals(loanStatement.getFirstName(), clientEntity.getFirstName()),
                () -> assertEquals(loanStatement.getLastName(), clientEntity.getLastName())
        );
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

    private LoanStatementRequestDto getLoanStatementObject() {
        return LoanStatementRequestDto.builder()
                .email("sasha@gmail.com")
                .amount(BigDecimal.valueOf(300000.00))
                .passportSeries("4456")
                .passportNumber("346894")
                .birthdate(LocalDate.of(1992, 5, 21))
                .firstName("Alexandr")
                .lastName("Sergeev")
                .middleName("Yurievich")
                .term(6)
                .build();
    }

    private List<LoanOfferDto> getLoanOfferDto() {
        var loanOfferDto1 = LoanOfferDto.builder()
                .rate(BigDecimal.valueOf(15.00))
                .requestAmount(BigDecimal.valueOf(300000.00))
                .totalAmount(BigDecimal.valueOf(300000.00))
                .monthlyPayment(BigDecimal.valueOf(52210.14))
                .term(6)
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        var loanOfferDto2 = LoanOfferDto.builder()
                .rate(BigDecimal.valueOf(13.00))
                .requestAmount(BigDecimal.valueOf(300000.00))
                .totalAmount(BigDecimal.valueOf(300000.00))
                .monthlyPayment(BigDecimal.valueOf(51912.87))
                .term(6)
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();

        var loanOfferDto3 = LoanOfferDto.builder()
                .rate(BigDecimal.valueOf(12.00))
                .requestAmount(BigDecimal.valueOf(300000.00))
                .totalAmount(BigDecimal.valueOf(306750.00))
                .monthlyPayment(BigDecimal.valueOf(52929.22))
                .term(6)
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build();

        var loanOfferDto4 = LoanOfferDto.builder()
                .rate(BigDecimal.valueOf(10.00))
                .requestAmount(BigDecimal.valueOf(300000.00))
                .totalAmount(BigDecimal.valueOf(306750.00))
                .monthlyPayment(BigDecimal.valueOf(52626.46))
                .term(6)
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        return List.of(loanOfferDto1, loanOfferDto2, loanOfferDto3, loanOfferDto4);
    }
}
