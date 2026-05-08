package com.flightbooking.model;

public class FlightResponse {
    private Object data;
    private Object pagination;

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public Object getPagination() { return pagination; }
    public void setPagination(Object pagination) { this.pagination = pagination; }
}