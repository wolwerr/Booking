package com.example.host.entities;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class BlockTest {

    @Test
    public void test_block_creation_with_valid_input() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);
        String reason = "Valid Reason";

        Block block = new Block(1L, startDate, endDate, reason);

        assertEquals(1L, block.getId());
        assertEquals(startDate, block.getStartDate());
        assertEquals(endDate, block.getEndDate());
        assertEquals(reason, block.getReason());
    }

    @Test
    public void test_block_update_with_valid_input() {
        Long id = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);
        String reason = "Valid Reason";

        Block block = new Block(id, startDate, endDate, reason);
        Block updatedBlock = new Block(id, startDate.plusDays(2), endDate.plusDays(3), "Updated Reason");

        block.setStartDate(updatedBlock.getStartDate());
        block.setEndDate(updatedBlock.getEndDate());
        block.setReason(updatedBlock.getReason());

        assertEquals(id, block.getId());
        assertEquals(updatedBlock.getStartDate(), block.getStartDate());
        assertEquals(updatedBlock.getEndDate(), block.getEndDate());
        assertEquals(updatedBlock.getReason(), block.getReason());
    }

    @Test
    public void test_block_deletion() {
        Long id = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);
        String reason = "Valid Reason";

        Block block =  new Block(id, startDate, endDate, reason);

        block = null;

        assertNull(block);
    }

    @Test
    public void test_block_equality() {
        Long id = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);
        String reason = "Valid Reason";

        Block block1 = new Block(id, startDate, endDate, reason);
        Block block2 = new Block(id, startDate, endDate, reason);

        assertNotEquals(block1, block2);
    }

}
