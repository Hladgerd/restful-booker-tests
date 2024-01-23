package com.codecool.pp.requests.auth;

import com.codecool.pp.ApiUrl;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PostTokenRequest {

    public Response createToken(String authLoad) {
        return given()
                .contentType(ContentType.JSON)
                .body(authLoad)
                .when()
                .post(ApiUrl.getAuthUrl())
                .then()
                .extract()
                .response();
    }
}
