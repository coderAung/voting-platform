package edu.ucsy.app.server.service.impl;

import edu.ucsy.app.rmi.dto.OptionItem;
import edu.ucsy.app.rmi.dto.PollDetail;
import edu.ucsy.app.server.entities.Vote;
import edu.ucsy.app.server.entities.pk.OptionPk;
import edu.ucsy.app.server.entities.pk.VotePk;
import edu.ucsy.app.server.repo.PollRepo;
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

    private final PollRepo pollRepo;
    private final VoteRepo voteRepo;

    @Override
    @Transactional
    public void bulkCreate(PollDetail pollDetail) {
        var votes = new ArrayList<Vote>();
        var poll = pollRepo.findById(pollDetail.id()).orElseThrow();
        for(var item : pollDetail.options()) {
            var option = poll.getOptions().stream().filter(o -> o.getId().toId().equals(item.id())).findFirst().orElseThrow();

            var list = item.voters().stream().map(v -> {
                var id = new VotePk();
                id.setOptionId(OptionPk.from(v.optionId()));
                id.setIpAddress(v.ipAddress());

                var vote = new Vote();
                vote.setId(id);
                vote.setVotedAt(LocalDateTime.now());
                vote.setPoll(poll);
                vote.setOption(option);
                return vote;
            }).toList();
            votes.addAll(list);
        }
        voteRepo.saveAll(votes);
    }
}
