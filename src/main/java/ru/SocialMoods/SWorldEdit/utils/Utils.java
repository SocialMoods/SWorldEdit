package ru.SocialMoods.SWorldEdit.utils;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import ru.SocialMoods.SWorldEdit.SWorldEdit;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static final Map<String, Integer> blockNameToIdMap = new HashMap<>();

    private static boolean blockTypesAvailable = true;

    static {
        initializeBlockNameMapping();
    }

    private static void initializeBlockNameMapping() {
        try {
            Field[] fields = BlockID.class.getFields();
            for (Field field : fields) {
                if (field.getType() == int.class) {
                    try {
                        String fieldName = field.getName().toLowerCase();
                        int fieldValue = field.getInt(null);
                        blockNameToIdMap.put(fieldName, fieldValue);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception e) {
            SWorldEdit.get().getLogger().warning("Failed to initialize block name mapping: " + e.getMessage());
        }
    }

    public static Block fromString(String str, Player player) {
        if (str == null || str.isEmpty()) {
            if (player != null) {
                player.sendMessage("Empty block string provided");
            }
            return null;
        }

        if (blockTypesAvailable) {
            Block block = parseWithBlockTypes(str, player);
            if (block != null) {
                return block;
            }
        }

        Block block = parseWithBlockIdMapping(str, player);
        if (block != null) {
            return block;
        }

        return parseWithIdDataFormat(str, player);
    }

    private static Block parseWithBlockTypes(String str, Player player) {
        try {
            Class<?> blockTypesClass = Class.forName("cn.nukkit.block.material.BlockTypes");
            Class<?> blockTypeClass = Class.forName("cn.nukkit.block.material.BlockType");

            String blockStr = str;
            if (!str.startsWith("minecraft:")) {
                blockStr = "minecraft:" + str;
            }

            Object blockType = blockTypesClass.getMethod("get", String.class).invoke(null, blockStr);
            if (blockType != null) {
                Object block = blockTypeClass.getMethod("createBlock").invoke(blockType);
                if (block instanceof Block) {
                    if (player != null) {
                        player.sendMessage("Block created using BlockType: " + blockStr);
                    }
                    return (Block) block;
                }
            }
        } catch (Exception e) {
            blockTypesAvailable = false;
            SWorldEdit.get().getLogger().warning("BlockTypes not available, falling back to BlockID mapping. Error: " + e.getMessage());
        }
        return null;
    }

    private static Block parseWithBlockIdMapping(String str, Player player) {
        try {
            String cleanStr = str.contains(":") ? str.split(":")[0] : str;
            cleanStr = cleanStr.toLowerCase().replace("minecraft:", "");

            int blockId = blockNameToIdMap.get(cleanStr);
            if (blockId != 0) {
                int blockData = 0;
                if (str.contains(":")) {
                    String[] parts = str.split(":");
                    if (parts.length > 1) {
                        blockData = Integer.parseInt(parts[1]);
                    }
                }

                Block block = Block.get(blockId, blockData);
                if (player != null) {
                    player.sendMessage("Block created using BlockID mapping: " + str);
                }
                return block;
            }
        } catch (Exception e) {
            SWorldEdit.get().getLogger().warning("Failed to parse block using BlockID mapping: " + str + " - " + e.getMessage());
        }
        return null;
    }

    private static Block parseWithIdDataFormat(String str, Player player) {
        try {
            String[] parts = str.split(":");
            int blockId = Integer.parseInt(parts[0]);
            int blockData = 0;

            if (parts.length > 1) {
                blockData = Integer.parseInt(parts[1]);
            }

            Block block = Block.get(blockId, blockData);

            if (player != null) {
                player.sendMessage("Block created using ID:DATA format: " + str);
            }

            return block;
        } catch (NumberFormatException e) {
            if (player != null) {
                player.sendMessage("Invalid block format: " + str + ". Use ID:DATA or block name");
            }
            SWorldEdit.get().getLogger().warning("Invalid block format: " + str);
        } catch (Exception e) {
            if (player != null) {
                player.sendMessage("Failed to create block: " + str);
            }
            SWorldEdit.get().getLogger().warning("Failed to create block: " + str + " - " + e.getMessage());
        }
        return null;
    }
}