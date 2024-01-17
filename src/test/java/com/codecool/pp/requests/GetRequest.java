package com.codecool.pp.requests;

import com.codecool.pp.ApiUrl;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class GetRequest {

    public static Response getAllBookings(){
        return given()
                .when()
                .get(ApiUrl.getBookingUrl())
                .then()
                .extract()
                .response();
    }
}
