package com.example.springrestmvc.service;

import com.example.springrestmvc.mapper.CustomerMapper;
import com.example.springrestmvc.model.CustomerDTO;
import com.example.springrestmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.toDTO(customerRepository.findById(id).orElse(null)));
    }

    @Override
    public boolean deleteCustomer(UUID id, CustomerDTO customerDTO) {
        return false;
    }

    @Override
    public Optional<CustomerDTO> createCustomer(CustomerDTO customerDTO) {
        return Optional.empty();
    }

    @Override
    public void patchCustomer(UUID id, CustomerDTO customerDTO) {

    }
}
