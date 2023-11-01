package com.example.host.controller;

import com.example.host.Exception.BlockNotFoundException;
import com.example.host.Exception.OverlappingDatesException;
import com.example.host.entities.Block;
import com.example.host.service.BlockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @ControllerAdvice
    public static class GlobalExceptionHandler {
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return errors;
        }
    }

    @PostMapping
    public ResponseEntity<Object> createBlock(@Valid @RequestBody Block block) {
        try {
            Block newBlock = blockService.createBlock(block);
            return new ResponseEntity<>(newBlock, HttpStatus.CREATED);
        } catch (OverlappingDatesException ode) {
            return new ResponseEntity<>(ode.getMessage(), HttpStatus.CONFLICT);
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
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBlock(@PathVariable("id") Long id) {
        try {
            blockService.deleteBlock(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BlockNotFoundException bnfe) {
            return new ResponseEntity<>(bnfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
