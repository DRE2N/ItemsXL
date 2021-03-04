package de.erethon.itemsxl.command;

import de.erethon.caliburn.CaliburnAPI;
import de.erethon.caliburn.category.IdentifierType;
import de.erethon.caliburn.item.ExItem;
import de.erethon.caliburn.loottable.LootTable;
import de.erethon.caliburn.mob.ExMob;
import de.erethon.caliburn.recipe.CustomRecipe;
import de.erethon.commons.command.DRECommand;
import de.erethon.commons.command.DRECommandCache;
import de.erethon.itemsxl.ItemsXL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ICommandCache extends DRECommandCache implements TabCompleter {

    public static final String LABEL = "itemsxl";

    private final CaliburnAPI api;

    public ICommandCache(ItemsXL plugin) {
        super(LABEL, plugin,
                new HelpCommand(plugin),
                new GiveCommand(plugin),
                new ListCommand(plugin),
                new LootTableCommand(plugin),
                new MainCommand(plugin),
                new OpenCommand(plugin),
                new RegisterItemCommand(plugin),
                new RegisterMobCommand(plugin),
                new ReloadCommand(plugin),
                new SerializeCommand(plugin),
                new SummonCommand(plugin),
                new RecipeEditorCommand(plugin)
        );
        this.api = plugin.getAPI();
    }

    @Override
    public void register(JavaPlugin plugin) {
        super.register(plugin);
        plugin.getCommand(LABEL).setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> cmds = new ArrayList<>();
        for (DRECommand cmd : getCommands()) {
            if (cmd.getPermission() == null || cmd.getPermission().isEmpty() || sender.hasPermission(cmd.getPermission())) {
                cmds.add(cmd.getCommand());
            }
        }
        List<String> completes = new ArrayList<>();
        String cmd = args[0];

        if (args.length == 1) {
            for(String string : cmds) {
                if(string.toLowerCase().startsWith(cmd)) completes.add(string);
            }
        } else {
            if (args.length == 2) {
                if (cmd.equalsIgnoreCase("editor")) {
                    for (CustomRecipe recipe : api.getRecipes()) {
                        if (recipe.getId().toLowerCase().startsWith(args[1].toLowerCase())) {
                            completes.add(recipe.getId());
                        }
                    }
                }
                if (cmd.equalsIgnoreCase("give")) {
                    for (ExItem item : api.getExItems()) {
                        if (item.getId().toLowerCase().startsWith(args[1].toLowerCase())) {
                            completes.add(item.getId());
                        }
                    }
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (online.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                            completes.add(online.getName());
                        }
                    }
                }
                if (cmd.equalsIgnoreCase("info")) {
                    for (ExItem item : api.getExItems()) {
                        if (item.getId().toLowerCase().startsWith(args[1].toLowerCase())) {
                            completes.add(item.getId());
                        }
                    }
                }
                if (cmd.equalsIgnoreCase("list")) {
                    for (String arg : Arrays.asList("vanillaItems", "customMobs", "vanillaMobs", "mobs", "lootTables", "recipes")) {
                        if (arg.toLowerCase().startsWith(args[1].toLowerCase())) {
                            completes.add(arg);
                        }
                    }
                }
                if (cmd.equalsIgnoreCase("lootTable") || cmd.equalsIgnoreCase("lt")) {
                    for (LootTable table : api.getLootTables()) {
                        if (table.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                            completes.add(table.getName());
                        }
                    }
                }
                if (cmd.equalsIgnoreCase("summon")) {
                    for (ExMob mob : api.getExMobs()) {
                        if (mob.getId().toLowerCase().startsWith(args[1].toLowerCase())) {
                            completes.add(mob.getId());
                        }
                    }
                }
            }
            if (args.length == 3) {
                if (cmd.equalsIgnoreCase("registerMob") || cmd.equalsIgnoreCase("rm") ||
                        cmd.equalsIgnoreCase("registerItem") || cmd.equalsIgnoreCase("ri")) {

                    for (IdentifierType identifier : IdentifierType.values()) {
                        if (identifier.name().toLowerCase().startsWith(args[2].toLowerCase())) {
                            completes.add(identifier.name());
                        }
                    }
                }
                if (cmd.equalsIgnoreCase("give")) {
                    for (ExItem item : api.getExItems()) {
                        if (item.getId().toLowerCase().startsWith(args[2].toLowerCase())) {
                            completes.add(item.getId());
                        }
                    }
                }
            }
        }
        return completes;
    }
}
