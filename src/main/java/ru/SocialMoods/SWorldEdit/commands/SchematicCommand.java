package ru.SocialMoods.SWorldEdit.commands;

import cn.nukkit.Player;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import lombok.extern.slf4j.Slf4j;
import ru.SocialMoods.SWorldEdit.PlayerData;
import ru.SocialMoods.SWorldEdit.service.SchematicService;

@Slf4j
public class SchematicCommand extends WECommand {
    private final SchematicService schematicService;

    public SchematicCommand(SchematicService schematicService) {
        super("/schem", "Save and load schematics");
        this.schematicService = schematicService;

        this.setAliases(java.util.List.of("/schematic").toArray(new String[0]));
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newEnum("action", new String[]{"save", "paste"}),
                CommandParameter.newType("name", CommandParamType.STRING)
        });
    }

    @Override
    public void execute(Player player, PlayerData dat, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Usage: //schem <save|paste> <name> [x y z]");
            return;
        }

        String action = args[0].toLowerCase();
        String name = args[1];

        switch (action) {
            case "save" -> {
                player.sendMessage("Saving schematic...");
                schematicService.save(dat, name).whenComplete((v, ex) -> {
                    if (ex != null) {
                        log.error("Error while saving schematic {}", name, ex);
                        player.sendMessage("Error: " + ex.getMessage());
                        return;
                    }
                    player.sendMessage("Schematic '" + name + "' has been saved");
                });
            }
            case "paste" -> {
                int x = player.getFloorX();
                int y = player.getFloorY();
                int z = player.getFloorZ();
                var center = player.getLevel().getBlock(x, y, z).getLocation();
                player.sendMessage("Loading schematic...");
                schematicService.load(dat, center, name).whenComplete((placed, ex) -> {
                    if (ex != null) {
                        log.error("Error while pasting schematic {}", name, ex);
                        player.sendMessage("Error: " + ex.getMessage());
                        return;
                    }
                    player.sendMessage("Schematic '" + name + "' has been pasted (" + placed + " blocks)");
                });
            }
            default -> player.sendMessage("Unknown action: " + action);
        }
    }
}
