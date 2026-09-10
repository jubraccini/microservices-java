package br.edu.atitus.productapi.services;

import br.edu.atitus.productapi.dtos.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse findById(Long id) throws Exception;

    Page<ProductResponse> findAll(Pageable pageable) throws Exception;
}