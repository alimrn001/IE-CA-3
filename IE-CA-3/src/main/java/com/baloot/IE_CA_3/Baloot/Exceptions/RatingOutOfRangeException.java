package com.baloot.IE_CA_3.Baloot.Exceptions;

public class RatingOutOfRangeException extends Exception {
    public RatingOutOfRangeException() {
        super("Rating must be an integer between 1 and 10!");
    }
}
