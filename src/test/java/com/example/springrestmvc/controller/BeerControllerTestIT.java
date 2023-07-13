package com.example.springrestmvc.controller;

import com.example.springrestmvc.entities.Beer;
import com.example.springrestmvc.mapper.BeerMapper;
import com.example.springrestmvc.model.BeerDTO;
import com.example.springrestmvc.repositories.BeerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerTestIT {
    @Autowired
    BeerController controller;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testPatchBeerBadName() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("name", "New Name 1234567890123442342342342342342342342343242342342343242342342356789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        MvcResult mvcResult = mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())

                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    void testListBeer() {

        List<BeerDTO> listBeer = controller.listBeers();

        assertThat(listBeer.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {

        beerRepository.deleteAll();

        List<BeerDTO> listBeer = controller.listBeers();

        assertThat(listBeer.size()).isEqualTo(0);
    }

    @Test
    void testGetBeerById() {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = controller.listBeers().get(0);

        assertThat(beer.getId()).isEqualTo(beerDTO.getId());
    }

    @Test
    void testBeerIdNotFound() {
        assertThrows(NotFoundException.class, () -> controller.getBeer(UUID.randomUUID()));
    }

    @Test
    void testCreateNewBeer() {
        BeerDTO beerDTO = BeerDTO.builder().name("New beer").build();

        ResponseEntity responseEntity = controller.creatBeer(beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));

        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");

        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer beer = beerRepository.findById(savedUUID).get();

        assertThat(beer).isNotNull();

    }

    @Test
    void testUpdateBeerById() {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO beerDTO = beerMapper.toDTO(beer);
        beerDTO.setId(null);
        final String name = "UPDATE";
        beerDTO.setName(name);

        ResponseEntity responseEntity = controller.updateBeerById(beer.getId(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer savedBeer = beerRepository.findById(beer.getId()).get();

        assertThat(savedBeer.getName()).isEqualTo(beerDTO.getName());
    }

    @Test
    void testUpdateNotFoundId() {
        //Expect update method should throw exception class
        assertThrows(
                NotFoundException.class, () -> {
                    controller.updateBeerById(UUID.randomUUID(), BeerDTO.builder().build());
                }
        );
    }

    @Rollback
    @Transactional
    @Test
    void testBeerDeleteNotFoundId() {
        assertThrows(NotFoundException.class, () -> {
            controller.deleteBeerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteBeerById() {
        Beer beer = beerRepository.findAll().get(0);

        controller.deleteBeerById(beer.getId());

        assertThat(beerRepository.findById(beer.getId())).isEmpty();

    }
}