package de.erethon.itemsxl.command;

import de.erethon.caliburn.CaliburnAPI;
import de.erethon.caliburn.recipe.CustomRecipe;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.erethon.itemsxl.ItemsXL;
import de.erethon.itemsxl.config.IMessage;
import org.bukkit.command.CommandSender;

public class UnregisterRecipeCommand extends DRECommand {

    private final CaliburnAPI api;

    public UnregisterRecipeCommand(ItemsXL plugin) {
        this.api = plugin.getAPI();
        setCommand("unregisterRecipe");
        setAliases("ur");
        setMinArgs(1);
        setMaxArgs(1);
        setHelp(IMessage.HELP_UNREGISTER_RECIPE.getMessage());
        setPermission("itemsxl.unregisterRecipe");
        setPlayerCommand(true);
        setConsoleCommand(true);
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        CustomRecipe recipe = api.getRecipe(args[1]);
        if (recipe == null) {
            MessageUtil.sendMessage(sender, IMessage.ERROR_RECIPE_NOT_FOUND.getMessage(args[1]));
            return;
        }
        api.unregisterRecipe(recipe);
        api.deleteRecipe(recipe);
        MessageUtil.sendMessage(sender, IMessage.COMMAND_UNREGISTER_RECIPE_SUCCESS.getMessage());
    }
}
