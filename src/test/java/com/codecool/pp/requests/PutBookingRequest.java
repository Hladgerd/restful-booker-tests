package com.codecool.pp.requests;

import com.codecool.pp.ApiUrl;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PutBookingRequest {

    public static Response updateBooking(String bookingLoad, int bookingId, String token){
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(bookingLoad)
                .when()
                .put(ApiUrl.getBookingUrl(bookingId))
                .then()
                .extract()
                .response();
    }
}