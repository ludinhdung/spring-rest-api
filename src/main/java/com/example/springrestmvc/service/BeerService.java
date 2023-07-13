package com.example.springrestmvc.service;

import com.example.springrestmvc.entities.Beer;
import com.example.springrestmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> listBeer();

    Optional<BeerDTO> getBeerById(UUID id);

    boolean deleteBeer(UUID id);

    BeerDTO createBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer);
    Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beer);
}
