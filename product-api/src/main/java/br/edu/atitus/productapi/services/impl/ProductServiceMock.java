package br.edu.atitus.productapi.services.impl;

import br.edu.atitus.productapi.dtos.ProductRequest;
import br.edu.atitus.productapi.dtos.ProductResponse;
import br.edu.atitus.productapi.entities.ProductEntity;
import br.edu.atitus.productapi.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceMock implements ProductService {

    private final List<ProductResponse> mockProducts = new ArrayList<>();

    public ProductServiceMock() {
        mockProducts.add(new ProductResponse(1L, "Logitech", "MX Master 3S", "Mouse ergonômico sem fio", "USD", 99.99, "img1.png", "local", "Promo 1", "BRL", 500.00));
        mockProducts.add(new ProductResponse(2L, "Keychron", "K2 V2", "Teclado mecânico sem fio", "USD", 79.99, "img2.png", "local", "Promo 2", "BRL", 400.00));
        mockProducts.add(new ProductResponse(3L, "Dell", "UltraSharp 27", "Monitor 4K IPS", "USD", 450.00, "img3.png", "local", "Promo 3", "BRL", 2250.00));
        mockProducts.add(new ProductResponse(4L, "Sony", "WH-1000XM5", "Fone com cancelamento de ruído", "USD", 399.99, "img4.png", "local", "Promo 4", "BRL", 2000.00));
    }

    @Override
    public ProductEntity save(ProductRequest request) throws Exception {
        if (request == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requisição inválida");
        if (request.brand() == null || request.brand().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Marca inválida");
        if (request.model() == null || request.model().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modelo inválido");
        if (request.description() == null || request.description().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Descrição inválida");
        if (request.price() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Preço inválido");
        if (request.currency() == null || request.currency().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Moeda inválida");

        var product = new ProductEntity();
        product.setId((long) (mockProducts.size() + 1));
        product.setBrand(request.brand().trim());
        product.setModel(request.model().trim());
        product.setDescription(request.description().trim());
        product.setPrice(request.price());
        product.setCurrency(request.currency().trim());
        product.setImage(request.image() != null ? request.image().trim() : null);

        return product;
    }

    @Override
    public ProductResponse findById(Long id) throws Exception {
        return mockProducts.stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new Exception("Produto não encontrado"));
    }

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) throws Exception {
        return new PageImpl<>(mockProducts, pageable, mockProducts.size());
    }
}