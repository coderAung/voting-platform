package edu.ucsy.app.rmi.event;

import java.io.Serial;
import java.io.Serializable;

public record PollEndEvent() implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
