package com.codecool.pp.tests.bookings;

import com.codecool.pp.helpers.JsonHelper;
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

import static com.codecool.pp.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateBookingTest extends BaseBookingTest {

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
    @DisplayName("Create new booking entry and verify details")
    public void createNewBooking(String firstname, String lastname, int totalPrice, boolean depositPaid, String checkin,
                                 String checkout, String additionalNeeds) {

        String bookingLoad = JsonHelper.createBookingLoad(firstname, lastname, totalPrice, depositPaid, checkin,
                checkout, additionalNeeds).toString();

        Response createBookingResponse = postBookingRequest.createBooking(bookingLoad);
        JsonPath createBookingJsonPath = createBookingResponse.jsonPath();
        bookingId = getBookingId(createBookingJsonPath);

        assertThat(createBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        verifyCreateResponseContainsCorrectData(createBookingJsonPath, firstname, lastname, totalPrice, depositPaid,
                checkin, checkout, additionalNeeds);

        JsonPath newBooking = getBookingById(bookingId);
        verifyNewBookingContainsCorrectData(newBooking, firstname, lastname, totalPrice, depositPaid, checkin,
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

    private int getBookingId(JsonPath jsonPath) {
        return Integer.parseInt(jsonPath.getString(BOOKING_ID));
    }

    private JsonPath getBookingById(int bookingId) {
        Response getBookingResponse = getBookingRequest.getBookingById(bookingId);
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
