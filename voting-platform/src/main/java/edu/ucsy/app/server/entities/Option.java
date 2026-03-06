package edu.ucsy.app.server.entities;

import edu.ucsy.app.server.entities.pk.OptionPk;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Option {
    @EmbeddedId
    private OptionPk id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false, cascade = {CascadeType.ALL})
    @JoinColumn(insertable = false, updatable = false)
    private Poll poll;

    @OneToMany(mappedBy = "option")
    private List<Vote> votes;
}
