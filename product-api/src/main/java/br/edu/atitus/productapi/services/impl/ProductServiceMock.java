package br.edu.atitus.productapi.services.impl;

import br.edu.atitus.productapi.dtos.ProductResponse;
import br.edu.atitus.productapi.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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