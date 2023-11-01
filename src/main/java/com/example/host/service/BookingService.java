package com.example.host.service;

import com.example.host.Exception.BookingNotFoundException;
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

    public Booking createBooking(Booking booking) throws OverlappingDatesException, NullPointerException, IllegalArgumentException  {

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
        if (overlapService.isBookingOverlap(booking.getStartDate(), booking.getEndDate(), null)
                || overlapService.isBlockOverlap(booking.getStartDate(), booking.getEndDate(), null)) {
            throw new OverlappingDatesException("The booking overlaps with an existing booking or block.");
        }
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking updatedBooking) throws BookingNotFoundException, OverlappingDatesException, NullPointerException, IllegalArgumentException  {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Reservation not found."));

        if (updatedBooking.getStartDate() == null ) {
            throw new NullPointerException ("Start date cannot be null.");
        }
        if (updatedBooking.getEndDate() == null) {
            throw new NullPointerException ("End date cannot be null.");
        }
        if (updatedBooking.getGuestData() == null || updatedBooking.getGuestData().isEmpty()) {
            throw new NullPointerException ("Guest data cannot be null or empty.");
        }
        if (updatedBooking.getEndDate().isBefore(updatedBooking.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before the start date.");
        }
        if (overlapService.isBookingOverlap(updatedBooking.getStartDate(), updatedBooking.getEndDate(), id)
                || overlapService.isBlockOverlap(updatedBooking.getStartDate(), updatedBooking.getEndDate(), null)) {
            throw new OverlappingDatesException("Reservation overlaps with an existing reservation or block.");
        }
        existingBooking.setStartDate(updatedBooking.getStartDate());
        existingBooking.setEndDate(updatedBooking.getEndDate());
        existingBooking.setGuestData(updatedBooking.getGuestData());

        return bookingRepository.save(existingBooking);
    }

    public void deleteBooking(Long id) throws BookingNotFoundException {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException("The reservation with the specified ID does not exist.");
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
