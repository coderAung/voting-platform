package edu.ucsy.app.server.repo;

import edu.ucsy.app.server.entities.Vote;
import edu.ucsy.app.server.entities.pk.VotePk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepo extends JpaRepository<Vote, VotePk> {
}
