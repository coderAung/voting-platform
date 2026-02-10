package edu.ucsy.app.rmi.event;

import edu.ucsy.app.rmi.dto.PollDetail;

import java.io.Serial;
import java.io.Serializable;

public record PollEndEvent(PollDetail poll) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
