package com.codecool.pp.tests.bookings;

import com.codecool.pp.helpers.JsonHelper;
import com.codecool.pp.requests.auth.PostTokenRequest;
import com.codecool.pp.requests.bookings.DeleteBookingRequest;
import com.codecool.pp.requests.bookings.GetBookingRequest;
import com.codecool.pp.requests.bookings.PutBookingRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Collections;
import java.util.List;

import static com.codecool.pp.helpers.JsonHelper.*;
import static com.codecool.pp.helpers.JsonHelper.ADDITIONAL_NEEDS;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateBookingTest {

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

    @AfterEach
    void deleteBooking() {
        DeleteBookingRequest.deleteBooking(bookingId, token);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/bookingDataValid.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Update booking and verify details")
    public void updateBooking(String firstname, String lastname, int totalPrice, boolean depositPaid, String checkin,
                                 String checkout, String additionalNeeds) {

        bookingId = pickValidId();
        String bookingLoad = createBookingLoad(firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds).toString();

        Response updateBookingResponse = PutBookingRequest.updateBooking(bookingLoad, bookingId, token);
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

        Response updateBookingResponse = PutBookingRequest.updateBooking(bookingLoad, bookingId, token);

        assertThat(updateBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    private List<Integer> getAllBookingIds() {
        Response allBookingsResponse = GetBookingRequest.getAllBookings();
        JsonPath jsonPath = allBookingsResponse.jsonPath();
        return jsonPath.getList(BOOKING_ID);
    }

    private JsonPath getBookingById(int bookingId) {
        Response getBookingResponse = GetBookingRequest.getBookingById(bookingId);
        return getBookingResponse.jsonPath();
    }

    private int pickValidId() {
        List<Integer> bookingIds = getAllBookingIds();
        Collections.shuffle(bookingIds);
        return bookingIds.get(0);
    }

    private void verifyBookingContainsCorrectData(JsonPath jsonPath, String firstname, String lastname,
                                                  int totalPrice, boolean depositPaid, String checkin,
                                                  String checkout, String additionalNeeds) {

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(jsonPath.getString(FIRSTNAME)).isEqualTo(firstname);
        softAssertions.assertThat(jsonPath.getString(LASTNAME)).isEqualTo(lastname);
        softAssertions.assertThat(jsonPath.getInt(TOTAL_PRICE)).isEqualTo(totalPrice);
        softAssertions.assertThat(jsonPath.getBoolean(DEPOSIT_PAID)).isEqualTo(depositPaid);
        softAssertions.assertThat(jsonPath.getString(BOOKING_DATES + "." + CHECKIN)).isEqualTo(checkin);
        softAssertions.assertThat(jsonPath.getString(BOOKING_DATES + "." + CHECKOUT)).isEqualTo(checkout);
        softAssertions.assertThat(jsonPath.getString(ADDITIONAL_NEEDS)).isEqualTo(additionalNeeds);

        softAssertions.assertAll();
    }
}
