package com.example.springrestmvc.mapper;

import com.example.springrestmvc.entities.Beer;
import com.example.springrestmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer toEntity(BeerDTO beerDTO);

    BeerDTO toDTO(Beer beer);

}
