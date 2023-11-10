package com.example.host.EndToEndBookingTest;

import com.example.host.entities.Booking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingLifecycleEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateAndGetBooking() throws Exception {
        // Create the URI for POST request to create a new Booking
        final String baseUrl = "http://localhost:" + port + "/api/v1/bookings";
        URI uri = new URI(baseUrl);

        // Create a new Booking object
        Map<String, Object> booking = new HashMap<>();
        booking.put("startDate", LocalDate.now().plusDays(100).toString());
        booking.put("endDate", LocalDate.now().plusDays(105).toString());
        booking.put("guestData", "E2E Test Guest");

        // Send the POST request and receive a response entity
        ResponseEntity<Booking> result = restTemplate.postForEntity(uri, booking, Booking.class);

        // Verify if the booking was created successfully
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Booking createdBooking = result.getBody();
        assertThat(createdBooking).isNotNull();
        assertThat(createdBooking.getGuestData()).isEqualTo("E2E Test Guest");

        // Retrieve the created Booking using a GET request
        Booking retrievedBooking = restTemplate.getForObject(baseUrl + "/" + createdBooking.getId(), Booking.class);

        // Verify if the retrieved Booking is the same as the created one
        assertThat(retrievedBooking).isNotNull();
        assertThat(retrievedBooking.getId()).isEqualTo(createdBooking.getId());
        assertThat(retrievedBooking.getGuestData()).isEqualTo(createdBooking.getGuestData());
    }

    @Test
    void testBookingLifecycle() throws Exception {
        // Create URI
        final String baseUrl = "http://localhost:" + port + "/api/v1/bookings";
        URI uri = new URI(baseUrl);

        // Create a Booking
        Map<String, Object> booking = new HashMap<>();
        booking.put("startDate", LocalDate.now().plusDays(30).toString());
        booking.put("endDate", LocalDate.now().plusDays(40).toString());
        booking.put("guestData", "Teste E2E");

        ResponseEntity<Booking> createResponse = restTemplate.postForEntity(uri, booking, Booking.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Booking createdBooking = createResponse.getBody();
        assert createdBooking != null;
        assertThat(createdBooking.getGuestData()).isEqualTo("Teste E2E");

        // Retrieve the Booking list
        ResponseEntity<List> listResponse = restTemplate.getForEntity(uri, List.class);
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).hasSize(6);

        // Update the Booking
        createdBooking.setGuestData("Jane Doe Updated");
        restTemplate.put(uri + "/" + createdBooking.getId(), createdBooking);
        Booking updatedBooking = restTemplate.getForObject(uri + "/" + createdBooking.getId(), Booking.class);
        assertThat(updatedBooking.getGuestData()).isEqualTo("Jane Doe Updated");

        // Delete the Booking
        restTemplate.delete(uri + "/" + createdBooking.getId());
        ResponseEntity<Booking> getResponse = restTemplate.getForEntity(uri + "/" + createdBooking.getId(), Booking.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

