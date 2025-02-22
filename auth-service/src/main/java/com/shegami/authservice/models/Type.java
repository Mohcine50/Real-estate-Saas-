package com.shegami.authservice.models;

public enum Type {
    ADMIN,
    INDIVIDUAL,
    INDEPENDENT_MARKETER,
    AGENCY_MARKETER,
    AGENCY_EMPLOYEE;


    public static Type getValue(String type) {
        return switch (type){
            case "admin" -> Type.ADMIN;
            case "individual" -> Type.INDIVIDUAL;
            case "agency_marketer" -> Type.AGENCY_MARKETER;
            case "agency_employee" -> Type.AGENCY_EMPLOYEE;
            default -> null;
        };
    }
}
