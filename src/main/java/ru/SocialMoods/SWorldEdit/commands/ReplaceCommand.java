package ru.SocialMoods.SWorldEdit.commands;

import cn.nukkit.block.Block;
import ru.SocialMoods.SWorldEdit.PlayerData;
import ru.SocialMoods.SWorldEdit.utils.Selection;
import ru.SocialMoods.SWorldEdit.utils.Utils;
import ru.SocialMoods.SWorldEdit.utils.WorldUtils;

@SuppressWarnings("unused")
public class ReplaceCommand extends WECommand {
    public ReplaceCommand() {
        super("/replace", "Replace blocks in selected area");
    }

    @Override
    public void execute(cn.nukkit.Player p, PlayerData dat, String[] args) {
        try {
            if (dat == null) {
                p.sendMessage("Player data not found");
                return;
            }

            Selection sel = dat.getSelection();
            if (sel == null || sel.getPosOne() == null || sel.getPosTwo() == null) {
                p.sendMessage("Please select area first using //pos1 and //pos2");
                return;
            }

            if (args.length != 2) {
                p.sendMessage("Usage: //replace <original_block> <replacement_block>");
                return;
            }

            Block originalBlock = Utils.fromString(args[0], p);
            Block replacementBlock = Utils.fromString(args[1], p);

            if (originalBlock == null) {
                p.sendMessage("Original block '" + args[0] + "' doesn't exist or is invalid");
                return;
            }

            if (replacementBlock == null) {
                p.sendMessage("Replacement block '" + args[1] + "' doesn't exist or is invalid");
                return;
            }

            WorldUtils.replaceAsync(dat, sel.getPosOne(), sel.getPosTwo(), originalBlock, replacementBlock)
                    .thenAccept(blocksReplaced -> {
                        p.sendMessage("Successfully replaced " + blocksReplaced + " blocks");
                    })
                    .exceptionally(throwable -> {
                        p.sendMessage("Error while replacing blocks: " + throwable.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            p.sendMessage("Error executing replace command: " + e.getMessage());
        }
    }
}