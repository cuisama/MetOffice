package com.weather.framework.exception;

public class ErrorEntity {
	private int resp_code;
    private String resp_msg;

    public ErrorEntity(int resp_code, String resp_msg) {
        this.resp_code = resp_code;
        this.resp_msg = resp_msg;
    }
    public ErrorEntity() {
    }

    public int getResp_code() {
        return resp_code;
    }

    public void setResp_code(int resp_code) {
        this.resp_code = resp_code;
    }

    public String getResp_msg() {
        return resp_msg;
    }

    public void setResp_msg(String resp_msg) {
        this.resp_msg = resp_msg;
    }
}
