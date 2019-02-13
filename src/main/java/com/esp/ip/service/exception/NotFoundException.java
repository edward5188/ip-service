package com.esp.ip.service.exception;


import java.io.IOException;

public class NotFoundException extends IOException {

    public NotFoundException(String name) {
        super(name);
    }
}
