package com.vuongle.imagine.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> implements Serializable {

    private boolean isSuccess;

    private T result;

    private String message;
}
