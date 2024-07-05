package com.example.customer.service;

import com.example.customer.dto.req.CustomerConfirmReq;
import com.example.customer.dto.req.CustomerCreateReq;
import com.example.customer.dto.req.CustomerUpdateReq;
import com.example.customer.dto.req.LoginReq;
import com.example.customer.dto.resp.CustomerResp;
import com.example.customer.dto.resp.LoginResp;

public interface CustomerService {
    Long create(CustomerCreateReq customer);

    CustomerResp profile();

    CustomerResp update(CustomerUpdateReq dto);

    void delete();

    LoginResp confirm(CustomerConfirmReq customer);

    LoginResp login(LoginReq login);
}
