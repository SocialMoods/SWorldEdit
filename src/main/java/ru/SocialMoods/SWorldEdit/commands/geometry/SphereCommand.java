package ru.SocialMoods.SWorldEdit.commands.geometry;

import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import ru.SocialMoods.SWorldEdit.PlayerData;
import ru.SocialMoods.SWorldEdit.commands.WECommand;
import ru.SocialMoods.SWorldEdit.utils.Utils;
import ru.SocialMoods.SWorldEdit.utils.WorldUtils;

@SuppressWarnings("unused")
public class SphereCommand extends WECommand {
    public SphereCommand() {
        super("/sphere", "Make sphere");
    }

    @Override
    public void execute(cn.nukkit.Player p, PlayerData dat, String[] args) {
        try {
            if (dat == null) {
                p.sendMessage("Player data not found");
                return;
            }

            if (args.length != 2) {
                p.sendMessage("Usage: //sphere <block_id>[:data] <radius>");
                p.sendMessage("Example: //sphere 1 5, //sphere stone 3");
                return;
            }

            Block block = Utils.fromString(args[0], p);
            if (block == null) {
                p.sendMessage("Block '" + args[0] + "' doesn't exist or is invalid");
                return;
            }

            int radius;
            try {
                radius = Integer.parseInt(args[1]);
                if (radius <= 0) {
                    p.sendMessage("Radius must be a positive number");
                    return;
                }
                if (radius > 50) {
                    p.sendMessage("Radius is too large (maximum 50)");
                    return;
                }
            } catch (NumberFormatException e) {
                p.sendMessage("Invalid radius. Please use a number");
                return;
            }

            Position center = p.getPosition();

            WorldUtils.sphereAsync(dat, center, radius, block)
                    .thenAccept(blocksChanged -> {
                        p.sendMessage("Successfully created sphere with " + blocksChanged + " blocks");
                    })
                    .exceptionally(throwable -> {
                        p.sendMessage("Error while creating sphere: " + throwable.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            p.sendMessage("Error executing sphere command: " + e.getMessage());
        }
    }
}