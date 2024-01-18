package com.codecool.pp.tests;

import com.codecool.pp.requests.PostBookingRequest;
import com.codecool.pp.requests.GetBookingRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.codecool.pp.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateBookingTest {

    private int bookingId;

    @ParameterizedTest
    @CsvFileSource(resources = "/bookingData.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Create new booking entry and verify details")
    public void createNewBooking(String firstname, String lastname, int totalPrice, boolean depositPaid, String checkin,
                                 String checkout, String additionalNeeds) {

        JSONObject bookingLoad = createNewBookingLoad(firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds);

        Response createBookingResponse = PostBookingRequest.createBooking(bookingLoad.toString());
        JsonPath createBookingJsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(createBookingJsonPath);

        assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        verifyCreateResponseContainsCorrectData(createBookingJsonPath, firstname, lastname, totalPrice, depositPaid,
                checkin, checkout, additionalNeeds);

        JsonPath newBooking = getBookingById(bookingId);
        verifyNewBookingContainsCorrectData(newBooking, firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds);
    }



    private JSONObject createNewBookingLoad(String firstname, String lastname, int totalPrice, boolean depositPaid,
                                            String checkin, String checkout, String additionalNeeds) {
        JSONObject bookingDates = new JSONObject();
        bookingDates.put(CHECKIN, checkin);
        bookingDates.put(CHECKOUT, checkout);

        JSONObject newBookingLoad = new JSONObject();
        newBookingLoad.put(FIRSTNAME, firstname);
        newBookingLoad.put(LASTNAME, lastname);
        newBookingLoad.put(TOTAL_PRICE, totalPrice);
        newBookingLoad.put(DEPOSIT_PAID, depositPaid);
        newBookingLoad.put(BOOKING_DATES, bookingDates);
        newBookingLoad.put(ADDITIONAL_NEEDS, additionalNeeds);
        return newBookingLoad;
    }

    private int getBookingId(JsonPath jsonPath) {
        return Integer.parseInt(jsonPath.getString(BOOKING_ID));
    }

    private JsonPath getBookingById(int bookingId) {
        Response getBookingResponse = GetBookingRequest.getBookingById(bookingId);
        return getBookingResponse.jsonPath();
    }

    private void verifyCreateResponseContainsCorrectData(JsonPath jsonPath, String firstname, String lastname,
                                                         int totalPrice, boolean depositPaid, String checkin,
                                                         String checkout, String additionalNeeds) {

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(jsonPath.getString(BOOKING + FIRSTNAME)).isEqualTo(firstname);
        softAssertions.assertThat(jsonPath.getString(BOOKING + LASTNAME)).isEqualTo(lastname);
        softAssertions.assertThat(jsonPath.getInt(BOOKING + TOTAL_PRICE)).isEqualTo(totalPrice);
        softAssertions.assertThat(jsonPath.getBoolean(BOOKING + DEPOSIT_PAID)).isEqualTo(depositPaid);
        softAssertions.assertThat(jsonPath.getString(BOOKING_BOOKING_DATES + CHECKIN)).isEqualTo(checkin);
        softAssertions.assertThat(jsonPath.getString(BOOKING_BOOKING_DATES + CHECKOUT)).isEqualTo(checkout);
        softAssertions.assertThat(jsonPath.getString(BOOKING + ADDITIONAL_NEEDS)).isEqualTo(additionalNeeds);

        softAssertions.assertAll();
    }

    private void verifyNewBookingContainsCorrectData(JsonPath jsonPath, String firstname, String lastname,
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
