package com.example.product.mapper;

import com.example.product.dto.request.CategoryCreateReq;
import com.example.product.dto.request.CategoryUpdateReq;
import com.example.product.dto.resp.CategoryResp;
import com.example.product.entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        implementationName = "CategoryMapperImpl")
public interface CategoryMapper {
    Category toEntity(CategoryCreateReq category);

    CategoryResp toResp(Category entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Category entity, CategoryUpdateReq category);
}
