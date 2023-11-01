package com.example.host.serviceTest;

import com.example.host.entities.Booking;
import com.example.host.repositories.BookingRepository;
import com.example.host.service.BookingService;
import com.example.host.service.OverlapValidationService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @Test
    public void test_createBookingWithValidData() {
        BookingRepository bookingRepository = mock(BookingRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BookingService bookingService = new BookingService(bookingRepository, overlapService);

        Booking booking = new Booking();
        booking.setStartDate(LocalDate.of(2022, 1, 1));
        booking.setEndDate(LocalDate.of(2022, 1, 5));
        booking.setGuestData("John Doe");

        when(overlapService.isBookingOverlap(any(LocalDate.class), any(LocalDate.class), any(Long.class))).thenReturn(false);
        when(overlapService.isBlockOverlap(any(LocalDate.class), any(LocalDate.class), any(Long.class))).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.createBooking(booking);

        assertEquals(booking, result);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    public void test_updateBookingWithValidData() {
        Long bookingId = 1L;
        Booking existingBooking = new Booking();
        existingBooking.setId(bookingId);
        existingBooking.setStartDate(LocalDate.of(2022, 1, 1));
        existingBooking.setEndDate(LocalDate.of(2022, 1, 5));
        existingBooking.setGuestData("John Doe");

        Booking updatedBooking = new Booking();
        updatedBooking.setStartDate(LocalDate.of(2022, 2, 1));
        updatedBooking.setEndDate(LocalDate.of(2022, 2, 5));
        updatedBooking.setGuestData("Jane Smith");

        BookingRepository bookingRepository = mock(BookingRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BookingService bookingService = new BookingService(bookingRepository, overlapService);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(overlapService.isBookingOverlap(any(LocalDate.class), any(LocalDate.class), any(Long.class))).thenReturn(false);
        when(overlapService.isBlockOverlap(any(LocalDate.class), any(LocalDate.class), any(Long.class))).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenReturn(updatedBooking);

        Booking result = bookingService.updateBooking(bookingId, updatedBooking);

        assertEquals(updatedBooking, result);
        verify(bookingRepository, times(1)).save(existingBooking);
    }

    @Test
    public void test_deleteBookingById() {
        Long bookingId = 1L;
        BookingRepository bookingRepository = mock(BookingRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BookingService bookingService = new BookingService(bookingRepository, overlapService);

        when(bookingRepository.existsById(bookingId)).thenReturn(true);

        bookingService.deleteBooking(bookingId);

        verify(bookingRepository, times(1)).deleteById(bookingId);
    }

    @Test
    public void test_getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        bookings.add(new Booking());

        BookingRepository bookingRepository = mock(BookingRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BookingService bookingService = new BookingService(bookingRepository, overlapService);

        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> result = bookingService.getAllBookings();

        assertEquals(bookings, result);
    }

    @Test
    public void test_createBookingWithNullStartDate() {
        Booking booking = new Booking();
        booking.setEndDate(LocalDate.of(2022, 1, 5));
        booking.setGuestData("John Doe");

        BookingRepository bookingRepository = mock(BookingRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BookingService bookingService = new BookingService(bookingRepository, overlapService);

        assertThrows(NullPointerException.class, () -> bookingService.createBooking(booking));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void test_createBookingWithNullEndDate() {
        Booking booking = new Booking();
        booking.setStartDate(LocalDate.of(2022, 1, 1));
        booking.setGuestData("John Doe");

        BookingRepository bookingRepository = mock(BookingRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BookingService bookingService = new BookingService(bookingRepository, overlapService);

        assertThrows(NullPointerException.class, () -> bookingService.createBooking(booking));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void test_createBookingWithNullOrEmptyGuestData() {
        Booking booking = new Booking();
        booking.setStartDate(LocalDate.of(2022, 1, 1));
        booking.setEndDate(LocalDate.of(2022, 1, 5));

        BookingRepository bookingRepository = mock(BookingRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BookingService bookingService = new BookingService(bookingRepository, overlapService);

        assertThrows(NullPointerException.class, () -> bookingService.createBooking(booking));
        verify(bookingRepository, never()).save(any(Booking.class));

        booking.setGuestData("");
        assertThrows(NullPointerException.class, () -> bookingService.createBooking(booking));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void test_createBookingWithEndDateBeforeStartDate() {
        Booking booking = new Booking();
        booking.setStartDate(LocalDate.of(2022, 1, 5));
        booking.setEndDate(LocalDate.of(2022, 1, 1));
        booking.setGuestData("John Doe");

        BookingRepository bookingRepository = mock(BookingRepository.class);
        OverlapValidationService overlapService = mock(OverlapValidationService.class);
        BookingService bookingService = new BookingService(bookingRepository, overlapService);

        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(booking));
        verify(bookingRepository, never()).save(any(Booking.class));
    }


}
