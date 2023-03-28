package com.baloot.IE_CA_3.Baloot.Exceptions;

public class ItemNotInBuyListForRemovingException extends Exception {
    public ItemNotInBuyListForRemovingException() {
        super("The selected product is not in buy list to be deleted !");
    }
}
