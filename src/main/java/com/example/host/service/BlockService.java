package com.example.host.service;

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

    public Block createBlock(Block block) throws Exception {
        if (overlapService.isBlockOverlap(block.getStartDate(), block.getEndDate())
                || overlapService.isBookingOverlap(block.getStartDate(), block.getEndDate())) {
            throw new Exception("The block overlaps with an existing block or reservation.");
        }
        return blockRepository.save(block);
    }

    public Block updateBlock(Long id, Block updatedBlock) throws Exception {
        if (!blockRepository.existsById(id)) {
            throw new Exception("The block with the specified ID does not exist.");
        }

        if (overlapService.isBlockOverlap(updatedBlock.getStartDate(), updatedBlock.getEndDate())
                || overlapService.isBookingOverlap(updatedBlock.getStartDate(), updatedBlock.getEndDate())) {
            throw new Exception("The updated block overwrites an existing block or reservation.");
        }

        updatedBlock.setId(id);
        return blockRepository.save(updatedBlock);
    }

    public void deleteBlock(Long id) throws Exception {
        if (!blockRepository.existsById(id)) {
            throw new Exception("The block with the specified ID does not exist.");
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
