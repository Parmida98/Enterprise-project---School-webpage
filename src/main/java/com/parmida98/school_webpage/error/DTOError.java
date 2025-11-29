package com.parmida98.school_webpage.error;

import java.time.LocalDateTime;

public record DTOError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
