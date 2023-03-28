package com.baloot.IE_CA_3.Baloot.Exceptions;

public class CommodityNotExistsException extends Exception {
    public CommodityNotExistsException() {
        super("There is not a commodity with this ID!");
    }
}
