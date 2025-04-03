package org.example.ssm59.domain;

import java.time.LocalTime;
import java.util.List;

public record Library(
        List<Cd> collection
) {

    public static Library buildSampleLibrary() {
        var track1 = new Track("Bohemian Rhapsody", length(5, 56));
        var track2 = new Track("Another One Bites the Dust", length(3, 36));
        var track3 = new Track("A kind of Magic", length(4, 22));
        var track4 = new Track("Under Pressure", length(4, 8));

        var cd1 = new Cd("Greatest Hits", List.of(track1, track2));
        var cd2 = new Cd("Greatest Hits II", List.of(track3, track4));

        return new Library(List.of(cd1, cd2));
    }

    private static LocalTime length(int minutes, int seconds) {
        return LocalTime.of(0, minutes, seconds);
    }
}
