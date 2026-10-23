package br.edu.atitus.productapi.services;

import br.edu.atitus.productapi.dtos.ProductRequest;
import br.edu.atitus.productapi.dtos.ProductResponse;
import br.edu.atitus.productapi.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductEntity save(ProductRequest dto) throws Exception;

    ProductResponse findById(Long id) throws Exception;

    Page<ProductResponse> findAll(Pageable pageable) throws Exception;
}