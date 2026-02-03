package edu.ucsy.app.rmi.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record PollForm(
        String title,
        LocalDateTime endTime,
        Integer voteLimit,
        List<String> options
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
