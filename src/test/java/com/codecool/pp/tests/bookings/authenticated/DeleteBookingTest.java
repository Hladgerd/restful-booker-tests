package com.codecool.pp.tests.bookings.authenticated;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTest extends AuthenticatedBookingTest {

    @Test
    @DisplayName("Delete existing booking")
    void deleteExistingBookingTest() {

        bookingId = pickValidId();

        Response deleteBookingResponse = deleteBookingRequest.deleteBooking(bookingId, token);
        assertThat(deleteBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);

        Response getBookingResponse = getBookingRequest.getBookingById(bookingId);
        assertThat(getBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Delete non existing booking")
    void deleteNonExistingBookingTest() {
        bookingId = pickInvalidId();

        Response deleteBookingResponse = deleteBookingRequest.deleteBooking(bookingId, token);
        assertThat(deleteBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }
}
