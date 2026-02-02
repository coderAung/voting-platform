package edu.ucsy.app.rmi.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record PollDetail(
        UUID id,
        String title,
        List<OptionItem> options
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
