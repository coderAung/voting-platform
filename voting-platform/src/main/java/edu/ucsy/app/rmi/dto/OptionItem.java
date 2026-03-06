package edu.ucsy.app.rmi.dto;

import edu.ucsy.app.server.entities.Option;
import edu.ucsy.app.server.entities.pk.OptionPk;

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

    public Option getEntity() {
        var option = new Option();
        option.setId(OptionPk.from(id));
        option.setTitle(title);
        return option;
    }

    public static OptionItem from(Option option) {
        return new OptionItem(
                option.getId().toId(),
                option.getTitle(),
                option.getVotes().stream()
                        .map(VoteDetail::from)
                        .toList());
    }
}
