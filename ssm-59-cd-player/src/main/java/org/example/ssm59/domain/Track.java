package org.example.ssm59.domain;

import java.time.LocalTime;

public record Track(
        String name,
        LocalTime length
) {

    @Override
    public String toString() {
        return name;
    }
}
