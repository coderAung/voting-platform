package edu.ucsy.app.rmi.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record OptionItem(
        String id,
        String title,
        List<VoteDetail> voters
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public int votes() {
        return voters.size();
    }

    public void addVote(VoteDetail vote) {
        voters.add(vote);
    }
}
