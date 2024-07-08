package com.example.order.mapper;

import com.example.order.dto.resp.OrderResp;
import com.example.order.dto.resp.ProductResp;
import com.example.order.entity.Order;
import com.example.order.entity.ProductDetails;
import com.example.order.external.messageBroker.dto.SaveOrderDto;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(target = "productDetails", qualifiedByName = "toResp")
    OrderResp toResp(Order order);

    @Named(value = "toResp")
    @IterableMapping(qualifiedByName = "toResp")
    Set<ProductResp> toRespSet(Set<ProductDetails> dtos);

    @Named(value = "toResp")
    @Mapping(target = "id", source = "productId")
    ProductResp toRespSet(ProductDetails product);

    @Mapping(target = "productDetails", qualifiedByName = "toEntity", source = "products")
    Order toEntity(SaveOrderDto dto);

    @Named(value = "toEntity")
    @IterableMapping(qualifiedByName = "toEntity")
    Set<ProductDetails> toEntitySet(Set<ProductResp> dtos);

    @Named(value = "toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", source = "id")
    ProductDetails toEntity(ProductResp product);
}
