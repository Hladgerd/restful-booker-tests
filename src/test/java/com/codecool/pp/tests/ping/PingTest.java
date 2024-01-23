package com.codecool.pp.tests.ping;

import com.codecool.pp.ApiUrl;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class PingTest {

    @Test
    @DisplayName("HealthCheck - verify the API is up and running")
    void ping() {
        given()
                .when()
                .get(ApiUrl.getPingUrl())
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }
}
