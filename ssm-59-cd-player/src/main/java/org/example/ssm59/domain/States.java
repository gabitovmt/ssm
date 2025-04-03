package org.example.ssm59.domain;

public enum States {
    // super state of PLAYING and PAUSED
    BUSY,
    PLAYING,
    PAUSED,
    // super state of CLOSED and OPEN
    IDLE,
    CLOSED,
    OPEN
}
