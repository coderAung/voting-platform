package edu.ucsy.app.rmi.dto;

import edu.ucsy.app.server.entities.Poll;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PollDetail(
        UUID id,
        String title,
        String ipAddress,
        LocalDateTime endTime,
        Integer limit,
        Poll.Status status,
        List<OptionItem> options
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public PollDetail(PollInfo info, List<OptionItem> options) {
        this(info.id(), info.title(), info.ipAddress(), info.endTime(), info.limit(), info.status(), options);
    }

    public long total() {
        return options.stream().mapToLong(OptionItem::votes).sum();
    }

    public void select(String optionId, String voterIpAddress) {
        options.stream().filter(o -> o.id().equals(optionId))
                .findFirst()
                .orElseThrow()
                .addVote(voterIpAddress);
    }

    public int getVotesByOptionId(String optionId) {
        return options.stream().filter(o -> o.id().equals(optionId))
                .findFirst()
                .orElseThrow()
                .votes();
    }
}
