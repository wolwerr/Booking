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

import static com.example.host.entities.BookingStatus.CANCELLED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository bookingRepository;

    String url = "/api/v1/blocks";

    @Test
    void shouldReturnAllBookings() throws Exception {
        this.mockMvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateBooking() throws Exception {
        String jsonRequest = "{\"startDate\":\"2020-01-01\",\"endDate\":\"2020-01-10\",\"guestData\":\"John Doe\",\"status\":\"ACTIVE\"}";

        this.mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldDeleteBooking() throws Exception {
        this.mockMvc.perform(delete("/api/v1/bookings/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotAllowStartDateAfterEndDate() throws Exception {
        String jsonRequest = "{\"startDate\":\"2022-01-10\",\"endDate\":\"2022-01-05\",\"guestData\":\"John Doe\",\"status\":\"ACTIVE\"}";

        this.mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void shouldNotAllowOverlappingDatesForBooking() throws Exception {
        Booking existingBooking = new Booking();
        existingBooking.setStartDate(LocalDate.of(2022, 1, 1));
        existingBooking.setEndDate(LocalDate.of(2022, 1, 10));
        existingBooking.setGuestData("Existing Guest");
        existingBooking.setStatus(CANCELLED);
        bookingRepository.save(existingBooking);

        String jsonRequest = "{\"startDate\":\"2022-01-05\",\"endDate\":\"2022-01-15\",\"guestData\":\"John Doe\",\"status\":\"ACTIVE\"}";

        this.mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict());

    }


}
