package edu.ucsy.app.server.repo;

import edu.ucsy.app.server.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PollRepo extends JpaRepository<Poll, UUID> {
    List<Poll> findAllByOrderByEndTimeDesc();
}
