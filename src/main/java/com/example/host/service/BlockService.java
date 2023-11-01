package com.example.host.service;

import com.example.host.Exception.BlockNotFoundException;
import com.example.host.Exception.OverlappingDatesException;
import com.example.host.entities.Block;
import com.example.host.repositories.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BlockService {

    private final BlockRepository blockRepository;

    private final OverlapValidationService overlapService;

    public Block createBlock(Block block) throws OverlappingDatesException {
        if (overlapService.isBlockOverlap(block.getStartDate(), block.getEndDate(), null)
                || overlapService.isBookingOverlap(block.getStartDate(), block.getEndDate(), null)) {
            throw new OverlappingDatesException("The block overlaps with an existing block or reservation.");
        }
        return blockRepository.save(block);
    }

    public Block updateBlock(Long id, Block updatedBlock) throws BlockNotFoundException, OverlappingDatesException {
        Block existingBlock = blockRepository.findById(id)
                .orElseThrow(() -> new BlockNotFoundException("Block not found."));
        if (overlapService.isBlockOverlap(updatedBlock.getStartDate(), updatedBlock.getEndDate(), id)
                || overlapService.isBookingOverlap(updatedBlock.getStartDate(), updatedBlock.getEndDate(), null)) {
            throw new OverlappingDatesException("The updated block overlaps with an existing block or booking.");
        }

        existingBlock.setStartDate(updatedBlock.getStartDate());
        existingBlock.setEndDate(updatedBlock.getEndDate());
        existingBlock.setReason(updatedBlock.getReason());

        return blockRepository.save(existingBlock);
    }

    public void deleteBlock(Long id) throws BlockNotFoundException {
        if (!blockRepository.existsById(id)) {
            throw new BlockNotFoundException("The block with the specified ID does not exist.");
        }
        blockRepository.deleteById(id);
    }

    public List<Block> getAllBlocks() {
        return blockRepository.findAll();
    }

    public Optional<Block> getBlockById(Long id) {
        return blockRepository.findById(id);
    }

}
