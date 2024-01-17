package com.codecool.pp.tests;

import com.codecool.pp.requests.GetRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingIdsTest {

    @Test
    @DisplayName("Get the ids of all the bookings")
    void getBookingIdsTest() {
        Response allIdsResponse = GetRequest.getAllBookings();

        JsonPath jsonPath = allIdsResponse.jsonPath();
        List<Integer> bookingIds = jsonPath.getList("bookingid");
        int bookingIdsSize = bookingIds.size();

        assertThat(allIdsResponse.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(bookingIds).isNotEmpty();
        long uniqueIds = bookingIds.stream()
                .distinct()
                .count();
        assertThat(bookingIdsSize).isEqualTo(uniqueIds);
    }
}
