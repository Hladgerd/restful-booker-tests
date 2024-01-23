package com.codecool.pp.tests.bookings.authenticated;

import com.codecool.pp.helpers.JsonHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateBookingTest extends AuthenticatedBookingTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/bookingDataValid.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Create new booking entry and verify details")
    public void createNewBooking(String firstname, String lastname, int totalPrice, boolean depositPaid, String checkin,
                                 String checkout, String additionalNeeds) {

        String bookingLoad = JsonHelper.createBookingLoad(firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds).toString();

        Response createBookingResponse = postBookingRequest.createBooking(bookingLoad);
        JsonPath createBookingJsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(createBookingJsonPath);

        assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        verifyResponseContainsCorrectData(createBookingJsonPath, firstname, lastname, totalPrice, depositPaid,
                checkin, checkout, additionalNeeds);

        JsonPath newBooking = getBookingById(bookingId);
        verifyBookingContainsCorrectData(newBooking, firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/bookingDataInvalid.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Create new booking entry with invalid data fails")
    public void createNewBookingWithInvalidData(String firstname, String lastname, int totalPrice, boolean depositPaid, String checkin,
                                 String checkout, String additionalNeeds) {

        String bookingLoad = JsonHelper.createBookingLoad(firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds).toString();

        Response createBookingResponse = postBookingRequest.createBooking(bookingLoad);

        assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
