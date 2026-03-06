package edu.ucsy.app.server.service;

import edu.ucsy.app.rmi.dto.OptionItem;

import java.util.List;

public interface VoteManagementService {

    void bulkCreate(List<OptionItem> items);

}
