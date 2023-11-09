package com.example.host.controller;

import com.example.host.entities.Booking;
import com.example.host.repositories.BookingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void shouldReturnAllBookings() throws Exception {
        this.mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateBooking() throws Exception {
        String jsonRequest = "{\"startDate\":\"2020-01-01\",\"endDate\":\"2020-01-10\",\"guestData\":\"John Doe\"}";

        this.mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldDeleteBooking() throws Exception {
        this.mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotAllowStartDateAfterEndDate() throws Exception {
        String jsonRequest = "{\"startDate\":\"2022-01-10\",\"endDate\":\"2022-01-05\",\"guestData\":\"John Doe\"}";

        this.mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void shouldNotAllowOverlappingDates() throws Exception {
        Booking existingBooking = new Booking();
        existingBooking.setStartDate(LocalDate.of(2022, 1, 1));
        existingBooking.setEndDate(LocalDate.of(2022, 1, 10));
        existingBooking.setGuestData("Existing Guest");
        bookingRepository.save(existingBooking);

        String jsonRequest = "{\"startDate\":\"2022-01-05\",\"endDate\":\"2022-01-15\",\"guestData\":\"John Doe\"}";

        this.mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict());

    }


}
