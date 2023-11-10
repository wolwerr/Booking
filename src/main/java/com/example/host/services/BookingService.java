package com.example.host.services;

import com.example.host.exceptions.BookingNotFoundException;
import com.example.host.exceptions.OverlappingDatesException;
import com.example.host.entities.Booking;
import com.example.host.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final OverlapValidationService overlapService;

    public Booking createBooking(Booking booking) throws OverlappingDatesException, NullPointerException, IllegalArgumentException  {

        getStartDate(booking);
        if (overlapService.isBookingOverlap(booking.getStartDate(), booking.getEndDate(), null)
                || overlapService.isBlockOverlap(booking.getStartDate(), booking.getEndDate(), null)) {
            throw new OverlappingDatesException("The booking overlaps with an existing booking or block.");
        }
        return bookingRepository.save(booking);
    }

    private void getStartDate(Booking booking) {
        if (booking.getStartDate() == null ) {
            throw new NullPointerException ("Start date cannot be null.");
        }
        if (booking.getEndDate() == null) {
            throw new NullPointerException ("End date cannot be null.");
        }
        if (booking.getGuestData() == null || booking.getGuestData().isEmpty()) {
            throw new NullPointerException ("Guest data cannot be null or empty.");
        }
        if (booking.getEndDate().isBefore(booking.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before the start date.");
        }
    }

    public Booking updateBooking(Long id, Booking updatedBooking) throws BookingNotFoundException, OverlappingDatesException, NullPointerException, IllegalArgumentException  {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Reservation not found."));

        getStartDate(updatedBooking);
        if (overlapService.isBookingOverlap(updatedBooking.getStartDate(), updatedBooking.getEndDate(), id)
                || overlapService.isBlockOverlap(updatedBooking.getStartDate(), updatedBooking.getEndDate(), null)) {
            throw new OverlappingDatesException("Reservation overlaps with an existing reservation or block.");
        }
        existingBooking.setStartDate(updatedBooking.getStartDate());
        existingBooking.setEndDate(updatedBooking.getEndDate());
        existingBooking.setGuestData(updatedBooking.getGuestData());

        return bookingRepository.save(existingBooking);
    }

    public ResponseEntity<String> deleteBooking(Long id) throws BookingNotFoundException {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException("The reservation with the specified ID does not exist.");
        }
        bookingRepository.deleteById(id);
        return ResponseEntity.ok("The reservation has been successfully cancelled.");
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

}
