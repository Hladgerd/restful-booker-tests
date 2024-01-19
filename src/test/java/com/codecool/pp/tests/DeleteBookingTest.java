package com.codecool.pp.tests;

import com.codecool.pp.helpers.JsonHelper;
import com.codecool.pp.requests.DeleteBookingRequest;
import com.codecool.pp.requests.GetBookingRequest;
import com.codecool.pp.requests.PostTokenRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.codecool.pp.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTest {

    private String token;
    private int bookingId;

    @BeforeEach
    void auth() {
        JSONObject authLoad = new JSONObject();
        authLoad.put(USERNAME, JsonHelper.getUsername());
        authLoad.put(PASSWORD, JsonHelper.getPassword());
        Response createTokenResponse = PostTokenRequest.createToken(authLoad.toString());
        token = createTokenResponse.jsonPath().getString(TOKEN);
    }

    @Test
    @DisplayName("Delete existing booking")
    void deleteExistingBookingTest() {

        bookingId = pickValidId();

        Response deleteBookingResponse = DeleteBookingRequest.deleteBooking(bookingId, token);
        assertThat(deleteBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);

        Response getBookingResponse = GetBookingRequest.getBookingById(bookingId);
        assertThat(getBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Delete non existing booking")
    void deleteNonExistingBookingTest() {
        bookingId = pickInvalidId();

        Response deleteBookingResponse = DeleteBookingRequest.deleteBooking(bookingId, token);
        assertThat(deleteBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    private List<Integer> getAllBookingIds() {
        Response allBookingsResponse = GetBookingRequest.getAllBookings();
        JsonPath jsonPath = allBookingsResponse.jsonPath();
        return jsonPath.getList(BOOKING_ID);
    }

    private int pickValidId() {
        List<Integer> bookingIds = getAllBookingIds();
        Collections.shuffle(bookingIds);
        return bookingIds.get(0);
    }

    private int pickInvalidId() {
        List<Integer> bookingIds = getAllBookingIds();
        Collections.sort(bookingIds);
        return bookingIds.get((bookingIds.size()-1)) + 1;
    }
}
