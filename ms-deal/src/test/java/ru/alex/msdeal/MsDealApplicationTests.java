package ru.alex.msdeal;

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
import ru.alex.msdeal.dto.LoanOfferDto;
import ru.alex.msdeal.dto.LoanStatementRequestDto;
import ru.alex.msdeal.service.CalculatorFeignClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith({MockitoExtension.class})
class MsDealApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CalculatorFeignClient calculatorFeignClient;

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
    void justTest() {
        mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getLoanStatementRequestBody()))

                .andExpect(status().isOk());
    }

    private String getLoanStatementRequestBody() {
        var loanStatementReuest = LoanStatementRequestDto.builder()
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
