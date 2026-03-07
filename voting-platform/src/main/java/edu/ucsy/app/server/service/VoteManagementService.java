package edu.ucsy.app.server.service;

import edu.ucsy.app.rmi.dto.OptionItem;
import edu.ucsy.app.rmi.dto.PollDetail;

import java.util.List;

public interface VoteManagementService {

    void bulkCreate(PollDetail pollDetail);

}
