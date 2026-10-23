package br.edu.atitus.currencyapi.services;

import br.edu.atitus.currencyapi.clients.BcbClient;
import br.edu.atitus.currencyapi.entities.CurrencyEntity;
import br.edu.atitus.currencyapi.repositories.CurrencyRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BcbClientService {

    private final BcbClient bcbClient;
    private final CurrencyRepository currencyRepository;

    public BcbClientService(BcbClient bcbClient, CurrencyRepository currencyRepository) {
        this.bcbClient = bcbClient;
        this.currencyRepository = currencyRepository;
    }

    @Cacheable(value = "bcbCache", key = "#moeda + '-' + #dataCotacao")
    @Retry(name = "bcbRetry")
    @CircuitBreaker(name = "bcbCircuitBreaker", fallbackMethod = "fallbackGetCotacao")
    public Double getCotacao(String moeda, String dataCotacao) {
        var response = bcbClient.getCotacaoDia("'" + moeda + "'", "'" + dataCotacao + "'", "json");

        if (response != null && response.value() != null && !response.value().isEmpty()) {
            return response.value().get(0).cotacaoVenda();
        }

        // Se o BCB responder sem dados no dia, força o fallback pro banco local
        return fallbackGetCotacao(moeda, dataCotacao, new RuntimeException("Sem cotação disponível no BCB"));
    }

    // Fallback: busca no PostgreSQL local se o BCB falhar ou estiver sem dados
    public Double fallbackGetCotacao(String moeda, String dataCotacao, Throwable t) {
        return currencyRepository.findBySourceCurrencyAndTargetCurrency(moeda, "BRL")
                .map(CurrencyEntity::getConversionRate)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cotação não encontrada nem na API externa nem no banco local para a moeda: " + moeda
                ));
    }
}