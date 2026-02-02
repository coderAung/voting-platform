package edu.ucsy.app.server.entities.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class VotePk {
    @Column(name = "option_id")
    private OptionPk optionId;
    private String ipAddress;
}
