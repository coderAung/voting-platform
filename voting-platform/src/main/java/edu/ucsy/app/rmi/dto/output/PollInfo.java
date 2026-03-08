package edu.ucsy.app.rmi.dto.output;

import edu.ucsy.app.rmi.dto.PollDetail;
import edu.ucsy.app.server.entities.Poll;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public record PollInfo(
        UUID id,
        String title,
        String ipAddress,
        LocalDateTime endTime,
        Integer limit,
        Poll.Status status,
        boolean isOwner,
        Boolean isVoted,
        List<OptionInfo> options
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public PollInfo(PollDetail poll) {
        this(poll, null);
    }

    public PollInfo(PollDetail poll, Boolean isVoted) {
        this(
                poll.id(), poll.title(), poll.ipAddress(), poll.endTime(), poll.limit(),
                poll.status(), poll.isOwner(), isVoted,
                poll.options().stream().map(OptionInfo::from).toList()
        );
    }


    public OptionInfo winner() {
        return options.stream().max(Comparator.comparingInt(OptionInfo::votes)).orElseThrow();
    }

    public long total() {
        return options.stream().mapToLong(OptionInfo::votes).sum();
    }
}
