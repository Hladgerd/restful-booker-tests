package com.codecool.pp.tests;

import com.codecool.pp.requests.GetRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingIdsTest {

    private Map<String, String> queryParameter;

    @BeforeEach
    void setup() {
        queryParameter = new HashMap<>();
    }

    @Test
    @DisplayName("Get all bookings and verify we get result")
    void getBookingsTest() {

        Response allBookingsResponse = GetRequest.getAllBookings();
        JsonPath jsonPath = allBookingsResponse.jsonPath();
        List<Integer> bookingIds = jsonPath.getList("bookingid");

        assertThat(allBookingsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotEmpty();
    }

    @Test
    @DisplayName("Get all bookings and verify their IDs are all unique")
    void getBookingIdsTest() {

        Response allBookingsResponse = GetRequest.getAllBookings();
        JsonPath jsonPath = allBookingsResponse.jsonPath();
        List<Integer> bookingIds = jsonPath.getList("bookingid");

        int bookingIdsSize = bookingIds.size();
        long uniqueIds = bookingIds.stream()
                .distinct()
                .count();

        assertThat(bookingIdsSize).isEqualTo(uniqueIds);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/bookingFilters.csv", numLinesToSkip = 1, delimiter = ';')
    @DisplayName("Get filtered bookings and verify the randomly picked booking matches the filter")
    void getFilteredBookingsTest(String key, String value) {

        queryParameter.put(key, value);
        Response filteredBookingsResponse = GetRequest.getFilteredBookings(queryParameter);
        JsonPath filteredBookingsJsonPath = filteredBookingsResponse.jsonPath();
        List<Integer> bookingIds = filteredBookingsJsonPath.getList("bookingid");

        JsonPath pickedBooking = pickRandomBooking(bookingIds);

        if (key.contains("check")) {
            assertThat(pickedBooking.getString("bookingdates" + "." + key)).isGreaterThanOrEqualTo(value);
        } else {
            assertThat(pickedBooking.getString(key)).isEqualTo(value);
        }
    }

    private JsonPath pickRandomBooking(List<Integer> bookingIds) {
        Collections.shuffle(bookingIds);
        Response bookingByIdResponse = GetRequest.getBookingById(bookingIds.get(0));
        return bookingByIdResponse.jsonPath();
    }
}
