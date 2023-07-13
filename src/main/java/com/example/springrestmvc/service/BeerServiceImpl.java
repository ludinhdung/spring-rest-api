package com.example.springrestmvc.service;

import com.example.springrestmvc.model.BeerDTO;
import com.example.springrestmvc.model.BeerStyle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BeerServiceImpl implements BeerService {
    private final HashMap<UUID, BeerDTO> beerMap = new HashMap<>();

    public BeerServiceImpl() {
        BeerDTO beer1 = BeerDTO.builder().id(UUID.randomUUID()).name("good beer").price(new BigDecimal(123)).beerStyle(BeerStyle.SAISON).version(1).upc("123").quantityOnHand(111).createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();

        BeerDTO beer2 = BeerDTO.builder().id(UUID.randomUUID()).beerStyle(BeerStyle.ALE).version(1).upc("123").quantityOnHand(222).createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();

        BeerDTO beer3 = BeerDTO.builder().id(UUID.randomUUID()).beerStyle(BeerStyle.GOSE).version(1).upc("123").quantityOnHand(333).createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<BeerDTO> listBeer() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional getBeerById(UUID id) {
        return Optional.of(beerMap.get(id));
    }

    @Override
    public boolean deleteBeer(UUID id) {
        beerMap.remove(id);
        return true;
    }

    @Override
    public BeerDTO createBeer(BeerDTO beer) {

        BeerDTO savedBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .name(beer.getName())
                .price(beer.getPrice())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer) {
        BeerDTO existing = beerMap.get(id);

        existing.setName(beer.getName());
        existing.setBeerStyle(beer.getBeerStyle());
        existing.setUpc(beer.getUpc());
        existing.setVersion(beer.getVersion());
        existing.setPrice(beer.getPrice());
        existing.setQuantityOnHand(beer.getQuantityOnHand());

        beerMap.put(existing.getId(), existing);

        return Optional.of(existing);
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
        return Optional.empty();
    }
}
