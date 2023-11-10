package com.example.host.entities;


import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BookingTest {


    @Test
    void test_createBookingWithValidData() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);
        String guestData = "Guest 1";

        Booking booking = new Booking(1L, startDate, endDate, guestData, BookingStatus.ACTIVE);

        assertEquals(1L, booking.getId());
        assertEquals(startDate, booking.getStartDate());
        assertEquals(endDate, booking.getEndDate());
        assertEquals(guestData, booking.getGuestData());
    }

    @Test
    void test_updateBookingWithValidData() {
        Long id = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);
        String guestData = "Guest 1";

        Booking booking = new Booking(1L, startDate, endDate, guestData, BookingStatus.ACTIVE);
        Booking updatedBooking = new Booking(id, startDate.plusDays(2), endDate.plusDays(3), "Updated Guest", BookingStatus.ACTIVE);

        booking.setStartDate(updatedBooking.getStartDate());
        booking.setEndDate(updatedBooking.getEndDate());
        booking.setGuestData(updatedBooking.getGuestData());

        assertEquals(id, booking.getId());
        assertEquals(updatedBooking.getStartDate(), booking.getStartDate());
        assertEquals(updatedBooking.getEndDate(), booking.getEndDate());
        assertEquals(updatedBooking.getGuestData(), booking.getGuestData());
    }

    @Test
    void test_getStartDate() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);
        String guestData = "Guest 1";

        Booking booking = new Booking(1L, startDate, endDate, guestData, BookingStatus.ACTIVE);

        assertEquals(startDate, booking.getStartDate());
    }

    @Test
    void test_getEndDate() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);
        String guestData = "Guest 1";

        Booking booking = new Booking(1L, startDate, endDate, guestData, BookingStatus.ACTIVE);

        assertEquals(endDate, booking.getEndDate());
    }

    @Test
    void test_getGuestData() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);
        String guestData = "Guest 1";

        Booking booking = new Booking(1L, startDate, endDate, guestData, BookingStatus.ACTIVE);

        assertEquals(guestData, booking.getGuestData());
    }

    @Test
    void test_createBookingWithMinimumDates() {
        LocalDate startDate = LocalDate.MIN;
        LocalDate endDate = LocalDate.MIN;
        String guestData = "Guest 1";

        Booking booking = new Booking(1L, startDate, endDate, guestData, BookingStatus.ACTIVE);

        assertEquals(startDate, booking.getStartDate());
        assertEquals(endDate, booking.getEndDate());
    }

    @Test
    void test_createBookingWithMaximumDates() {
        LocalDate startDate = LocalDate.MAX;
        LocalDate endDate = LocalDate.MAX;
        String guestData = "Guest 1";

        Booking booking = new Booking(1L, startDate, endDate, guestData, BookingStatus.ACTIVE);

        assertEquals(startDate, booking.getStartDate());
        assertEquals(endDate, booking.getEndDate());
    }

}