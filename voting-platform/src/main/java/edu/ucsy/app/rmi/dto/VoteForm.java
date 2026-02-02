package edu.ucsy.app.rmi.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public record VoteForm(
    UUID pollId,
    String optionId,
    String ipAddress
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
