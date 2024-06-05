package ru.alex.msdeal.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alex.msdeal.dto.FinishRegistrationRequestDto;
import ru.alex.msdeal.dto.LoanOfferDto;
import ru.alex.msdeal.dto.LoanStatementRequestDto;


@Service
@RequiredArgsConstructor
public class DealService {


    public List<LoanOfferDto> offer(LoanStatementRequestDto loanStatementRequestDto) {
        return null;
    }

    public void selectOffer(LoanOfferDto loanOfferDto) {

    }

    public void calculate(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId) {


    }
}
