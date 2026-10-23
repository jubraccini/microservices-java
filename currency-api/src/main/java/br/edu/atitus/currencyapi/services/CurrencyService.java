package br.edu.atitus.currencyapi.services;

import br.edu.atitus.currencyapi.dtos.CurrencyResponse;

public interface CurrencyService {
    CurrencyResponse findBySourceCurrencyAndTargetCurrency(Double number, String sourceCurrency, String targetCurrency);
}