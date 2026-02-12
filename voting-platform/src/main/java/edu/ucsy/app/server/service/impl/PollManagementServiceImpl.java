package edu.ucsy.app.server.service.impl;

import edu.ucsy.app.rmi.dto.OptionItem;
import edu.ucsy.app.rmi.dto.PollDetail;
import edu.ucsy.app.rmi.dto.PollForm;
import edu.ucsy.app.server.entities.Option;
import edu.ucsy.app.server.entities.Poll;
import edu.ucsy.app.server.entities.pk.OptionPk;
import edu.ucsy.app.server.repo.OptionRepo;
import edu.ucsy.app.server.repo.PollRepo;
import edu.ucsy.app.server.service.PollManagementService;
import edu.ucsy.app.utils.exception.VotingPlatformBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PollManagementServiceImpl implements PollManagementService {

    private final PollRepo pollRepo;
    private final OptionRepo optionRepo;

    @Override
    @Transactional
    public UUID create(PollForm form) {
        var seq = 1;
        var poll = new Poll();
        poll.setTitle(form.title());
        poll.setEndTime(form.endTime());
        poll.setVoteLimit(form.voteLimit());
        poll.setStatus(Poll.Status.Active);
        poll.setCreatedAt(LocalDateTime.now());
        poll.setIsOwner(true);

        poll = pollRepo.save(poll);

        var options = new ArrayList<Option>();
        for (var item : form.options()) {
            var option = new Option();
            option.setId(new OptionPk(poll.getId(), seq));
            options.add(option);
            seq ++;
        }
        optionRepo.saveAll(options);

        return poll.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public PollDetail findById(UUID id) {
        return pollRepo.findById(id).map(PollDetail::from).orElseThrow(() -> new VotingPlatformBusinessException("Poll with id : %s is not found.".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PollDetail> getAll() {
        return pollRepo.findAll().stream().map(PollDetail::from).toList();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        var poll = pollRepo.findById(id).orElseThrow(() -> new VotingPlatformBusinessException("Poll with id : %s is not found.".formatted(id)));
        pollRepo.delete(poll);
    }

    @Override
    @Transactional
    public void create(PollDetail detail, boolean isOwner) {
        var poll = detail.getEntity(isOwner);
        var options = detail.options().stream().map(OptionItem::getEntity).toList();
        pollRepo.save(poll);
        optionRepo.saveAll(options);
    }
}
