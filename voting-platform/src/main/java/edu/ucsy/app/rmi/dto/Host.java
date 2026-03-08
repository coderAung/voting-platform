package edu.ucsy.app.rmi.dto;

import edu.ucsy.app.rmi.VotingService;

import java.io.Serial;
import java.io.Serializable;

public record Host(
        String ipAddress,
        VotingService votingService
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
