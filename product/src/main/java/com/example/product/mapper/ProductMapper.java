package com.example.product.mapper;

import com.example.product.dto.request.ProductCreateReq;
import com.example.product.dto.request.ProductUpdateReq;
import com.example.product.dto.resp.ProductResp;
import com.example.product.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        implementationName = "ProductMapperImpl")
public interface ProductMapper {
    Product toEntity(ProductCreateReq product);

    ProductResp toResp(Product save);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Product entity, ProductUpdateReq product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "quantity", source = "integer")
    ProductResp toOrderedProduct(Product product, Integer integer);
}
