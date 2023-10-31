package com.example.host.service;

import com.example.host.Exception.OverlappingDatesException;
import com.example.host.entities.Booking;
import com.example.host.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final OverlapValidationService overlapService;

    public Booking createBooking(Booking booking) {
        if (overlapService.isBookingOverlap(booking.getStartDate(), booking.getEndDate())
                || overlapService.isBlockOverlap(booking.getStartDate(), booking.getEndDate())) {
            throw new OverlappingDatesException("\n" + "Reservation overlaps with an existing reservation or block.");
        }
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking updatedBooking) throws Exception {
        Optional<Booking> existingBooking = bookingRepository.findById(id);
        if (existingBooking.isEmpty()) {
            throw new Exception("\n" + "Reservation not found.");
        }

        if (overlapService.isBookingOverlap(updatedBooking.getStartDate(), updatedBooking.getEndDate())
                || overlapService.isBlockOverlap(updatedBooking.getStartDate(), updatedBooking.getEndDate())) {
            throw new Exception("Reservation overlaps with an existing reservation or block.");
        }

        existingBooking.get().setStartDate(updatedBooking.getStartDate());
        existingBooking.get().setEndDate(updatedBooking.getEndDate());
        existingBooking.get().setGuestData(updatedBooking.getGuestData());

        return bookingRepository.save(existingBooking.get());
    }

    public void deleteBooking(Long id) throws Exception {
        if (!bookingRepository.existsById(id)) {
            throw new Exception("Reservation not found.");
        }
        bookingRepository.deleteById(id);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

}
