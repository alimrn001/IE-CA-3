package com.baloot.IE_CA_3.Baloot.Utilities;

public class EmailParser {
    public String getEmailUsername(String email) {
        return email.split("@")[0];
    }
}
