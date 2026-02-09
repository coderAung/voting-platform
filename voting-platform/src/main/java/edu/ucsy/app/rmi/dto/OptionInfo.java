package edu.ucsy.app.rmi.dto;

import java.io.Serial;
import java.io.Serializable;

public record OptionInfo(
        String id,
        String title,
        int votes
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static OptionInfo from(OptionItem optionItem) {
        return new OptionInfo(optionItem.id(), optionItem.title(), optionItem.votes());
    }
}
