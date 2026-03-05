package edu.ucsy.app.server.service.impl;

import edu.ucsy.app.rmi.dto.OptionItem;
import edu.ucsy.app.rmi.dto.PollDetail;
import edu.ucsy.app.rmi.dto.PollForm;
import edu.ucsy.app.server.entities.Option;
import edu.ucsy.app.server.entities.Poll;
import edu.ucsy.app.server.entities.pk.OptionPk;
import edu.ucsy.app.server.repo.PollRepo;
import edu.ucsy.app.server.repo.OptionRepo;
import edu.ucsy.app.server.service.PollManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PollManagementServiceImpl implements PollManagementService {

    private final PollRepo pollRepo;
    private final OptionRepo optionRepo;

    @Override
    @Transactional
    public UUID create(PollForm form) {
        var poll = new Poll();
        poll.setTitle(form.title());
        poll.setCreatedAt(LocalDateTime.now());
        poll.setEndTime(form.endTime());
        poll.setVoteLimit(form.voteLimit());
        poll.setStatus(Poll.Status.Active);
        poll.setIsOwner(true);
        var saved = pollRepo.save(poll);

        // 2. Save each Option
        IntStream.range(0, form.options().size()).forEach(i -> {
            var pk = new OptionPk();
            pk.setPollId(saved.getId());
            pk.setSequenceNo(i + 1);

            var option = new Option();
            option.setId(pk);
            option.setTitle(form.options().get(i));
            option.setPoll(saved);
            optionRepo.save(option);
        });

        return saved.getId();
    }

    @Override
    @Transactional
    public PollDetail findById(UUID id) {
        var poll = pollRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Poll not found: " + id));

        var options = poll.getOptions().stream()
                .map(o -> new OptionItem(
                        o.getId().getPollId() + "-" + o.getId().getSequenceNo(),
                        o.getTitle(),
                        new ArrayList<>()
                ))
                .toList();

        return new PollDetail(
                poll.getId(),
                poll.getTitle(),
                null,
                poll.getEndTime(),
                poll.getVoteLimit(),
                poll.getStatus(),
                options
        );
    }

    @Override
    @Transactional
    public List<PollDetail> getAll() {
        return pollRepo.findAll().stream()
                .map(poll -> findById(poll.getId()))
                .toList();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        pollRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void create(PollDetail poll, boolean isOwner) {
        var entity = new Poll();
        entity.setId(poll.id());
        entity.setTitle(poll.title());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setEndTime(poll.endTime());
        entity.setStatus(poll.status());
        entity.setIsOwner(isOwner);
        pollRepo.save(entity);
    }
}
