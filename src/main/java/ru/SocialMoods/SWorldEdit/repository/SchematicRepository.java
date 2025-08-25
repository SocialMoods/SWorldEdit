package ru.SocialMoods.SWorldEdit.repository;

import ru.SocialMoods.SWorldEdit.data.Schematic;

import java.io.IOException;

public interface SchematicRepository {

    void save(Schematic schematic, String name) throws IOException;

    Schematic load(String name) throws IOException;
}
