package com.baloot.IE_CA_3.Baloot.Exceptions;

public class DiscountCouponNotExistsException extends Exception {
    public DiscountCouponNotExistsException() {
        super("There is no discount coupon with this code!");
    }
}
