package com.example.springrestmvc.controller;

import com.example.springrestmvc.model.BeerDTO;
import com.example.springrestmvc.service.BeerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BeerController {
    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = "/api/v1/beer/{id}";

    private final BeerService beerService;

    @GetMapping(value = BEER_PATH)
    public List<BeerDTO> listBeers() {
        return beerService.listBeer();
    }

    @PostMapping(value = BEER_PATH)
    public ResponseEntity creatBeer(@Validated @RequestBody BeerDTO beer) {

        BeerDTO savedBeer = beerService.createBeer(beer);

        HttpHeaders headers = new HttpHeaders();

        headers.add("Location", BEER_PATH + "/" + savedBeer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = BEER_PATH_ID)
    public ResponseEntity updateBeerById(@PathVariable("id") UUID id, @RequestBody BeerDTO beer) {
        if (beerService.updateBeer(id, beer).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeerById(@PathVariable("id") UUID id) {
        if (!beerService.deleteBeer(id)) {
            throw new NotFoundException();
        }
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeer(@PathVariable("id") UUID id) {
        return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity updateBeerPatchById(@PathVariable("id") UUID beerId,@RequestBody BeerDTO beer) {

        beerService.patchBeerById(beerId, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
