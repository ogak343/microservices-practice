package com.example.product.service;

import com.example.product.dto.request.OrderCreate;
import com.example.product.dto.resp.OrderedProductResp;
import com.example.product.dto.resp.ProductResp;
import com.example.product.dto.request.ProductCreateReq;
import com.example.product.dto.request.ProductUpdateReq;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {
    @Transactional
    ProductResp create(ProductCreateReq product);

    ProductResp get(Long id);

    ProductResp update(ProductUpdateReq product);

    void delete(Long id);

    Page<ProductResp> search(int page, int size, Long categoryId, String nameLike);

    @Transactional
    OrderedProductResp createOrder(OrderCreate order);
}
