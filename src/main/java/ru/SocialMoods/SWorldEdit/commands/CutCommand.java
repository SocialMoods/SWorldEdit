package ru.SocialMoods.SWorldEdit.commands;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import ru.SocialMoods.SWorldEdit.PlayerData;
import ru.SocialMoods.SWorldEdit.utils.Selection;
import ru.SocialMoods.SWorldEdit.utils.WorldUtils;

@SuppressWarnings("unused")
public class CutCommand extends WECommand {
    public CutCommand() {
        super("/cut", "Delete blocks in area");
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

            Block airBlock = new BlockAir();

            WorldUtils.setAsync(dat, sel.getPosOne(), sel.getPosTwo(), airBlock)
                    .thenAccept(blocksChanged -> {
                        p.sendMessage("Successfully removed " + blocksChanged + " blocks");
                    })
                    .exceptionally(throwable -> {
                        p.sendMessage("Error while cutting blocks: " + throwable.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            p.sendMessage("Error executing cut command: " + e.getMessage());
        }
    }
}