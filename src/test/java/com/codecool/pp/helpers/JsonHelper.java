package com.codecool.pp.helpers;

import org.json.JSONObject;

import java.util.List;
import java.util.ResourceBundle;

public class JsonHelper {
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String TOTAL_PRICE = "totalprice";
    public static final String DEPOSIT_PAID = "depositpaid";
    public static final String CHECKIN = "checkin";
    public static final String CHECKOUT = "checkout";
    public static final String ADDITIONAL_NEEDS = "additionalneeds";
    public static final String BOOKING_DATES = "bookingdates";

    public static final String BOOKING_ID = "bookingid";
    public static final String BOOKING = "booking.";
    public static final String BOOKING_BOOKING_DATES = "booking.bookingdates.";

    public static final String TOKEN = "token";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final List<String> JSONKEYS = List.of(FIRSTNAME, LASTNAME, TOTAL_PRICE, DEPOSIT_PAID,
            BOOKING_DATES + "." + CHECKIN, BOOKING_DATES + "." + CHECKOUT, ADDITIONAL_NEEDS);

    public static JSONObject createBookingLoad(String firstname, String lastname, int totalPrice, boolean depositPaid,
                                            String checkin, String checkout, String additionalNeeds) {
        JSONObject bookingDates = new JSONObject();
        bookingDates.put(CHECKIN, checkin);
        bookingDates.put(CHECKOUT, checkout);

        JSONObject newBookingLoad = new JSONObject();
        newBookingLoad.put(FIRSTNAME, firstname);
        newBookingLoad.put(LASTNAME, lastname);
        newBookingLoad.put(TOTAL_PRICE, totalPrice);
        newBookingLoad.put(DEPOSIT_PAID, depositPaid);
        newBookingLoad.put(BOOKING_DATES, bookingDates);
        newBookingLoad.put(ADDITIONAL_NEEDS, additionalNeeds);
        return newBookingLoad;
    }

    public static String getUsername() {
        return getProperty(USERNAME);
    }

    public static String getPassword() {
        return getProperty(PASSWORD);
    }

    private static String getProperty(String key) {
        return ResourceBundle.getBundle("authentication").getString(key);
    }
}
