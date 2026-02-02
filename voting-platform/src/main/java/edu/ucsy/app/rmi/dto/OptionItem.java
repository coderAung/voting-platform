package edu.ucsy.app.rmi.dto;

import java.io.Serial;
import java.io.Serializable;

public record OptionItem(
        String id,
        String title,
        int votes
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
