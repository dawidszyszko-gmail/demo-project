package com.demo.carrental.domain;

public class NoCarsAvailableException extends RuntimeException {

    public NoCarsAvailableException(CarType carType) {
        super("No cars available for type: " + carType);
    }
}

