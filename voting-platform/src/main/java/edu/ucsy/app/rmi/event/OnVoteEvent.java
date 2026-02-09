package edu.ucsy.app.rmi.event;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public record OnVoteEvent(
        UUID pollId,
        String optionId,
        int votes
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
