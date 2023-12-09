package com.example.session.exception;

public record ApiError(String message, Integer statusCode) {}