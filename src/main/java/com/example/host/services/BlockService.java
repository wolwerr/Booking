package com.example.host.services;

import com.example.host.exceptions.BlockNotFoundException;
import com.example.host.exceptions.OverlappingDatesException;
import com.example.host.entities.Block;
import com.example.host.repositories.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BlockService {

    private final BlockRepository blockRepository;

    private final OverlapValidationService overlapService;

    public Block createBlock(Block block) throws OverlappingDatesException, NullPointerException, IllegalArgumentException  {

        if (block.getStartDate() == null ) {
            throw new NullPointerException ("Start date cannot be null.");
        }
        if (block.getEndDate() == null) {
            throw new NullPointerException ("End date cannot be null.");
        }
        if (block.getReason() == null || block.getReason().isEmpty()) {
            throw new NullPointerException ("Reason cannot be null or empty.");
        }

        if (block.getEndDate().isBefore(block.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before the start date.");
        }

        if (overlapService.isBlockOverlap(block.getStartDate(), block.getEndDate(), null)
                || overlapService.isBookingOverlap(block.getStartDate(), block.getEndDate(), null)) {
            throw new OverlappingDatesException("The block overlaps with an existing block or booking.");
        }

        return blockRepository.save(block);
    }

    public Block updateBlock(Long id, Block updatedBlock) throws BlockNotFoundException, OverlappingDatesException, NullPointerException, IllegalArgumentException  {
        Block existingBlock = blockRepository.findById(id)
                .orElseThrow(() -> new BlockNotFoundException("Block not found."));

        if (updatedBlock.getStartDate() == null ) {
            throw new NullPointerException ("Start date cannot be null.");
        }
        if (updatedBlock.getEndDate() == null) {
            throw new NullPointerException ("End date cannot be null.");
        }
        if (updatedBlock.getReason() == null || updatedBlock.getReason().isEmpty()) {
            throw new NullPointerException ("Reason data cannot be null or empty.");
        }
        if (updatedBlock.getEndDate().isBefore(updatedBlock.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before the start date.");
        }
        if (overlapService.isBlockOverlap(updatedBlock.getStartDate(), updatedBlock.getEndDate(), id)
                || overlapService.isBookingOverlap(updatedBlock.getStartDate(), updatedBlock.getEndDate(), null)) {
            throw new OverlappingDatesException("The updated block overlaps with an existing block or booking.");
        }

        existingBlock.setStartDate(updatedBlock.getStartDate());
        existingBlock.setEndDate(updatedBlock.getEndDate());
        existingBlock.setReason(updatedBlock.getReason());

        return blockRepository.save(existingBlock);
    }

    public ResponseEntity<String> deleteBlock(Long id) throws BlockNotFoundException {
        if (!blockRepository.existsById(id)) {
            throw new BlockNotFoundException("The block with the specified ID does not exist.");
        }
        blockRepository.deleteById(id);
        return ResponseEntity.ok("The block has been successfully cancelled.");
    }

    public List<Block> getAllBlocks() {
        return blockRepository.findAll();
    }

    public Optional<Block> getBlockById(Long id) {
        return blockRepository.findById(id);
    }

}
