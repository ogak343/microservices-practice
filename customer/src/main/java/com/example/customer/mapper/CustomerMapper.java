package com.example.customer.mapper;

import com.example.customer.dto.req.CustomerCreateReq;
import com.example.customer.dto.req.CustomerUpdateReq;
import com.example.customer.dto.resp.CustomerResp;
import com.example.customer.entity.Customer;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        implementationName = "CustomerMapperImpl")
public interface CustomerMapper {
    Customer toEntity(CustomerCreateReq create);

    CustomerResp toResp(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Customer entity, CustomerUpdateReq updateDto);

    @Mapping(target = "username", source = "email")
    UserRepresentation toKeycloakCreate(Customer customer);
}
