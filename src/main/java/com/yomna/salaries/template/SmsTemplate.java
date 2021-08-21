package com.yomna.salaries.template;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class SmsTemplate {
    private final String header = "Dear Customer, \n";
    private final String intro = "Please notice that ";
    protected LocalDateTime timestamp;
    protected String body;

    public String toString() {
        String msg = header + intro + body;
        if (timestamp != null) {
            msg += " on "+timestamp;
        }

        return msg;
    }
}
