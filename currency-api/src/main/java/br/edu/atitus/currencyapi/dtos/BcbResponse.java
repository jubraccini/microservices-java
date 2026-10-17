package br.edu.atitus.currencyapi.dtos;

import java.util.List;

public record BcbResponse(List<BcbValue> value) {
    public record BcbValue(Double cotacaoVenda) {}
}