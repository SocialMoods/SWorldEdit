package ru.SocialMoods.SWorldEdit.commands;

import cn.nukkit.level.Position;
import ru.SocialMoods.SWorldEdit.PlayerData;
import ru.SocialMoods.SWorldEdit.utils.BlocksArray;
import ru.SocialMoods.SWorldEdit.utils.WorldUtils;

@SuppressWarnings("unused")
public class PasteCommand extends WECommand {
    public PasteCommand() {
        super("/paste", "Paste area");
    }

    @Override
    public void execute(cn.nukkit.Player p, PlayerData dat, String[] args) {
        try {
            if (dat == null) {
                p.sendMessage("Player data not found");
                return;
            }

            BlocksArray copy = dat.copiedBlocks;
            if (copy == null || copy.blocks.isEmpty()) {
                p.sendMessage("Clipboard is empty. Use //copy first to copy blocks");
                return;
            }

            Position pastePosition = new Position(p.getX(), p.getY(), p.getZ(), p.getLevel());

            WorldUtils.pasteAsync(dat, pastePosition, copy)
                    .thenAccept(blocksPasted -> {
                        p.sendMessage("Successfully pasted " + blocksPasted + " blocks");
                    })
                    .exceptionally(throwable -> {
                        p.sendMessage("Error while pasting: " + throwable.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            p.sendMessage("Error executing paste command: " + e.getMessage());
        }
    }
}