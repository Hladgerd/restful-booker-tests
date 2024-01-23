package com.codecool.pp.requests.bookings;

import com.codecool.pp.ApiUrl;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteBookingRequest {

    public static Response deleteBooking(int bookingId, String token) {
        return given()
                .header("Cookie", "token=" + token)
                .when()
                .delete(ApiUrl.getBookingUrl(bookingId))
                .then()
                .extract()
                .response();
    }
}
