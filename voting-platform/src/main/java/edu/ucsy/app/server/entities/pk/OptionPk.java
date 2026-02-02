package edu.ucsy.app.server.entities.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.UUID;

@Data
@Embeddable
public class OptionPk {
    @Column(name = "poll_id")
    private UUID pollId;
    private int sequenceNo;
}
