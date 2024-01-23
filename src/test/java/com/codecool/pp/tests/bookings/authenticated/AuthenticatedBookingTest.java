package com.codecool.pp.tests.bookings.authenticated;

import com.codecool.pp.helpers.JsonHelper;
import com.codecool.pp.tests.bookings.BaseBookingTest;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.codecool.pp.helpers.JsonHelper.*;

public abstract class AuthenticatedBookingTest extends BaseBookingTest {

    protected String token;
    protected int bookingId;

    @BeforeEach
    protected void auth() {
        JSONObject authLoad = new JSONObject();
        authLoad.put(USERNAME, JsonHelper.getUsername());
        authLoad.put(PASSWORD, JsonHelper.getPassword());
        Response createTokenResponse = postTokenRequest.createToken(authLoad.toString());
        token = createTokenResponse.jsonPath().getString(TOKEN);
    }

    @AfterEach
    protected void deleteBooking() {
        deleteBookingRequest.deleteBooking(bookingId, token);
    }
}
