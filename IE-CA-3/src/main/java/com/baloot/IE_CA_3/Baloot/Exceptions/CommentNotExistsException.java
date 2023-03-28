package com.baloot.IE_CA_3.Baloot.Exceptions;

public class CommentNotExistsException extends Exception {
    public CommentNotExistsException() {
        super("The selected comment does not exist!");
    }
}
