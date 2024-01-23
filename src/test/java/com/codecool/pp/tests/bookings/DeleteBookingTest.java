package com.codecool.pp.tests.bookings;

import com.codecool.pp.helpers.JsonHelper;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codecool.pp.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTest extends BaseBookingTest {

    private String token;
    private int bookingId;

    @BeforeEach
    void auth() {
        JSONObject authLoad = new JSONObject();
        authLoad.put(USERNAME, JsonHelper.getUsername());
        authLoad.put(PASSWORD, JsonHelper.getPassword());
        Response createTokenResponse = postTokenRequest.createToken(authLoad.toString());
        token = createTokenResponse.jsonPath().getString(TOKEN);
    }

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
