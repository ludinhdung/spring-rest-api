package com.example.springrestmvc.service;

import com.example.springrestmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> listCustomers();

    Optional<CustomerDTO> getCustomerById(UUID id);

    boolean deleteCustomer(UUID id, CustomerDTO customerDTO);

    Optional<CustomerDTO> createCustomer(CustomerDTO customerDTO);

    void patchCustomer(UUID id, CustomerDTO customerDTO);

}
