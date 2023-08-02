package com.vuongle.imagine.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageResponse implements Serializable {
    private String title;
    private String message;

    private int status;

    public MessageResponse(String title, String message, HttpStatus status) {
        this.title = title;
        this.message = message;
        this.status = status.value();
    }

}
