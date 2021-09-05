package io.github.lukeeey.simplequests.quest;

import io.github.lukeeey.simplequests.quest.handler.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestType {
    KILL_MOB(KillMobHandler.class),
    BLOCK_BREAK(BreakBlockHandler.class),
    BLOCK_PLACE(PlaceBlockHandler.class),
    RUN_COMMAND(RunCommandHandler.class);

    private final Class<? extends QuestHandler> handlerClass;
}