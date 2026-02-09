package edu.ucsy.app.rmi.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public record VoteDetail(
        String optionId,
        String ipAddress,
        LocalDateTime votedAt
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
