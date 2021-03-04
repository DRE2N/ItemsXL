package de.erethon.itemsxl.command;

import de.erethon.caliburn.CaliburnAPI;
import de.erethon.caliburn.recipe.CustomRecipe;
import de.erethon.caliburn.util.ValidationException;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.erethon.itemsxl.ItemsXL;
import de.erethon.itemsxl.config.IMessage;
import de.erethon.itemsxl.recipe.RecipeEditor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RecipeEditorCommand extends DRECommand {

    private final CaliburnAPI api;

    public RecipeEditorCommand(ItemsXL plugin) {
        api = plugin.getAPI();
        setCommand("recipeEditor");
        setAliases("re", "recipe", "editor");
        setMinArgs(1);
        setMaxArgs(1);
        setHelp(IMessage.HELP_RECIPE_EDITOR.getMessage());
        setPermission("ixl.recipeEditor");
        setPlayerCommand(true);
        setConsoleCommand(false);
    }

    // ixl recipeEditor [recipe]
    @Override
    public void onExecute(String[] args, CommandSender sender) {
        Player player = (Player) sender;
        try {
            if (!player.hasPermission("ixl.recipeEditor")) {
                MessageUtil.sendMessage(player, IMessage.ERROR_NO_PERMISSION.getMessage("/ixl recipeEditor"));
                return;
            }
            String id = args[1];
            CustomRecipe existing = api.getRecipe(id);
            if (existing == null) {
                RecipeEditor.startSession(id, player);
            } else {
                RecipeEditor.startSession(existing, player);
            }
        } catch (IllegalArgumentException | ValidationException e) {
            MessageUtil.sendMessage(player, ChatColor.RED + e.getMessage());
        }
    }
}
