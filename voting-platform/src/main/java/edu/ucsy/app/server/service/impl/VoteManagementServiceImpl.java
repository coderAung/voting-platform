package edu.ucsy.app.server.service.impl;

import edu.ucsy.app.rmi.dto.OptionItem;
import edu.ucsy.app.server.entities.Vote;
import edu.ucsy.app.server.entities.pk.OptionPk;
import edu.ucsy.app.server.entities.pk.VotePk;
import edu.ucsy.app.server.repo.VoteRepo;
import edu.ucsy.app.server.service.VoteManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteManagementServiceImpl implements VoteManagementService {

    private final VoteRepo voteRepo;

    @Override
    @Transactional
    public void bulkCreate(List<OptionItem> items) {
        var votes = new ArrayList<Vote>();
        for(var item : items) {
            var list = item.voters().stream().map(v -> {
                var id = new VotePk();
                id.setOptionId(OptionPk.from(v.optionId()));
                id.setIpAddress(v.ipAddress());

                var vote = new Vote();
                vote.setId(id);
                vote.setVotedAt(LocalDateTime.now());

                return vote;
            }).toList();
            votes.addAll(list);
        }
        voteRepo.saveAll(votes);
    }
}
