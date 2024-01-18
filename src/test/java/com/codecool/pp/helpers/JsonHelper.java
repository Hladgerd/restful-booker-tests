package com.codecool.pp.helpers;

import java.util.List;

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

    public static final List<String> JSONKEYS = List.of(FIRSTNAME, LASTNAME, TOTAL_PRICE, DEPOSIT_PAID,
            BOOKING_DATES + "." + CHECKIN, BOOKING_DATES + "." + CHECKOUT, ADDITIONAL_NEEDS);
}
