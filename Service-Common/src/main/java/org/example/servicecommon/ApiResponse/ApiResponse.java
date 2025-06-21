/**
 * File Name: ApiResponse.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.servicecommon.ApiResponse;

import lombok.Data;


/**
 * ApiResponse 类用于封装 API 请求的响应数据，包含状态码、消息和返回数据。
 */
@Data
public class ApiResponse {
    int status;
    String message;
    Object data;

    /**
     * 构造函数，用于创建 ApiResponse 实例，仅传入状态码和消息
     *
     * @param status 响应的状态码
     */
    public ApiResponse(int status) {
        this.status = status;
    }

    /**
     * 构造函数，用于创建 ApiResponse 实例，仅传入状态码和消息
     *
     * @param status  响应的状态码
     * @param message 响应的消息
     */
    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * 构造函数，用于创建 ApiResponse 实例，传入状态码、消息和数据
     *
     * @param status  响应的状态码
     * @param message 响应的消息
     * @param data    响应的数据
     */
    public ApiResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * 推荐使用此静态方法快速创建一个成功响应的 ApiResponse 实例
     *
     * @return 返回一个封装了 200 状态码和成功消息的 ApiResponse 实例
     */
    public static ApiResponse ok() {
        return new ApiResponse(200, "操作成功");
    }


    /**
     * 推荐使用此静态方法快速创建一个成功响应的 ApiResponse 实例
     *
     * @param data 返回的数据
     * @return 返回一个封装了 200 状态码和成功消息的 ApiResponse 实例
     */
    public static ApiResponse ok(Object data) {
        return new ApiResponse(200, "操作成功", data);
    }

    /**
     * 推荐使用此静态方法快速创建一个成功响应的 ApiResponse 实例，支持自定义消息
     *
     * @param message 自定义的成功消息
     * @param data    返回的数据
     * @return 返回一个封装了 200 状态码和自定义消息的 ApiResponse 实例
     */
    public static ApiResponse ok(String message, Object data) {
        return new ApiResponse(200, message, data);
    }

    /**
     * 创建一个失败响应的 ApiResponse 实例，适用于错误场景
     *
     * @param status 错误状态码
     * @return 返回一个封装了指定状态码和错误消息的 ApiResponse 实例
     */
    public static ApiResponse error(int status) {
        return new ApiResponse(status);
    }


    /**
     * 创建一个失败响应的 ApiResponse 实例，适用于错误场景
     *
     * @param status  错误状态码
     * @param message 错误消息
     * @return 返回一个封装了指定状态码和错误消息的 ApiResponse 实例
     */
    public static ApiResponse error(int status, String message) {
        return new ApiResponse(status, message);
    }

    /**
     * 创建一个失败响应的 ApiResponse 实例，支持返回错误数据
     *
     * @param status  错误状态码
     * @param message 错误消息
     * @param data    返回的错误数据
     * @return 返回一个封装了指定状态码、错误消息和数据的 ApiResponse 实例
     */
    public static ApiResponse error(int status, String message, Object data) {
        return new ApiResponse(status, message, data);
    }
}

