package com.example.host.controller;

import com.example.host.Exception.OverlappingDatesException;
import com.example.host.entities.Block;
import com.example.host.service.BlockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blocks")
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @ExceptionHandler(OverlappingDatesException.class)
    public ResponseEntity<String> handleOverlappingDatesException(OverlappingDatesException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
    }

    @PostMapping
    public ResponseEntity<Block> createBlock(@RequestBody Block block) {
        try {
            Block newBlock = blockService.createBlock(block);
            return new ResponseEntity<>(newBlock, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Block>> getAllBlocks() {
        List<Block> blocks = blockService.getAllBlocks();
        if (blocks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(blocks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Block> getBlockById(@PathVariable("id") Long id) {
        Optional<Block> blockData = blockService.getBlockById(id);

        return blockData.map(block -> new ResponseEntity<>(block, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Block> updateBlock(@PathVariable("id") Long id, @RequestBody Block block) {
        try {
            Block updatedBlock = blockService.updateBlock(id, block);
            return new ResponseEntity<>(updatedBlock, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBlock(@PathVariable("id") Long id) {
        try {
            blockService.deleteBlock(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
