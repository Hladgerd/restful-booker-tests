package com.codecool.pp.requests.bookings;

import com.codecool.pp.ApiUrl;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PostBookingRequest {

    public static Response createBooking(String bookingLoad){
        return given()
                .contentType(ContentType.JSON)
                .accept("application/json")
                .body(bookingLoad)
                .when()
                .post(ApiUrl.createBookingUrl())
                .then()
                .extract()
                .response();
    }
}
