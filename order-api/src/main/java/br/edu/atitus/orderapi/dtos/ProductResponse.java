package br.edu.atitus.orderapi.dtos;

public record ProductResponse(
        Long id,
        String brand,
        String model,
        String description,
        String currency,
        Double price,
        String image,

        String environment,
        String promotionMessage,
        String targetCurrency,
        Double convertedPrice
) {}
