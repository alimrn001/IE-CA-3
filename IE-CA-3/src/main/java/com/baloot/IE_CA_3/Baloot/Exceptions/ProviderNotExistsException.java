package com.baloot.IE_CA_3.Baloot.Exceptions;

public class ProviderNotExistsException extends Exception {
    public ProviderNotExistsException() {
        super("The selected provider does not exists!");
    }
}
