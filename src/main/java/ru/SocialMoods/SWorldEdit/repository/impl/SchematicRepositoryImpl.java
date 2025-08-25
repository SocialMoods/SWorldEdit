package ru.SocialMoods.SWorldEdit.repository.impl;

import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import ru.SocialMoods.SWorldEdit.data.Schematic;
import ru.SocialMoods.SWorldEdit.repository.SchematicRepository;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SchematicRepositoryImpl implements SchematicRepository {
    private final File baseDir;

    public SchematicRepositoryImpl(File baseDir) {
        this.baseDir = baseDir;
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    @Override
    public void save(Schematic schematic, String name) throws IOException {
        CompoundTag tag = new CompoundTag()
                .putShort("Width", schematic.getWidth())
                .putShort("Height", schematic.getHeight())
                .putShort("Length", schematic.getLength())
                .putByteArray("Blocks", schematic.getBlocks())
                .putByteArray("Data", schematic.getData());

        File file = new File(baseDir, name + ".schem");
        try (FileOutputStream fos = new FileOutputStream(file);
             GZIPOutputStream gzos = new GZIPOutputStream(fos)) {
            NBTIO.write(tag, gzos);
        }
    }

    @Override
    public Schematic load(String name) throws IOException {
        File file = new File(baseDir, name + ".schem");
        try (FileInputStream fis = new FileInputStream(file);
             GZIPInputStream gzis = new GZIPInputStream(fis)) {
            CompoundTag tag = NBTIO.read(gzis);
            return new Schematic(
                    (short) tag.getShort("Width"),
                    (short) tag.getShort("Height"),
                    (short) tag.getShort("Length"),
                    tag.getByteArray("Blocks"),
                    tag.getByteArray("Data")
            );
        }
    }
}
