package com.example.customer.mapper;

import com.example.customer.dto.req.CustomerCreateReq;
import com.example.customer.dto.req.CustomerUpdateReq;
import com.example.customer.dto.resp.CustomerResp;
import com.example.customer.entity.Customer;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        implementationName = "CustomerMapperImpl")
public interface CustomerMapper {
    Customer toEntity(CustomerCreateReq create);

    CustomerResp toResp(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Customer entity, CustomerUpdateReq updateDto);

}
