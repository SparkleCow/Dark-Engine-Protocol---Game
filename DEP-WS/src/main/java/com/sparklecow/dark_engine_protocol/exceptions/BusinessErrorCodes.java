package com.sparklecow.dark_engine_protocol.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Email / password is incorrect"),
    CHAT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Chat not found"),
    BAD_CREDENTIALS(400, HttpStatus.UNAUTHORIZED, "Email / password is incorrect"),
    ACCOUNT_LOCKED(423, HttpStatus.LOCKED, "User account is locked"),
    ACCOUNT_DISABLED(403, HttpStatus.FORBIDDEN, "User account is disabled"),
    METHOD_NOT_ALLOWED(405, HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed"),
    VALIDATION_ERROR(400, HttpStatus.BAD_REQUEST, "Validation error"),
    TOKEN_EXPIRED(401, HttpStatus.UNAUTHORIZED, "Token expired"),
    TOKEN_INVALID(401, HttpStatus.UNAUTHORIZED, "Invalid token"),
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    MESSAGE_ERROR(500,  HttpStatus.INTERNAL_SERVER_ERROR, "Error sending data"),
    ILLEGAL_OPERATION(403, HttpStatus.FORBIDDEN, "Illegal operation"),
    IO_EXCEPTION(500, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    BusinessErrorCodes(int errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}