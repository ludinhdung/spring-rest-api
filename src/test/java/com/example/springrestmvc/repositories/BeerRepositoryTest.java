package com.example.springrestmvc.repositories;

import com.example.springrestmvc.entities.Beer;
import com.example.springrestmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        assertThrows(ConstraintViolationException.class, () -> {
                    Beer savedBeer = beerRepository.save(Beer.builder().build());
                    beerRepository.flush();
                }
        );
//        assertThat(savedBeer).isNotNull();
//        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSaveBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            Beer savedBeer = beerRepository.save(Beer.builder()
                    .name("New Name 12312421341231242134123124213412312421341231242134123124213412312421341231242134123124213412312421341231242134123124213412312421341231242134")
                    .upc("313")
                    .beerStyle(BeerStyle.GOSE)
                    .price(new BigDecimal(132))
                    .build());

            beerRepository.flush();
        });
    }
}
