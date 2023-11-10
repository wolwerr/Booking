package com.example.host.controller;

import com.example.host.dto.RebookingRequest;
import com.example.host.exceptions.BookingNotFoundException;
import com.example.host.exceptions.OverlappingDatesException;
import com.example.host.entities.Booking;
import com.example.host.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    String error = "An error occurred.";

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
        } catch (IllegalArgumentException | NullPointerException iae) {
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
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
        } catch (IllegalArgumentException | NullPointerException iae) {
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        try {
            return bookingService.deleteBooking(id);
        } catch (BookingNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        try {
            return bookingService.cancelBooking(id);
        } catch (BookingNotFoundException bnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bnfe.getMessage());
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PatchMapping("/{id}/rebook")
    public ResponseEntity<String> rebookBooking(@PathVariable Long id, @RequestBody RebookingRequest rebookingRequest) {
        try {
            return bookingService.rebookBooking(id, rebookingRequest.getNewStartDate(), rebookingRequest.getNewEndDate());
        } catch (BookingNotFoundException bnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bnfe.getMessage());
        } catch (OverlappingDatesException ode) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ode.getMessage());
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
