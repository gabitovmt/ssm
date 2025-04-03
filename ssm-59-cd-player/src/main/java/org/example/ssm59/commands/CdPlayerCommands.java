package org.example.ssm59.commands;

import lombok.RequiredArgsConstructor;
import org.example.ssm59.cdplayer.CdPlayer;
import org.example.ssm59.domain.Cd;
import org.example.ssm59.domain.Library;
import org.example.ssm59.domain.Track;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Command
@RequiredArgsConstructor
public class CdPlayerCommands {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("mm:ss");
    private final CdPlayer cdPlayer;
    private final Library library;

    @Command(command = "cd lcd", description = "Prints CD player LCD info")
    public String lcd() {
        return cdPlayer.getLdcStatus();
    }

    @Command(command = "cd library", description = "List user CD library")
    public String library() {
        return toString(library);
    }

    private String toString(Library library) {
        var index = new AtomicInteger(0);

        return library.collection().stream()
                .map(cd -> toString(cd, index))
                .collect(Collectors.joining("\n"));
    }

    private String toString(Cd cd, AtomicInteger index) {
        var trackIndex = new AtomicInteger(0);
        var tracks = cd.tracks().stream()
                .map(track -> toString(track, trackIndex))
                .collect(Collectors.joining("\n"));

        return MessageFormat.format("{0}: {1}\n{2}", index.getAndIncrement(), cd.name(), tracks);
    }

    private String toString(Track track, AtomicInteger index) {
        return MessageFormat.format("  {0}: {1}  {2}",
                index.getAndIncrement(),
                track.name(),
                track.length().format(FORMATTER)
        );
    }

    @Command(command = "cd load", description = "Load CD into player")
    public String load(@Option int index) {
        try {
            Cd cd = library.collection().get(index);
            cdPlayer.load(cd);
            return "Loading cd " + cd;
        } catch (Exception ex) {
            return "CD with index " + index + " not found, check library";
        }
    }

    @Command(command = "cd play", description = "Press player play button")
    public void play() {
        cdPlayer.play();
    }

    @Command(command = "cd stop", description = "Press player stop button")
    public void stop() {
        cdPlayer.stop();
    }

    @Command(command = "cd pause", description = "Press player pause button")
    public void pause() {
        cdPlayer.pause();
    }

    @Command(command = "cd eject", description = "Press player eject button")
    public void eject() {
        cdPlayer.eject();
    }

    @Command(command = "cd forward", description = "Press player forward button")
    public void forward() {
        cdPlayer.forward();
    }

    @Command(command = "cd back", description = "Press player back button")
    public void back() {
        cdPlayer.back();
    }
}
