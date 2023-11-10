package com.example.host.controller;

import com.example.host.exceptions.OverlappingDatesException;
import com.example.host.entities.Block;
import com.example.host.services.BlockService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BlockControllerTest {

    private final BlockService blockService = Mockito.mock(BlockService.class);
    private final BlockController blockController = new BlockController(blockService);
    @Test
    void test_getAllBlocks_returnsAllBlocks() {
        // Arrange
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block(1L, LocalDate.now(), LocalDate.now().plusDays(1), "Reason 1"));
        blocks.add(new Block(2L, LocalDate.now().plusDays(2), LocalDate.now().plusDays(3), "Reason 2"));
        Mockito.when(blockService.getAllBlocks()).thenReturn(blocks);

        // Act
        ResponseEntity<Object> response = blockController.getAllBlocks();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(blocks, response.getBody());
    }

    @Test
    void test_getBlockById_returnsBlockWithId() {
        Long id = 1L;
        Block block = new Block(id, LocalDate.now(), LocalDate.now().plusDays(1), "Reason");
        Mockito.when(blockService.getBlockById(id)).thenReturn(Optional.of(block));

        ResponseEntity<Block> response = blockController.getBlockById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(block, response.getBody());
    }

    @Test
    void test_createBlock_createsNewBlock() {

        Block block = new Block(1L, LocalDate.now(), LocalDate.now().plusDays(1), "Reason");
        Mockito.when(blockService.createBlock(block)).thenReturn(block);

        ResponseEntity<Object> response = blockController.createBlock(block);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(block, response.getBody());
    }

    @Test
    void test_getAllBlocks_returnsNoContent() {

        Mockito.when(blockService.getAllBlocks()).thenReturn(Collections.emptyList());

        ResponseEntity<Object> response = blockController.getAllBlocks();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void test_getBlockById_returnsNotFound() {
        Long id = 1L;
        Mockito.when(blockService.getBlockById(id)).thenReturn(Optional.empty());

        ResponseEntity<Block> response = blockController.getBlockById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void test_createBlock_withOverlappingDates_throwsOverlappingDatesException() {

        Block block = new Block(1L, LocalDate.now(), LocalDate.now().plusDays(1), "Reason");
        Mockito.when(blockService.createBlock(block)).thenThrow(new OverlappingDatesException("The block overlaps with an existing block or reservation."));

        ResponseEntity<Object> response = blockController.createBlock(block);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("The block overlaps with an existing block or reservation.", response.getBody());
    }

    @Test
    void test_putRequest_updatesBlockAndReturnsStatusCode200() {
        Long id = 1L;
        Block block = new Block(id, LocalDate.now(), LocalDate.now().plusDays(1), "Reason");
        Block updatedBlock = new Block(id, LocalDate.now().plusDays(2), LocalDate.now().plusDays(3), "Updated Reason");
        Mockito.when(blockService.updateBlock(id, block)).thenReturn(updatedBlock);

        ResponseEntity<Object> response = blockController.updateBlock(id, block);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBlock, response.getBody());
    }

    @Test
    void test_postRequest_withInvalidData_returnsStatusCode500() {
        Block block = new Block(1L, LocalDate.now(), LocalDate.now().plusDays(1), "Reason");
        Mockito.when(blockService.createBlock(block)).thenThrow(new RuntimeException());

        ResponseEntity<Object> response = blockController.createBlock(block);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
