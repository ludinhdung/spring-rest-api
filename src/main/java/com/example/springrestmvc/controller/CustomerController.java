package com.example.springrestmvc.controller;

import com.example.springrestmvc.model.CustomerDTO;
import com.example.springrestmvc.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_ID_PATH = CUSTOMER_PATH + "/{id}";

    private final CustomerService customerService;

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_ID_PATH)
    public CustomerDTO getCustomerById(@PathVariable UUID id) {
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }
}
