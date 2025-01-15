/**
 * File Name: ApiResponse.java
 * Description: Todo
 * Author: holic512
 * Created Date: 2025-01-15
 * Version: 1.0
 * Usage:
 * Todo
 */
package org.example.serviceadmin.config.ApiResponse;

import lombok.Data;

@Data
public class ApiResponse {
    int status;
    String message;
    Object data;

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
