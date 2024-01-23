package com.codecool.pp.tests.bookings;

import com.codecool.pp.requests.auth.PostTokenRequest;
import com.codecool.pp.requests.bookings.DeleteBookingRequest;
import com.codecool.pp.requests.bookings.GetBookingRequest;
import com.codecool.pp.requests.bookings.PostBookingRequest;
import com.codecool.pp.requests.bookings.PutBookingRequest;

public abstract class BaseBookingTest {

    DeleteBookingRequest deleteBookingRequest = new DeleteBookingRequest();
    GetBookingRequest getBookingRequest = new GetBookingRequest();
    PostBookingRequest postBookingRequest = new PostBookingRequest();
    PutBookingRequest putBookingRequest = new PutBookingRequest();
    PostTokenRequest postTokenRequest = new PostTokenRequest();

}
