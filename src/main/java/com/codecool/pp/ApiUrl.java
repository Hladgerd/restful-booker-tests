package com.codecool.pp;

public class ApiUrl {

    public static final String BASE_URL = "https://restful-booker.herokuapp.com/";
    public static final String BOOKING = "booking";
    public static final String PING = "ping";

    public static String getBookingUrl(){
        return BASE_URL + BOOKING;
    }

    public static String getPingUrl(){
        return BASE_URL + PING;
    }
}
