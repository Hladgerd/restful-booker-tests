package com.codecool.pp.tests.bookings;

import com.codecool.pp.helpers.JsonHelper;
import com.codecool.pp.requests.auth.PostTokenRequest;
import com.codecool.pp.requests.bookings.DeleteBookingRequest;
import com.codecool.pp.requests.bookings.GetBookingRequest;
import com.codecool.pp.requests.bookings.PostBookingRequest;
import com.codecool.pp.requests.bookings.PutBookingRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;

import java.util.Collections;
import java.util.List;

import static com.codecool.pp.helpers.JsonHelper.*;
import static com.codecool.pp.helpers.JsonHelper.ADDITIONAL_NEEDS;

public abstract class BaseBookingTest {

    protected DeleteBookingRequest deleteBookingRequest = new DeleteBookingRequest();
    protected GetBookingRequest getBookingRequest = new GetBookingRequest();
    protected PostBookingRequest postBookingRequest = new PostBookingRequest();
    protected PutBookingRequest putBookingRequest = new PutBookingRequest();
    protected PostTokenRequest postTokenRequest = new PostTokenRequest();

    protected List<Integer> getAllBookingIds() {
        Response allBookingsResponse = getBookingRequest.getAllBookings();
        JsonPath jsonPath = allBookingsResponse.jsonPath();
        return jsonPath.getList(BOOKING_ID);
    }

    protected Response getBookingById(int bookingId) {
        return getBookingRequest.getBookingById(bookingId);
    }

    protected int collectId(Response response) {
        JsonPath jsonPath = response.jsonPath();
        return Integer.parseInt(jsonPath.getString(BOOKING_ID));
    }

    protected List<Integer> collectIds(Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList(BOOKING_ID);
    }

    protected JsonPath pickRandomBooking(List<Integer> bookingIds) {
        Collections.shuffle(bookingIds);
        Response bookingByIdResponse = getBookingRequest.getBookingById(bookingIds.get(0));
        return bookingByIdResponse.jsonPath();
    }

    protected int pickValidId() {
        List<Integer> bookingIds = getAllBookingIds();
        Collections.shuffle(bookingIds);
        return bookingIds.get(0);
    }

    protected int pickInvalidId() {
        List<Integer> bookingIds = getAllBookingIds();
        Collections.sort(bookingIds);
        return bookingIds.get((bookingIds.size()-1)) + 1;
    }

    protected int createNewBooking(){
        String bookingLoad = JsonHelper.createBookingLoad(FIRSTNAME_SAMPLE, LASTNAME_SAMPLE, TOTAL_PRICE_SAMPLE,
                DEPOSIT_PAID_SAMPLE, CHECKIN_SAMPLE, CHECKOUT_SAMPLE, ADDITIONAL_NEEDS_SAMPLE).toString();

        Response createBookingResponse = postBookingRequest.createBooking(bookingLoad);
        return collectId(createBookingResponse);
    }

    protected void verifyResponseContainsCorrectData(Response response, String firstname, String lastname,
                                                     int totalPrice, boolean depositPaid, String checkin,
                                                     String checkout, String additionalNeeds) {

        SoftAssertions softAssertions = new SoftAssertions();
        JsonPath jsonPath = response.jsonPath();

        softAssertions.assertThat(jsonPath.getString(BOOKING + FIRSTNAME)).isEqualTo(firstname);
        softAssertions.assertThat(jsonPath.getString(BOOKING + LASTNAME)).isEqualTo(lastname);
        softAssertions.assertThat(jsonPath.getInt(BOOKING + TOTAL_PRICE)).isEqualTo(totalPrice);
        softAssertions.assertThat(jsonPath.getBoolean(BOOKING + DEPOSIT_PAID)).isEqualTo(depositPaid);
        softAssertions.assertThat(jsonPath.getString(BOOKING_BOOKING_DATES + CHECKIN)).isEqualTo(checkin);
        softAssertions.assertThat(jsonPath.getString(BOOKING_BOOKING_DATES + CHECKOUT)).isEqualTo(checkout);
        softAssertions.assertThat(jsonPath.getString(BOOKING + ADDITIONAL_NEEDS)).isEqualTo(additionalNeeds);

        softAssertions.assertAll();
    }

    protected void verifyBookingContainsCorrectData(Response response, String firstname, String lastname,
                                                  int totalPrice, boolean depositPaid, String checkin,
                                                  String checkout, String additionalNeeds) {

        SoftAssertions softAssertions = new SoftAssertions();
        JsonPath jsonPath = response.jsonPath();

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
