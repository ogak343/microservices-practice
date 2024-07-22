package com.example.customer.service;

import com.example.customer.dto.req.CustomerConfirmReq;
import com.example.customer.dto.req.CustomerCreateReq;
import com.example.customer.dto.req.CustomerUpdateReq;
import com.example.customer.dto.resp.CustomerResp;
import com.example.customer.dto.resp.ConfirmResp;

public interface CustomerService {
    Long create(CustomerCreateReq customer);

    CustomerResp profile();

    CustomerResp update(CustomerUpdateReq dto);

    void delete();

    ConfirmResp confirm(CustomerConfirmReq customer);
}
