package edu.ucsy.app.server.service;

import edu.ucsy.app.rmi.dto.PollDetail;
import edu.ucsy.app.rmi.dto.PollForm;

import java.util.List;
import java.util.UUID;

public interface PollManagementService {

    UUID create(PollForm form);

    PollDetail findById(UUID id);

    List<PollDetail> getAll();

    void delete(UUID id);

    void create(PollDetail poll, boolean isOwner);
}
