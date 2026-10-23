package br.edu.atitus.currencyapi.controllers;

import br.edu.atitus.currencyapi.dtos.CurrencyResponse;
import br.edu.atitus.currencyapi.services.BcbClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/currency-service")
public class CurrencyController {

    private final BcbClientService bcbClientService;

    public CurrencyController(BcbClientService bcbClientService) {
        this.bcbClientService = bcbClientService;
    }

    @GetMapping("/{number}/{from}/{to}")
    public ResponseEntity<CurrencyResponse> getConversion(
            @PathVariable Double number,
            @PathVariable String from,
            @PathVariable String to,
            @RequestParam(required = false) String date
    ) {
        // Se não passar data na URL, usa a data de ontem formatada em MM-dd-yyyy (padrão do BCB)
        if (date == null || date.isBlank()) {
            date = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        }

        Double conversionRate = bcbClientService.getCotacao(from, date);
        Double convertedValue = number * conversionRate;

        CurrencyResponse response = new CurrencyResponse(
                from,
                to,
                conversionRate,
                convertedValue,
                "BCB Integration (Resilience4j + Caffeine)"
        );

        return ResponseEntity.ok(response);
    }
}