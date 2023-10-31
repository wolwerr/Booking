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

    public boolean isBookingOverlap(LocalDate startDate, LocalDate endDate) {
        List<Booking> overlappingBookings = bookingRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);
        return !overlappingBookings.isEmpty();
    }

    public boolean isBlockOverlap(LocalDate startDate, LocalDate endDate) {
        List<Block> overlappingBlocks = blockRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);
        return !overlappingBlocks.isEmpty();
    }
}
