package ru.alex.mscalc.util;

import ru.alex.mscalc.web.dto.LoanOfferDto;

import java.util.Comparator;

public class RateComparator implements Comparator<LoanOfferDto> {
    @Override
    public int compare(LoanOfferDto o1, LoanOfferDto o2) {
        return o1.getRate().doubleValue() <= o2.getRate().doubleValue() ? 0 : -1;
    }
}
