package com.codecool.pp.tests.bookings;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codecool.pp.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class RetrieveBookingTest extends BaseBookingTest {

    private Map<String, String> queryParameter;

    @BeforeEach
    void setup() {
        queryParameter = new HashMap<>();
    }

    @Test
    @DisplayName("Get all bookings and verify that we get result")
    void getBookingsTest() {

        Response allBookingsResponse = getBookingRequest.getAllBookings();
        JsonPath jsonPath = allBookingsResponse.jsonPath();
        List<Integer> bookingIds = jsonPath.getList(BOOKING_ID);

        assertThat(allBookingsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotEmpty();
    }

    @Test
    @DisplayName("Get all bookings and verify their IDs are all unique")
    void getBookingIdsTest() {

        List<Integer> bookingIds = getAllBookingIds();
        int bookingIdsSize = bookingIds.size();
        long uniqueIds = bookingIds.stream()
                .distinct()
                .count();

        assertThat(bookingIdsSize).isEqualTo(uniqueIds);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/bookingValidFilters.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Get filtered bookings and verify the randomly picked booking matches the filter")
    void getFilteredBookingsTest(String key, String value) {

        queryParameter.put(key, value);
        Response filteredBookingsResponse = getBookingRequest.getFilteredBookings(queryParameter);
        JsonPath filteredBookingsJsonPath = filteredBookingsResponse.jsonPath();
        List<Integer> bookingIds = filteredBookingsJsonPath.getList(BOOKING_ID);

        JsonPath pickedBooking = pickRandomBooking(bookingIds);

        if (key.contains("check")) {
            assertThat(pickedBooking.getString(BOOKING_DATES + "." + key)).isGreaterThanOrEqualTo(value);
        } else {
            assertThat(pickedBooking.getString(key)).isEqualTo(value);
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/bookingInvalidFilters.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Get bookings by invalid filter and verify that no data was found")
    void getBookingsByInvalidFilterTest(String key, String value) {

        queryParameter.put(key, value);
        Response filteredBookingsResponse = getBookingRequest.getFilteredBookings(queryParameter);
        String bookingData = filteredBookingsResponse.getBody().asString();

        assertThat(filteredBookingsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingData).isEqualTo("[]");
    }

    @Test
    @DisplayName("Get booking by id and verify that we get result")
    void getBookingByIdTest() {

        int bookingId = pickValidId();
        Response getBookingResponse = getBookingRequest.getBookingById(bookingId);
        JsonPath getBookingJsonPath = getBookingResponse.jsonPath();

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        for (String jsonKey: JSONKEYS) {
            assertThat(getBookingJsonPath.getString(jsonKey)).isNotEmpty();
        }
    }

    @Test
    @DisplayName("Get booking by invalid id and verify that no booking was found")
    void getBookingByInvalidIdTest() {

        int bookingId = pickInvalidId();
        Response getBookingResponse = getBookingRequest.getBookingById(bookingId);

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }
}
