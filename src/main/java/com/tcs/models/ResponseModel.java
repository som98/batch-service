package com.tcs.models;

public class ResponseModel {
    public boolean success;
    public String response;
    public ResponseModel(){}

    public ResponseModel(boolean success, String response) {
        this.success = success;
        this.response = response;
    }
}
