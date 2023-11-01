package com.example.host.serviceTest;

import com.example.host.entities.Block;
import com.example.host.entities.Booking;
import com.example.host.repositories.BlockRepository;
import com.example.host.repositories.BookingRepository;
import com.example.host.service.OverlapValidationService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OverlapValidationServiceTest {

    @Test
    public void test_is_booking_overlap_returns_false_when_no_overlapping_bookings() {
        BookingRepository bookingRepository = mock(BookingRepository.class);
        BlockRepository blockRepository = mock(BlockRepository.class);
        OverlapValidationService overlapValidationService = new OverlapValidationService(bookingRepository, blockRepository);

        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 5);
        Long excludeBookingId = null;

        when(bookingRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate)).thenReturn(Collections.emptyList());

        boolean result = overlapValidationService.isBookingOverlap(startDate, endDate, excludeBookingId);

        assertFalse(result);
        verify(bookingRepository, times(1)).findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);
    }

    @Test
    public void test_is_block_overlap_returns_false_when_no_overlapping_blocks() {
        BookingRepository bookingRepository = mock(BookingRepository.class);
        BlockRepository blockRepository = mock(BlockRepository.class);
        OverlapValidationService overlapValidationService = new OverlapValidationService(bookingRepository, blockRepository);

        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 5);
        Long excludeBlockId = null;

        when(blockRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate)).thenReturn(Collections.emptyList());

        boolean result = overlapValidationService.isBlockOverlap(startDate, endDate, excludeBlockId);

        assertFalse(result);
        verify(blockRepository, times(1)).findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);
    }

    @Test
    public void test_is_booking_overlap_returns_true_when_overlapping_booking_exists() {
        BookingRepository bookingRepository = mock(BookingRepository.class);
        BlockRepository blockRepository = mock(BlockRepository.class);
        OverlapValidationService overlapValidationService = new OverlapValidationService(bookingRepository, blockRepository);

        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 5);
        Long excludeBookingId = null;

        List<Booking> overlappingBookings = new ArrayList<>();
        overlappingBookings.add(new Booking(1L, LocalDate.of(2022, 1, 2), LocalDate.of(2022, 1, 4), "Guest Data"));

        when(bookingRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate)).thenReturn(overlappingBookings);

        boolean result = overlapValidationService.isBookingOverlap(startDate, endDate, excludeBookingId);

        assertTrue(result);
        verify(bookingRepository, times(1)).findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);
    }

    @Test
    public void test_is_block_overlap_returns_true_when_overlapping_block_exists() {
        BookingRepository bookingRepository = mock(BookingRepository.class);
        BlockRepository blockRepository = mock(BlockRepository.class);
        OverlapValidationService overlapValidationService = new OverlapValidationService(bookingRepository, blockRepository);

        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 5);
        Long excludeBlockId = null;

        List<Block> overlappingBlocks = new ArrayList<>();
        overlappingBlocks.add(new Block(1L, LocalDate.of(2022, 1, 2), LocalDate.of(2022, 1, 4), "Reason"));

        when(blockRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate)).thenReturn(overlappingBlocks);

        boolean result = overlapValidationService.isBlockOverlap(startDate, endDate, excludeBlockId);

        assertTrue(result);
        verify(blockRepository, times(1)).findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);
    }

}
