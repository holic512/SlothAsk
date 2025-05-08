/**
 * File Name: GitHubAuthException.java
 * Description: GitHub认证异常类
 * Author: holic512
 * Created Date: 2025-05-08
 * Version: 1.0
 * Usage:
 * 用于封装GitHub OAuth认证过程中出现的异常
 */
package org.example.serviceuser.user.sign.exception;

/**
 * GitHub认证异常类
 * 处理GitHub OAuth认证过程中可能发生的各种异常
 */
public class GitHubAuthException extends RuntimeException {

    /**
     * 是否为用户操作错误（而非系统错误）
     */
    private boolean isUserError = false;

    /**
     * 默认构造函数
     */
    public GitHubAuthException() {
        super();
    }

    /**
     * 带错误信息的构造函数
     *
     * @param message 错误信息
     */
    public GitHubAuthException(String message) {
        super(message);
    }

    /**
     * 带错误信息和用户错误标识的构造函数
     *
     * @param message 错误信息
     * @param isUserError 是否为用户错误
     */
    public GitHubAuthException(String message, boolean isUserError) {
        super(message);
        this.isUserError = isUserError;
    }

    /**
     * 带错误信息和原因的构造函数
     *
     * @param message 错误信息
     * @param cause 原因异常
     */
    public GitHubAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 带错误信息、原因和用户错误标识的构造函数
     *
     * @param message 错误信息
     * @param cause 原因异常
     * @param isUserError 是否为用户错误
     */
    public GitHubAuthException(String message, Throwable cause, boolean isUserError) {
        super(message, cause);
        this.isUserError = isUserError;
    }

    /**
     * 带原因的构造函数
     *
     * @param cause 原因异常
     */
    public GitHubAuthException(Throwable cause) {
        super(cause);
    }

    /**
     * 判断是否为用户错误
     *
     * @return 如果是用户操作导致的错误返回true，否则返回false
     */
    public boolean isUserError() {
        return isUserError;
    }

    /**
     * 设置异常是否为用户错误
     *
     * @param userError 是否为用户错误
     */
    public void setUserError(boolean userError) {
        isUserError = userError;
    }
} 