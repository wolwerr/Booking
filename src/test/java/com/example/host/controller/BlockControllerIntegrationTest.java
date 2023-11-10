package com.example.host.controller;

import com.example.host.entities.Block;
import com.example.host.repositories.BlockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BlockControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlockRepository blockRepository;

    String url = "/api/v1/blocks";

    @Test
    void shouldReturnAllBlocks() throws Exception {
        this.mockMvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateBlock() throws Exception {
        String jsonRequest = "{\"startDate\":\"2021-01-01\",\"endDate\":\"2021-01-10\",\"reason\":\"Maintenance\"}";

        this.mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldDeleteBlock() throws Exception {
        this.mockMvc.perform(delete(url + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotAllowBlockStartDateAfterEndDate() throws Exception {
        String jsonRequest = "{\"startDate\":\"2022-01-10\",\"endDate\":\"2022-01-05\",\"reason\":\"Event\"}";

        this.mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }


    @Test
    @Transactional
    void shouldNotAllowOverlappingDatesForBlock() throws Exception {
        Block existingBlock = new Block();
        existingBlock.setStartDate(LocalDate.of(2022, 1, 1));
        existingBlock.setEndDate(LocalDate.of(2022, 1, 10));
        existingBlock.setReason("Maintenance");
        blockRepository.save(existingBlock);

        String jsonRequest = "{\"startDate\":\"2022-01-05\",\"endDate\":\"2022-01-15\",\"reason\":\"Private Event\"}";

        this.mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict());

    }
}
