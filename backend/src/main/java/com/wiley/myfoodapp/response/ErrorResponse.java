package com.wiley.myfoodapp.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {

    private String apiPath;
    private HttpStatus status;
    private String message;
    private LocalDateTime timestamp;
}
