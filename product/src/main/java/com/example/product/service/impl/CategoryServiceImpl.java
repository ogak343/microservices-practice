package com.example.product.service.impl;

import com.example.product.config.exception.CustomException;
import com.example.product.contants.ErrorCode;
import com.example.product.dto.request.CategoryCreateReq;
import com.example.product.dto.request.CategoryUpdateReq;
import com.example.product.dto.resp.CategoryResp;
import com.example.product.mapper.CategoryMapper;
import com.example.product.repository.CategoryRepository;
import com.example.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "CATEGORY")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public CategoryResp create(CategoryCreateReq category) {

        if (repository.existsByName(category.getName())) {
            throw new CustomException(ErrorCode.CATEGORY_EXISTS);
        }

        var entity = repository.save(mapper.toEntity(category));

        return mapper.toResp(entity);
    }

    @Override
    public CategoryResp get(Long id) {
        return mapper.toResp(repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)));
    }

    @Override
    public CategoryResp update(CategoryUpdateReq category) {

        var entity = repository.findById(category.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        mapper.update(entity, category);

        return mapper.toResp(repository.save(entity));
    }

    @Override
    public void delete(Long id) {

        repository.deleteById(id);
    }

    @Override
    public List<CategoryResp> getAll() {
        return repository.findAll().stream().map(mapper::toResp).toList();
    }
}
