package com.codecool.pp.tests.bookings.authenticated;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.codecool.pp.helpers.JsonHelper.createBookingLoad;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateBookingTest extends AuthenticatedBookingTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/bookingDataValid.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Update booking and verify details")
    public void updateBookingTest(String firstname, String lastname, int totalPrice, boolean depositPaid, String checkin,
                                 String checkout, String additionalNeeds) {

        bookingId = pickValidId();
        String bookingLoad = createBookingLoad(firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds).toString();

        Response updateBookingResponse = putBookingRequest.updateBooking(bookingLoad, bookingId, token);

        assertThat(updateBookingResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        verifyBookingContainsCorrectData(updateBookingResponse, firstname, lastname, totalPrice, depositPaid,
                checkin, checkout, additionalNeeds);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/bookingDataValid.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Update booking and verify it was saved properly")
    public void updateBookingAndSaveTest(String firstname, String lastname, int totalPrice, boolean depositPaid, String checkin,
                              String checkout, String additionalNeeds) {

        bookingId = pickValidId();
        String bookingLoad = createBookingLoad(firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds).toString();

        putBookingRequest.updateBooking(bookingLoad, bookingId, token);

        Response updatedBooking = getBookingById(bookingId);
        verifyBookingContainsCorrectData(updatedBooking, firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds);

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/bookingDataInvalid.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Update booking with invalid data fails")
    public void updateBookingWithInvalidDataTest(String firstname, String lastname, int totalPrice, boolean depositPaid, String checkin,
                                                String checkout, String additionalNeeds) {

        bookingId = pickValidId();
        String bookingLoad = createBookingLoad(firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds).toString();

        Response updatedBookingResponse = putBookingRequest.updateBooking(bookingLoad, bookingId, token);

        assertThat(updatedBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
