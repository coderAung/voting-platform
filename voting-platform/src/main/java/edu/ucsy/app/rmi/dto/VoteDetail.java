package edu.ucsy.app.rmi.dto;

import edu.ucsy.app.server.entities.Vote;

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

    public static VoteDetail from(Vote vote) {
        return new VoteDetail(
                vote.getId().getOptionId().toId(),
                vote.getId().getIpAddress(),
                vote.getVotedAt());
    }
}
