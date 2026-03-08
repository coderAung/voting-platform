package edu.ucsy.app.server.entities.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class OptionPk {
    @Column(name = "poll_id")
    private UUID pollId;
    private int sequenceNo;

    public static OptionPk from(String pk) {
        var array = pk.split("-seq-");
        return new OptionPk(UUID.fromString(array[0]), Integer.parseInt(array[1]));
    }

    public String toId() {
        return "%s-seq-%s".formatted(pollId, sequenceNo);
    }
}
