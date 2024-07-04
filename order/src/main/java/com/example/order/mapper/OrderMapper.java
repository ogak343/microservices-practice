package com.example.order.mapper;

import com.example.order.dto.resp.OrderResp;
import com.example.order.dto.resp.ProductResp;
import com.example.order.entity.Order;
import com.example.order.entity.ProductDetails;
import com.example.order.external.messageBroker.dto.OrderCreatePublishDto;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(target = "productDetails.quantity", ignore = true)
    OrderResp toResp(Order order);

    @Mapping(target = "productDetails", qualifiedByName = "toEntity", source = "orderedProducts")
    Order toEntity(OrderCreatePublishDto dto);

    @Named(value = "toEntity")
    @IterableMapping(qualifiedByName = "toEntity")
    Set<ProductDetails> toEntitySet(Set<ProductResp> dtos);

    @Named(value = "toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", source = "id")
    ProductDetails toEntity(ProductResp product);
}
