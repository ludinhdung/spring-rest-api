package com.example.springrestmvc.controller;

import com.example.springrestmvc.entities.Customer;
import com.example.springrestmvc.model.CustomerDTO;
import com.example.springrestmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerControllerTest {
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testListCustomer() {
        List<CustomerDTO> dtos = customerController.listCustomers();

        assertThat(dtos.size()).isEqualTo(customerRepository.findAll().size());
    }

    @Test
    void testGetCustomerById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());

        assertThat(customer.getId()).isEqualTo(customerDTO.getId());
    }
}