package com.codecool.pp.tests.bookings;

import com.codecool.pp.helpers.JsonHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.codecool.pp.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateBookingTest extends BaseBookingTest {

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

    @AfterEach
    void deleteBooking() {
        deleteBookingRequest.deleteBooking(bookingId, token);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/bookingDataValid.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Update booking and verify details")
    public void updateBooking(String firstname, String lastname, int totalPrice, boolean depositPaid, String checkin,
                                 String checkout, String additionalNeeds) {

        bookingId = pickValidId();
        String bookingLoad = createBookingLoad(firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds).toString();

        Response updateBookingResponse = putBookingRequest.updateBooking(bookingLoad, bookingId, token);
        JsonPath updateBookingJsonPath = updateBookingResponse.jsonPath();
        updateBookingResponse.prettyPrint();

        assertThat(updateBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        verifyBookingContainsCorrectData(updateBookingJsonPath, firstname, lastname, totalPrice, depositPaid,
                checkin, checkout, additionalNeeds);

        JsonPath updatedBooking = getBookingById(bookingId);
        verifyBookingContainsCorrectData(updatedBooking, firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds);

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/bookingDataInvalid.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Update booking with invalid data fails")
    public void updateBookingWithInvalidData(String firstname, String lastname, int totalPrice, boolean depositPaid, String checkin,
                                                String checkout, String additionalNeeds) {

        bookingId = pickValidId();
        String bookingLoad = createBookingLoad(firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds).toString();

        Response updateBookingResponse = putBookingRequest.updateBooking(bookingLoad, bookingId, token);

        assertThat(updateBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
