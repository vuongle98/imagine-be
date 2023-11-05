package com.vuongle.imagine.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {

    private String message;
    private Instant timestamp;
    private String details;
}
