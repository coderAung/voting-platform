package edu.ucsy.app.server.service;

import edu.ucsy.app.rmi.dto.OptionItem;
import edu.ucsy.app.rmi.dto.VoteDetail;

import java.util.List;
import java.util.UUID;

public interface VoteManagementService {

    void create(VoteDetail form);

    void bulkCreate(UUID pollId, List<OptionItem> items);
}
