package com.example.host.controller;

import com.example.host.exceptions.BookingNotFoundException;
import com.example.host.exceptions.OverlappingDatesException;
import com.example.host.entities.Booking;
import com.example.host.services.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
class BookingControllerTest {

    private final BookingService bookingService = Mockito.mock(BookingService.class);
    private final BookingController bookingController = new BookingController(bookingService);

    @Test
    void test_getAllBookings_ReturnsBookingsWithStatusCode200_WhenBookingsExist() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(1L, LocalDate.now(), LocalDate.now().plusDays(1), "Guest 1"));
        bookings.add(new Booking(2L, LocalDate.now().plusDays(2), LocalDate.now().plusDays(3), "Guest 2"));
        Mockito.when(bookingService.getAllBookings()).thenReturn(bookings);

        ResponseEntity<Object> response = bookingController.getAllBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookings, response.getBody());
    }

    @Test
    void test_getBookingById_ReturnsBookingWithStatusCode200_WhenBookingExists() {
        Long id = 1L;
        Booking booking = new Booking(id, LocalDate.now(), LocalDate.now().plusDays(1), "Guest 1");
        Mockito.when(bookingService.getBookingById(id)).thenReturn(Optional.of(booking));

        ResponseEntity<Booking> response = bookingController.getBookingById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(booking, response.getBody());
    }

    @Test
    void test_createBooking_CreatesNewBookingAndReturnsItWithStatusCode201_WhenValidBookingData() {
        Booking booking = new Booking(1L, LocalDate.now(), LocalDate.now().plusDays(1), "Guest 1");
        Mockito.when(bookingService.createBooking(booking)).thenReturn(booking);

        ResponseEntity<Object> response = bookingController.createBooking(booking);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(booking, response.getBody());
    }

    @Test
    void test_updateBooking_UpdatesBookingWithStatusCode200_WhenValidBookingData() {
        Long id = 1L;
        Booking updatedBooking = new Booking(id, LocalDate.now().plusDays(2), LocalDate.now().plusDays(3), "Guest 2");
        Mockito.when(bookingService.updateBooking(id, updatedBooking)).thenReturn(updatedBooking);

        ResponseEntity<Object> response = bookingController.updateBooking(id, updatedBooking);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBooking, response.getBody());
    }

    @Test
    void test_getAllBookings_ReturnsStatusCode204_WhenNoBookingsExist() {
        Mockito.when(bookingService.getAllBookings()).thenReturn(Collections.emptyList());

        ResponseEntity<Object> response = bookingController.getAllBookings();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void test_getBookingById_ReturnsStatusCode404_WhenBookingDoesNotExist() {
        Long id = 1L;
        Mockito.when(bookingService.getBookingById(id)).thenReturn(Optional.empty());

        ResponseEntity<Booking> response = bookingController.getBookingById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void test_createBooking_ReturnsStatusCode409_WhenBookingDataOverlaps() {
        Booking booking = new Booking(1L, LocalDate.now(), LocalDate.now().plusDays(1), "Guest 1");
        Mockito.when(bookingService.createBooking(booking)).thenThrow(new OverlappingDatesException("The booking overlaps with an existing booking or block."));

        ResponseEntity<Object> response = bookingController.createBooking(booking);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("The booking overlaps with an existing booking or block.", response.getBody());
    }

    @Test
    void test_updateBooking_ReturnsStatusCode409_WhenBookingDataOverlaps() {
        Long id = 1L;
        Booking updatedBooking = new Booking(id, LocalDate.now().plusDays(2), LocalDate.now().plusDays(3), "Guest 2");
        Mockito.when(bookingService.updateBooking(id, updatedBooking)).thenThrow(new OverlappingDatesException("Reservation overlaps with an existing reservation or block."));

        ResponseEntity<Object> response = bookingController.updateBooking(id, updatedBooking);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Reservation overlaps with an existing reservation or block.", response.getBody());
    }

    @Test
    void test_updateBooking_ReturnsStatusCode404_WhenInvalidID() {
        Long id = 1L;
        Booking updatedBooking = new Booking(id, LocalDate.now().plusDays(2), LocalDate.now().plusDays(3), "Guest 2");
        Mockito.when(bookingService.updateBooking(id, updatedBooking)).thenThrow(new BookingNotFoundException("Reservation not found."));

        ResponseEntity<Object> response = bookingController.updateBooking(id, updatedBooking);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Reservation not found.", response.getBody());
    }

}