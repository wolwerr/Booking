package com.example.host.controller;

import com.example.host.Exception.BookingNotFoundException;
import com.example.host.Exception.OverlappingDatesException;
import com.example.host.entities.Booking;
import com.example.host.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @ExceptionHandler(OverlappingDatesException.class)
    public ResponseEntity<String> handleOverlappingDatesException(OverlappingDatesException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
    }

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody Booking booking) {
        try {
            Booking newBooking = bookingService.createBooking(booking);
            return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
        } catch (OverlappingDatesException ode) {
            return new ResponseEntity<>(ode.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Server error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable("id") Long id) {
        Optional<Booking> bookingData = bookingService.getBookingById(id);
        return bookingData.map(booking -> new ResponseEntity<>(booking, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBooking(@PathVariable("id") Long id, @RequestBody Booking booking) {
        try {
            Booking updatedBooking = bookingService.updateBooking(id, booking);
            return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
        } catch (BookingNotFoundException bnfe) {
            return new ResponseEntity<>(bnfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (OverlappingDatesException ode) {
            return new ResponseEntity<>(ode.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBooking(@PathVariable("id") Long id) {
        try {
            bookingService.deleteBooking(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BookingNotFoundException bnfe) {
            return new ResponseEntity<>(bnfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
