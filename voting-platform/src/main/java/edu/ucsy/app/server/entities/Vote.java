package edu.ucsy.app.server.entities;

import edu.ucsy.app.server.entities.pk.VotePk;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Vote {

    @EmbeddedId
    private VotePk id;

    @ManyToOne(optional = false, cascade = {CascadeType.ALL})
    @JoinColumn(insertable = false, updatable = false)
    private Poll poll;

    @ManyToOne(optional = false, cascade = {CascadeType.ALL})
    private Option option;

    @Column(nullable = false)
    private LocalDateTime votedAt;
}
