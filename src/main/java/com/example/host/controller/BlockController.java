package com.example.host.controller;

import com.example.host.exceptions.BlockNotFoundException;
import com.example.host.exceptions.OverlappingDatesException;
import com.example.host.entities.Block;
import com.example.host.services.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blocks")
public class BlockController {

    private final BlockService blockService;

    @ExceptionHandler(OverlappingDatesException.class)
    public ResponseEntity<String> handleOverlappingDatesException(OverlappingDatesException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
    }

    @PostMapping
    public ResponseEntity<Object> createBlock(@RequestBody Block block) {
        try {
            Block newBlock = blockService.createBlock(block);
            return new ResponseEntity<>(newBlock, HttpStatus.CREATED);
        } catch (OverlappingDatesException ode) {
            return new ResponseEntity<>(ode.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalArgumentException | NullPointerException iae) {
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Server error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllBlocks() {
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
    public ResponseEntity<Object> updateBlock(@PathVariable("id") Long id, @RequestBody Block block) {
        try {
            Block updatedBlock = blockService.updateBlock(id, block);
            return new ResponseEntity<>(updatedBlock, HttpStatus.OK);
        } catch (BlockNotFoundException bnfe) {
            return new ResponseEntity<>(bnfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (OverlappingDatesException ode) {
            return new ResponseEntity<>(ode.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalArgumentException | NullPointerException iae) {
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlock(@PathVariable("id") Long id) {
        try {
            return blockService.deleteBlock(id);
        } catch (BlockNotFoundException bnfe) {
            return new ResponseEntity<>(bnfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
