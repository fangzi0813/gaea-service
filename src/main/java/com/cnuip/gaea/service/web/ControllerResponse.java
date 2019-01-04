package com.cnuip.gaea.service.web;

public class ControllerResponse {
    protected <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(0, "success", data);
    }
}
