package edu.ucsy.app.server.service;

import edu.ucsy.app.rmi.dto.VoteDetail;

import java.util.List;

public interface VoteManagementService {

    void create(VoteDetail form);

    void bulkCreate(List<VoteDetail> items);
}
