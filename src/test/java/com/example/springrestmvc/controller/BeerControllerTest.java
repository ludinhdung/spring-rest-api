package com.example.springrestmvc.controller;

import com.example.springrestmvc.model.BeerDTO;
import com.example.springrestmvc.service.BeerService;
import com.example.springrestmvc.service.BeerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.example.springrestmvc.controller.BeerController.BEER_PATH;
import static com.example.springrestmvc.controller.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> idCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerCaptor;
    BeerServiceImpl beerServiceImpl = new BeerServiceImpl();


    @Test
    void getBeerById() throws Exception {

        BeerDTO testBeer = beerServiceImpl.listBeer().get(0);

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.of(testBeer));

        RequestBuilder requestBuilder = get(BEER_PATH_ID, UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Is.is(testBeer.getId().toString())));

    }

    @Test
    void testListBeer() throws Exception {
        given(beerService.listBeer()).willReturn(beerServiceImpl.listBeer());

        RequestBuilder requestBuilder = get(BEER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("$.length()", Is.is(3)));
    }

    @Test
    void testCreateBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeer().get(0);

        given(beerService.createBeer(any(BeerDTO.class))).willReturn(beer);

        RequestBuilder requestBuilder = post(BEER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(beer));

        mockMvc.perform(requestBuilder).andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void testCreateBeerNullName() throws Exception {
        BeerDTO beer = BeerDTO.builder().build();

        given(beerService.createBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeer().get(0));

        RequestBuilder requestBuilder = post(BEER_PATH).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer));

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeer().get(0);

        RequestBuilder requestBuilder = put(BEER_PATH_ID, beer.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(beer));

        mockMvc.perform(requestBuilder);

        verify(beerService).updateBeer(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void testDeleteBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeer().get(0);

        given(beerService.deleteBeer(any(UUID.class))).willReturn(true);

        RequestBuilder requestBuilder = delete(BEER_PATH_ID, beer.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).contentType(objectMapper.writeValueAsString(beer));

        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());

        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);

        verify(beerService).deleteBeer(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).isEqualTo(beer.getId());

    }

    @Test
    void testPatchBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeer().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("name", "new name");

        RequestBuilder requestBuilder = patch(BEER_PATH_ID, beer.getId()).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(beerMap));

        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());

        verify(beerService).patchBeerById(idCaptor.capture(), beerCaptor.capture());

        assertThat(beer.getId()).isEqualTo(idCaptor.getValue());
        assertThat(beerMap.get("name")).isEqualTo("new name");
    }

    @Test
    void testHandleException() throws Exception {
        System.out.println(UUID.randomUUID());
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        RequestBuilder requestBuilder = get(BEER_PATH_ID, UUID.randomUUID());

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());

    }
}