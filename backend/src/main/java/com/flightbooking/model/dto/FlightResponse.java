package com.flightbooking.model.dto;

import java.util.List;

public class FlightResponse {

    private final int count;
    private final List<Object> data;

    public FlightResponse(List<Object> data) {
        this.data = data;
        this.count = data.size();
    }

    public int getCount() { return count; }
    public List<Object> getData() { return data; }
}