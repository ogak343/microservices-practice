package com.example.product.service;

import com.example.product.dto.request.CategoryCreateReq;
import com.example.product.dto.request.CategoryUpdateReq;
import com.example.product.dto.resp.CategoryResp;

import java.util.List;

public interface CategoryService {
    CategoryResp create(CategoryCreateReq category);

    CategoryResp get(Long id);

    CategoryResp update(CategoryUpdateReq category);

    void delete(Long id);

    List<CategoryResp> getAll();

}
