package br.edu.atitus.currencyapi.services;

import br.edu.atitus.currencyapi.dtos.CurrencyResponse;
import br.edu.atitus.currencyapi.entities.CurrencyEntity;
import br.edu.atitus.currencyapi.repositories.CurrencyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CurrencyServiceJpa implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final BcbClientService bcbClientService;

    public CurrencyServiceJpa(CurrencyRepository currencyRepository, BcbClientService bcbClientService) {
        this.currencyRepository = currencyRepository;
        this.bcbClientService = bcbClientService;
    }

    @Override
    public CurrencyResponse findBySourceCurrencyAndTargetCurrency(Double number, String sourceCurrency, String targetCurrency) {
        String date = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        Double conversionRate;
        try {
            conversionRate = bcbClientService.getCotacao(sourceCurrency, date);
        } catch (Exception e) {
            conversionRate = currencyRepository.findBySourceCurrencyAndTargetCurrency(sourceCurrency, targetCurrency)
                    .map(CurrencyEntity::getConversionRate)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Moeda não encontrada no BCB nem no banco local"
                    ));
        }

        Double convertedValue = number * conversionRate;

        return new CurrencyResponse(
                sourceCurrency,
                targetCurrency,
                conversionRate,
                convertedValue,
                "BCB Integration (Resilience4j + Caffeine)"
        );
    }
}