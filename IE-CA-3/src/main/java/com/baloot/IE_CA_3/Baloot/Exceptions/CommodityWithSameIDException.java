package com.baloot.IE_CA_3.Baloot.Exceptions;

public class CommodityWithSameIDException extends Exception {
    public CommodityWithSameIDException() {
        super("Cant add new commodity. There is already a commodity with this id!");
    }
}
