package com.baloot.IE_CA_3.Baloot.Exceptions;

public class ItemAlreadyExistsInBuyListException extends Exception {
    public ItemAlreadyExistsInBuyListException() {
        super("The selected item already exists in your buy list!");
    }
}
