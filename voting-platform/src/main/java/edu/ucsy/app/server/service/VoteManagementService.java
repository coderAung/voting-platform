package edu.ucsy.app.server.service;

import edu.ucsy.app.rmi.dto.PollDetail;

public interface VoteManagementService {

    void bulkCreate(PollDetail pollDetail);

}
