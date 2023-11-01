package com.example.host.service;

import com.example.host.entities.Block;
import com.example.host.entities.Booking;
import com.example.host.repositories.BlockRepository;
import com.example.host.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OverlapValidationService {

    private final BookingRepository bookingRepository;

    private final BlockRepository blockRepository;

    public boolean isBookingOverlap(LocalDate startDate, LocalDate endDate, Long excludeBookingId) {
        List<Booking> overlappingBookings = bookingRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);
        overlappingBookings.removeIf(booking -> booking.getId().equals(excludeBookingId));
        return !overlappingBookings.isEmpty();
    }

    public boolean isBlockOverlap(LocalDate startDate, LocalDate endDate, Long excludeBlockId) {
        List<Block> overlappingBlocks = blockRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);
        overlappingBlocks.removeIf(block -> block.getId().equals(excludeBlockId));
        return !overlappingBlocks.isEmpty();
    }
}
