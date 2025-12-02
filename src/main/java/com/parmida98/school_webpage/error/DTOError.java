package com.parmida98.school_webpage.error;

import java.time.OffsetDateTime;

public record DTOError(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
