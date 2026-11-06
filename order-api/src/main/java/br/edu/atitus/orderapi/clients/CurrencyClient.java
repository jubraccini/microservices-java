package br.edu.atitus.orderapi.clients;

import br.edu.atitus.orderapi.dtos.CurrencyResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface CurrencyClient {

    @GetExchange("/currency-service/{amount}/{from}/{to}")
    CurrencyResponse getCurrency(
            @PathVariable("amount") Double amount,
            @PathVariable("from") String from,
            @PathVariable("to") String to);
}
