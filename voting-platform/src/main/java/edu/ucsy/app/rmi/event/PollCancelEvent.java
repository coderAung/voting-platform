package edu.ucsy.app.rmi.event;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public record PollCancelEvent(UUID pollId, String ipAddress) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public String message() {
        return "Poll with id : %s is cancelled".formatted(pollId);
    }
}
