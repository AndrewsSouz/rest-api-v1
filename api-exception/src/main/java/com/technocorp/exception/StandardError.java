package com.technocorp.exception;

import lombok.Builder;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Data
@Builder
public class StandardError {
    private final String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            .format(Calendar.getInstance().getTime());
    private final int status;
    private final String message;
}
