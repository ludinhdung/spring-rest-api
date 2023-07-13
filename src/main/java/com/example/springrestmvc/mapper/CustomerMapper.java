package com.example.springrestmvc.mapper;

import com.example.springrestmvc.entities.Customer;
import com.example.springrestmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer toEntity(CustomerDTO customerDTO);

    CustomerDTO toDTO(Customer customer);


}
