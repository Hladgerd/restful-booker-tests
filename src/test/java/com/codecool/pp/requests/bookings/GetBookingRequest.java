package com.codecool.pp.requests.bookings;

import com.codecool.pp.ApiUrl;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class GetBookingRequest {

    public Response getAllBookings(){
        return given()
                .when()
                .get(ApiUrl.getBookingUrl())
                .then()
                .extract()
                .response();
    }

    public Response getFilteredBookings(Map<String, String> queryParams){
        return given()
                .when()
                .queryParams(queryParams)
                .get(ApiUrl.getBookingUrl())
                .then()
                .extract()
                .response();
    }

    public Response getBookingById(int bookingId){
        return given()
                .when()
                .get(ApiUrl.getBookingUrl(bookingId))
                .then()
                .extract()
                .response();
    }
}
