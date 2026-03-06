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
        LocalDateTime createdAt,
        LocalDateTime endTime,
        Integer limit,
        Poll.Status status,
        List<OptionItem> options
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static PollDetail from(Poll poll) {
        return new PollDetail(
                poll.getId(),
                poll.getTitle(),
                poll.getIpAddress(),
                poll.getCreatedAt(),
                poll.getEndTime(),
                poll.getVoteLimit(),
                poll.getStatus(),
                poll.getOptions().stream().map(OptionItem::from).toList()
        );
    }

    public long total() {
        return options.stream().mapToLong(OptionItem::votes).sum();
    }

    public void select(String optionId, String voterIpAddress) {
        options.stream().filter(o -> o.id().equals(optionId))
                .findFirst()
                .orElseThrow()
                .addVote(new VoteDetail(optionId, voterIpAddress, LocalDateTime.now()));
    }

    public int getVotesByOptionId(String optionId) {
        return options.stream().filter(o -> o.id().equals(optionId))
                .findFirst()
                .orElseThrow()
                .votes();
    }

    public Poll getEntity(boolean isOwner) {
        var poll = new Poll();
        poll.setId(id);
        poll.setTitle(title);
        poll.setCreatedAt(createdAt);
        poll.setEndTime(endTime);
        poll.setStatus(status);
        poll.setIsOwner(isOwner);
        poll.setVoteLimit(limit);
        return poll;
    }

}
