package org.example.ssm59.domain;

import java.util.List;

public record Cd(
        String name,
        List<Track> tracks
) {
    @Override
    public String toString() {
        return name;
    }
}
