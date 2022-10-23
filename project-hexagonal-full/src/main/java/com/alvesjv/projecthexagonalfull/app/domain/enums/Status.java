package com.alvesjv.projecthexagonalfull.app.domain.enums;

public enum Status {
    WAITING, INCLUDED;

    public static Status fromString(String text){
        for(Status status : Status.values()){
            if(status.name().equalsIgnoreCase(text)){
                return status;
            }
        }
        return null;
    }
}
