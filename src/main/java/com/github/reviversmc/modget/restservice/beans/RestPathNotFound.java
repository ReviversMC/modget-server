package com.github.reviversmc.modget.restservice.beans;

public class RestPathNotFound {

    private final int httpCode;
    private final String error;

    public RestPathNotFound(int httpCode) {

        this.httpCode = httpCode;

        if (httpCode == 404) error = "The endpoint you are searching for does not exist ¯\\_(ツ)_/¯";
        else error = "Unspecified error";

    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getError() {
        return error;
    }
}
