package br.edu.atitus.currencyapi.services;

import br.edu.atitus.currencyapi.dtos.CurrencyResponse;
import br.edu.atitus.currencyapi.repositories.CurrencyRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceJpa implements CurrencyService {

    private final CurrencyRepository repository;
    private final BcbClientService bcbClientService;

    @Value("${server.port:8100}")
    private String port;

    public CurrencyServiceJpa(CurrencyRepository repository, BcbClientService bcbClientService) {
        this.repository = repository;
        this.bcbClientService = bcbClientService;
    }

    @Override
    public CurrencyResponse findBySourceCurrencyAndTargetCurrency(String sourceCurrency, String targetCurrency) throws Exception {
        Double rate = null;

        // 1. Tenta buscar cotação da API externa do Banco Central
        try {
            var bcbRes = bcbClientService.getCotacaoDia(sourceCurrency, "10-14-2026");
            if (bcbRes != null && bcbRes.value() != null && !bcbRes.value().isEmpty()) {
                rate = bcbRes.value().get(0).cotacaoVenda();
            }
        } catch (Exception e) {
            // Em caso de erro/timeout no BCB, o fluxo cai silenciosamente para o Fallback do banco local abaixo
        }

        // 2. Fallback: se a API do BCB falhar ou não retornar valor, busca do banco de dados local db_currency
        if (rate == null) {
            var entity = repository.findBySourceCurrencyAndTargetCurrency(sourceCurrency, targetCurrency)
                    .orElseThrow(() -> new Exception("Currency not found for source: " + sourceCurrency + " and target: " + targetCurrency));
            rate = entity.getConversionRate();
        }

        String environment = "Currency API running in Port: " + port;
        return new CurrencyResponse(sourceCurrency, targetCurrency, rate, environment);
    }
}