package com.example.host.serviceTest;

import com.example.host.entities.Block;
import com.example.host.repositories.BlockRepository;
import com.example.host.services.BlockService;
import com.example.host.services.OverlapValidationService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BlockServiceTest {

    @Test
    void test_createBlockWithValidData() {
        BlockRepository blockRepository = mock(BlockRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BlockService blockService = new BlockService(blockRepository, overlapService);

        Block block = new Block();
        block.setStartDate(LocalDate.of(2022, 1, 1));
        block.setEndDate(LocalDate.of(2022, 1, 5));
        block.setReason("Vacation");

        when(blockRepository.save(any(Block.class))).thenReturn(block);
        when(overlapService.isBlockOverlap(any(LocalDate.class), any(LocalDate.class), any(Long.class))).thenReturn(false);
        when(overlapService.isBookingOverlap(any(LocalDate.class), any(LocalDate.class), any(Long.class))).thenReturn(false);

        Block result = blockService.createBlock(block);

        assertEquals(block, result);
        verify(blockRepository, times(1)).save(block);
    }


    @Test
    void test_updateBlockWithValidData() {
        Long blockId = 1L;

        Block existingBlock = new Block();
        existingBlock.setId(blockId);
        existingBlock.setStartDate(LocalDate.of(2022, 1, 1));
        existingBlock.setEndDate(LocalDate.of(2022, 1, 5));
        existingBlock.setReason("Vacation");

        Block updatedBlock = new Block();
        updatedBlock.setStartDate(LocalDate.of(2022, 2, 1));
        updatedBlock.setEndDate(LocalDate.of(2022, 2, 5));
        updatedBlock.setReason("Sick leave");

        BlockRepository blockRepository = mock(BlockRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BlockService blockService = new BlockService(blockRepository, overlapService);

        when(blockRepository.findById(blockId)).thenReturn(Optional.of(existingBlock));
        when(blockRepository.save(any(Block.class))).thenReturn(updatedBlock);
        when(overlapService.isBlockOverlap(any(LocalDate.class), any(LocalDate.class), any(Long.class))).thenReturn(false);
        when(overlapService.isBookingOverlap(any(LocalDate.class), any(LocalDate.class), any(Long.class))).thenReturn(false);

        Block result = blockService.updateBlock(blockId, updatedBlock);

        assertEquals(updatedBlock, result);
        verify(blockRepository, times(1)).save(existingBlock);
    }

    @Test
    void test_deleteBlockById() {
        Long blockId = 1L;

        BlockRepository blockRepository = mock(BlockRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BlockService blockService = new BlockService(blockRepository, overlapService);

        when(blockRepository.existsById(blockId)).thenReturn(true);

        blockService.deleteBlock(blockId);

        verify(blockRepository, times(1)).deleteById(blockId);
    }

    @Test
    void test_getAllBlocks() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block(1L, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 5), "Vacation"));
        blocks.add(new Block(2L, LocalDate.of(2022, 2, 1), LocalDate.of(2022, 2, 5), "Sick leave"));

        BlockRepository blockRepository = mock(BlockRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BlockService blockService = new BlockService(blockRepository, overlapService);

        when(blockRepository.findAll()).thenReturn(blocks);

        List<Block> result = blockService.getAllBlocks();

        assertEquals(blocks, result);
    }

    @Test
    void test_createBlockWithNullStartDate() {
        BlockRepository blockRepository = mock(BlockRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BlockService blockService = new BlockService(blockRepository, overlapService);

        Block block = new Block();
        block.setEndDate(LocalDate.of(2022, 1, 5));
        block.setReason("Vacation");

        assertThrows(NullPointerException.class, () -> blockService.createBlock(block));
        verify(blockRepository, never()).save(any(Block.class));
    }

    @Test
    void test_createBlockWithNullEndDate() {
        BlockRepository blockRepository = mock(BlockRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BlockService blockService = new BlockService(blockRepository, overlapService);

        Block block = new Block();
        block.setStartDate(LocalDate.of(2022, 1, 1));
        block.setReason("Vacation");

        assertThrows(NullPointerException.class, () -> blockService.createBlock(block));
        verify(blockRepository, never()).save(any(Block.class));
    }

    @Test
    void test_createBlockWithNullOrEmptyReason() {
        BlockRepository blockRepository = mock(BlockRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BlockService blockService = new BlockService(blockRepository, overlapService);

        Block block = new Block();
        block.setStartDate(LocalDate.of(2022, 1, 1));
        block.setEndDate(LocalDate.of(2022, 1, 5));

        assertThrows(NullPointerException.class, () -> blockService.createBlock(block));
        verify(blockRepository, never()).save(any(Block.class));
    }

    @Test
    void test_createBlockWithEndDateBeforeStartDate() {
        BlockRepository blockRepository = mock(BlockRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BlockService blockService = new BlockService(blockRepository, overlapService);

        Block block = new Block();
        block.setStartDate(LocalDate.of(2022, 1, 5));
        block.setEndDate(LocalDate.of(2022, 1, 1));
        block.setReason("Vacation");

        assertThrows(IllegalArgumentException.class, () -> blockService.createBlock(block));
        verify(blockRepository, never()).save(any(Block.class));
    }


}
