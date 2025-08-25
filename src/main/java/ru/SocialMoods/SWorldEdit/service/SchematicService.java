package ru.SocialMoods.SWorldEdit.service;

import cn.nukkit.level.Position;
import ru.SocialMoods.SWorldEdit.PlayerData;

import java.util.concurrent.CompletableFuture;

public interface SchematicService {

    CompletableFuture<Void> save(PlayerData dat, String name);

    CompletableFuture<Integer> load(PlayerData dat, Position center, String name);
}
