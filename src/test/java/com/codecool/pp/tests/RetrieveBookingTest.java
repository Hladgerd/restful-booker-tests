package com.codecool.pp.tests;

import com.codecool.pp.requests.GetBookingRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.*;

import static com.codecool.pp.helpers.JsonHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class RetrieveBookingTest {

    private Map<String, String> queryParameter;

    @BeforeEach
    void setup() {
        queryParameter = new HashMap<>();
    }

    @Test
    @DisplayName("Get all bookings and verify that we get result")
    void getBookingsTest() {

        Response allBookingsResponse = GetBookingRequest.getAllBookings();
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
        Response filteredBookingsResponse = GetBookingRequest.getFilteredBookings(queryParameter);
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
        Response filteredBookingsResponse = GetBookingRequest.getFilteredBookings(queryParameter);
        String bookingData = filteredBookingsResponse.getBody().asString();

        assertThat(filteredBookingsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingData).isEqualTo("[]");
    }

    @Test
    @DisplayName("Get booking by id and verify that we get result")
    void getBookingByIdTest() {

        int bookingId = pickValidId();
        Response getBookingResponse = GetBookingRequest.getBookingById(bookingId);
        JsonPath getBookingJsonPath = getBookingResponse.jsonPath();

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        for (String jsonkey: JSONKEYS) {
            assertThat(getBookingJsonPath.getString(jsonkey)).isNotEmpty();
        }
    }

    @Test
    @DisplayName("Get booking by invalid id and verify that no booking was found")
    void getBookingByInvalidIdTest() {

        int bookingId = pickInvalidId();
        Response getBookingResponse = GetBookingRequest.getBookingById(bookingId);

        assertThat(getBookingResponse.statusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    private List<Integer> getAllBookingIds() {
        Response allBookingsResponse = GetBookingRequest.getAllBookings();
        JsonPath jsonPath = allBookingsResponse.jsonPath();
        return jsonPath.getList(BOOKING_ID);
    }

    private JsonPath pickRandomBooking(List<Integer> bookingIds) {
        Collections.shuffle(bookingIds);
        Response bookingByIdResponse = GetBookingRequest.getBookingById(bookingIds.get(0));
        return bookingByIdResponse.jsonPath();
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
