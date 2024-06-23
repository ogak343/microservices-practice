package com.example.product.service;

import com.example.product.dto.resp.ProductResp;
import com.example.product.dto.request.ProductCreateReq;
import com.example.product.dto.request.ProductUpdateReq;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResp create(ProductCreateReq product);

    ProductResp get(Long id);

    ProductResp update(ProductUpdateReq product);

    void delete(Long id);

    Page<ProductResp> search(int page, int size, Long categoryId, String nameLike);

}
