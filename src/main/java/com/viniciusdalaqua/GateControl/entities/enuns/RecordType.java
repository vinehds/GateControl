package com.viniciusdalaqua.GateControl.entities.enuns;

public enum RecordType {

    IN(1),OUT(2);


    private int code;

    RecordType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static RecordType valueOf(int code) {
        for (RecordType recordType : RecordType.values()) {
            if (recordType.getCode() == code) {
                return recordType;
            }
        }
        throw new IllegalArgumentException("Invalid RecordType code: " + code);
    }
}
