package edu.ucsy.app.server.service;

import edu.ucsy.app.rmi.dto.PollDetail;
import edu.ucsy.app.rmi.dto.PollForm;
import edu.ucsy.app.server.entities.Poll;

import java.util.List;
import java.util.UUID;

public interface PollManagementService {

    UUID create(PollForm form);

    PollDetail findById(UUID id);

    List<PollDetail> getAll();

    void delete(UUID id);

    void create(PollDetail detail, boolean isOwner);

    void changeStatus(UUID id, Poll.Status status);
}
