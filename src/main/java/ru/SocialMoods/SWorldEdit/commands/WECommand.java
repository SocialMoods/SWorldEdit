package ru.SocialMoods.SWorldEdit.commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.SocialMoods.SWorldEdit.PlayerData;
import ru.SocialMoods.SWorldEdit.SWorldEdit;

@SuppressWarnings("unused")
public abstract class WECommand extends Command {
    public WECommand(String name, String description) {
        super(name, description);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof cn.nukkit.Player player)) {
            sender.sendMessage("WorldEdit is only available in game");
            return false;
        }

        if (!player.hasPermission("we.command")) {
            player.sendMessage("You don't have permission to use WorldEdit commands");
            return false;
        }

        try {
            PlayerData wePlayerData = SWorldEdit.get().getWEPlayer(player);
            if (wePlayerData == null) {
                player.sendMessage("Failed to get WorldEdit player data");
                return false;
            }

            this.execute(player, wePlayerData, args);

        } catch (Exception e) {
            player.sendMessage("Error executing command: " + e.getMessage());
        }

        return true;
    }

    public void checkForErrors(cn.nukkit.Player player, PlayerData dat) {
        if (dat == null) {
            player.sendMessage("Player data not found");
            return;
        }

        if (!dat.hasSelection()) {
            player.sendMessage("You need to select both positions first using //pos1 and //pos2");
            return;
        }

        if (dat.getSelection().getPosOne() == null) {
            player.sendMessage("First position not set. Use //pos1 to set it");
            return;
        }

        if (dat.getSelection().getPosTwo() == null) {
            player.sendMessage("Second position not set. Use //pos2 to set it");
        }
    }

    public abstract void execute(cn.nukkit.Player player, PlayerData wePlayerData, String[] args);
}