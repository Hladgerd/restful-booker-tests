package com.codecool.pp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class PingTest {

    @Test
    @DisplayName("HealthCheck - verify the API is up and running")
    void ping() {
        given()
                .when()
                .get("https://restful-booker.herokuapp.com/ping")
                .then()
                .statusCode(201);
    }
}
