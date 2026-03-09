package edu.ucsy.app.rmi.dto;

import edu.ucsy.app.rmi.VotingService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

public record Voter(
        String ipAddress,
        IsVoted isVotedStatus,
        VotingService votingService
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Voter(String ipAddress, VotingService votingService) {
        this(ipAddress, new IsVoted(false), votingService);
    }

    public void voted() {
        isVotedStatus.setValue(true);
    }

    public boolean isVoted() {
        return isVotedStatus.value;
    }

    @AllArgsConstructor
    private static class IsVoted {
        @Setter
        @Getter
        private boolean value;
    }
}
