package com.viniciusdalaqua.GateControl.services.exception;

import static java.lang.String.format;

public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(String plate) {
        super(format("vehicle with plate %s not found", plate));
    }

}
