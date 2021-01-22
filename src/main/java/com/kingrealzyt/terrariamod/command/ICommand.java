package com.kingrealzyt.terrariamod.command;

import java.util.List;

public interface ICommand {

    void handle(CommandContext event);

    String getName();

    default List<String> getAliases() {
        return List.of();
    }

}
