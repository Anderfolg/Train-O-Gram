package org.anderfolg.trainogram.entities;

import java.time.LocalDateTime;

public record ApiResponse(boolean success, String message) {

    public String getTimestamp() {
        return LocalDateTime.now().toString();
    }

}
