package org.example.serviceuser.config.Exception;

import lombok.extern.slf4j.Slf4j;
import org.example.serviceuser.config.ApiResponse.ApiResponse;
import org.example.serviceuser.user.sign.exception.GitHubAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获 @Valid 或 @Validated 的 异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 收集所有字段错误信息
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // 返回自定义响应对象，包含详细的错误信息
        return ResponseEntity.ok(new ApiResponse(400, "参数校验失败"+errors));
    }
    
    /**
     * 处理GitHub认证异常
     * 根据是否为用户错误决定日志级别
     *
     * @param ex GitHub认证异常
     * @return 响应对象
     */
    @ExceptionHandler(GitHubAuthException.class)
    public ResponseEntity<ApiResponse> handleGitHubAuthException(GitHubAuthException ex) {
        if (ex.isUserError()) {
            // 用户错误使用WARN级别，不打印堆栈
            log.warn("GitHub认证用户错误: {}", ex.getMessage());
        } else {
            // 系统错误使用ERROR级别，打印完整堆栈
            log.error("GitHub认证系统错误: {}", ex.getMessage(), ex);
        }
        
        return ResponseEntity.ok(new ApiResponse(400, ex.getMessage()));
    }
}
