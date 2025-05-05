package com.example.demostore.dtos.errors;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String message, String path, LocalDateTime timestamp) {}
