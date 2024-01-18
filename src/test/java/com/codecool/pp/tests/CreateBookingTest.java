package com.codecool.pp.tests;

import com.codecool.pp.requests.PostBookingRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
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

        String bookingLoad = createNewBookingLoad(firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds).toString();

        Response createBookingResponse = PostBookingRequest.createBooking(bookingLoad);

        assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
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

}
