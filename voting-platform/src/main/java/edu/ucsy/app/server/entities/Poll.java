package edu.ucsy.app.server.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime endTime;
    private Integer voteLimit;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Boolean isOwner;

    @OneToMany(mappedBy = "poll")
    private List<Option> options;

    public enum Status {
        Active, Fail, Cancel, Finished
    }

}
